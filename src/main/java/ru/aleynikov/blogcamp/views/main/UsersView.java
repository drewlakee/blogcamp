package ru.aleynikov.blogcamp.views.main;

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
import ru.aleynikov.blogcamp.component.PageSwitcherComponent;
import ru.aleynikov.blogcamp.component.UserComponent;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.service.FilterDataManager;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.List;
import java.util.Map;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class UsersView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private UserService userService;

    private static final int USERS_ON_PAGE_LIMIT = 12;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private TextField searchUserField = new TextField();

    private Icon searchUserFieldIcon = new Icon(VaadinIcon.SEARCH);

    private Label tagsLabel = new Label("Users");

    private H2 notFoundedH2 = new H2("Users not founded.");

    private Tabs sortBar = new Tabs();
    private Tab nameTab = new Tab("A - Z");

    private int page = 1;
    private String sortTab;
    private String filter;
    private Map<String, List<String>> qparams;

    public UsersView() {
        getContent().setSizeFull();

        contentLayout.setSizeFull();
        contentLayout.getStyle().set("padding", "0");

        headerUpLayout.setSizeFull();

        tagsLabel.addClassName("content-label");

        headerUpLayout.add(tagsLabel);

        headerLowerLayout.setSizeFull();

        searchUserField.setPlaceholder("Filter by user");
        searchUserField.setPrefixComponent(searchUserFieldIcon);
        searchUserField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchUserField);

        sortBar.addClassName("rs-cmp");
        sortBar.addClassName("tabs-bar");
        sortBar.add(nameTab);

        sortTab = sortBar.getSelectedTab().getLabel();

        headerLowerLayout.add(searchUserField, sortBar);

        headerLayout.addClassName("content-header");
        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        sortBar.addSelectedChangeListener(event -> {
            if (sortBar.getSelectedTab() != null) {
                String selectedTab = event.getSource().getSelectedTab().getLabel();

                if (selectedTab.equals(nameTab.getLabel())) {
                    UI.getCurrent().navigate("users", QueryParametersManager.queryParametersBuild(nameTab.getLabel()));
                }
            }
        });

        searchUserField.addKeyPressListener(Key.ENTER, keyEventListener -> searchFieldProcess());

        searchUserFieldIcon.addClickListener(event -> searchFieldProcess());
    }

    private void searchFieldProcess() {
        sortBar.setSelectedTab(null);
        UI.getCurrent().navigate("users", QueryParametersManager.querySearchParametersBuild(searchUserField.getValue().strip()));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        queryParametersSetter();

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else if (sortTab.equals(nameTab.getLabel())) {
                sortBar.setSelectedTab(nameTab);
            }
        }

        if (qparams.containsKey("search")) {
            tagsBrowserBuild(page, sortTab, event.getLocation().getPath(), filter);
        } else
            tagsBrowserBuild(page, sortTab, event.getLocation().getPath(), "");
    }

    public void queryParametersSetter() {
        if (qparams.containsKey("page")) {
            page = Integer.parseInt(qparams.get("page").get(0));
        }

        if (qparams.containsKey("tab")) {
            sortTab = qparams.get("tab").get(0);
        }

        if (qparams.containsKey("search")) {
            filter = qparams.get("search").get(0);
        }
    }

    private void tagsBrowserBuild(int page, String sortTab, String locationPath, String filter) {
        int rowLimit = 3;
        int pageLimit;
        int counter = 0;
        HorizontalLayout row = new HorizontalLayout();;
        List<User> userList = null;
        float countUsers = 0;

        bodyLayout.removeAll();

        row.setWidth("100%");

        if (!filter.isEmpty()) {
            userList = userService.getFilterByUsernameUsersList(page, USERS_ON_PAGE_LIMIT, filter);
            countUsers = userService.getFilterUsersCount(filter);
        } else if (sortTab.equals(nameTab.getLabel())) {
            userList = userService.getSortedByUsernameUserList(page, USERS_ON_PAGE_LIMIT);
            countUsers = userService.getAllUsersCount();
        }

        if (!userList.isEmpty()) {
            for (User user : userList) {
                counter += 1;
                row.add(new UserComponent(user));

                if (counter == rowLimit || userList.indexOf(user) == userList.size() - 1) {
                    bodyLayout.add(row);
                    if (!(userList.indexOf(user) == userList.size() - 1)) {
                        row = new HorizontalLayout();
                        row.setWidth("100%");
                    }
                    counter = 0;
                }
            }

            pageLimit = FilterDataManager.pageLimit(countUsers, USERS_ON_PAGE_LIMIT);

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, QueryParametersManager.queryParametersBuild(page, sortTab)));
        } else {
            bodyLayout.add(notFoundedH2);
        }
    }
}
