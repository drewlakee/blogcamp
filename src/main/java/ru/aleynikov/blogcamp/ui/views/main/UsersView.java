package ru.aleynikov.blogcamp.ui.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.ui.components.PageSwitcherComponent;
import ru.aleynikov.blogcamp.ui.components.UserComponent;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.services.DataHandleService;
import ru.aleynikov.blogcamp.services.QueryParametersConstructor;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users - Blogcamp")
@StyleSheet(StaticContent.MAIN_STYLES)
public class UsersView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private UserService userService;

    private User userInSession = SecurityUtils.getPrincipal();

    private static final int USERS_ON_PAGE_LIMIT = 12;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private TextField searchField = new TextField();

    private Icon searchUserFieldIcon = new Icon(VaadinIcon.SEARCH);

    private Label tagsLabel = new Label("Users");

    private H2 notFoundedH2 = new H2("Users not founded.");

    private Tabs sortBar = new Tabs();
    private Tab nameTab = new Tab("A-Z");

    private HashMap<String, Object> pageParametersMap = new HashMap<>(
            Map.of("tab", "",
                    "search","",
                    "page", "1")
    );
    private static Set<String> pageParametersKeySet;
    private Map<String, List<String>> qparams;

    public UsersView() {
        getContent().setSizeFull();

        contentLayout.setSizeFull();
        contentLayout.getStyle().set("padding", "0");

        headerUpLayout.setSizeFull();

        tagsLabel.addClassName("content-label");

        headerUpLayout.add(tagsLabel);

        headerLowerLayout.setSizeFull();

        searchField.setPlaceholder("Filter by user");
        searchField.setPrefixComponent(searchUserFieldIcon);
        searchField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchField);

        sortBar.addClassName("rs-cmp");
        sortBar.addClassName("tabs-bar");
        sortBar.add(nameTab);

        headerLowerLayout.add(searchField, sortBar);

        headerLayout.addClassName("content-header");
        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        sortBar.addSelectedChangeListener(event -> {
            if (sortBar.getSelectedTab() != null) {
                String selectedTab = event.getSource().getSelectedTab().getLabel();
                HashMap<String, Object> customQueryParams = new HashMap<>();

                if (selectedTab.equals(nameTab.getLabel())) {
                    customQueryParams.put("tab", nameTab.getLabel().toLowerCase());
                    UI.getCurrent().navigate("users", new QueryParameters(QueryParametersConstructor.buildQueryParams(customQueryParams)));
                }
            }
        });

        searchField.addKeyPressListener(Key.ENTER, keyEventListener -> search());

        searchUserFieldIcon.addClickListener(event -> search());
    }

    private void search() {
        if (!searchField.isEmpty()) {
            sortBar.setSelectedTab(null);
            HashMap<String, Object> customQueryParams = new HashMap<>();
            customQueryParams.put("search", searchField.getValue().strip());
            UI.getCurrent().navigate("users", new QueryParameters(QueryParametersConstructor.buildQueryParams(customQueryParams)));
        } else
            UI.getCurrent().navigate("users");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        pageParametersKeySet = pageParametersMap.keySet();
        QueryParametersConstructor.setQueryParamsToViewClass(qparams, pageParametersMap, pageParametersKeySet);

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else {
                sortBar.setSelectedTab(nameTab);
                pageParametersMap.replace("tab", nameTab.getLabel().toLowerCase());
            }
        }

        buildUsersBrowser(
                Integer.parseInt(pageParametersMap.get("page").toString()),
                pageParametersMap.get("tab").toString(),
                event.getLocation().getPath(),
                pageParametersMap.get("search").toString()
        );
    }

    private void buildUsersBrowser(int page, String sortTab, String locationPath, String filter) {
        int rowLimit = 3;
        int pageLimit;
        int counter = 0;
        HorizontalLayout row = new HorizontalLayout();;
        List<User> userList;
        int countUsers;
        HashMap<String, Object> customQueryParams = new HashMap<>();

        customQueryParams.put("page", page);

        bodyLayout.removeAll();

        row.setWidth("100%");

        if (!filter.isEmpty()) {
            userList = userService.getFilteredByUsernameUsersList(page, USERS_ON_PAGE_LIMIT, filter, userInSession.isAdmin());
            countUsers = userService.getFilteredUsersCount(filter);
        } else {
            userList = userService.getSortedByUsernameUsersList(page, USERS_ON_PAGE_LIMIT, userInSession.isAdmin());
            countUsers = userService.getAllUsersCount();
            customQueryParams.put("tab", sortTab);
        }

        if (!userList.isEmpty()) {
            for (User user : userList) {
                counter += 1;
                row.add(new UserComponent(user, userService));

                if (counter == rowLimit || userList.indexOf(user) == userList.size() - 1) {
                    bodyLayout.add(row);
                    if (!(userList.indexOf(user) == userList.size() - 1)) {
                        row = new HorizontalLayout();
                        row.setWidth("100%");
                    }
                    counter = 0;
                }
            }

            pageLimit = DataHandleService.calculatePageLimit(countUsers, USERS_ON_PAGE_LIMIT);

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, QueryParametersConstructor.buildQueryParams(customQueryParams)));
        } else {
            bodyLayout.add(notFoundedH2);
        }
    }
}
