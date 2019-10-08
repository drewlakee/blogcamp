package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
import ru.aleynikov.blogcamp.component.PostComponent;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.FilterDataManager;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.List;
import java.util.Map;

@Route(value = "globe", layout = MainLayout.class)
@PageTitle("Posts - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class GlobeView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private PostService postService;

    private static final int POST_ON_PAGE_LIMIT = 5;

    private User currentUser;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private Label homeLabel = new Label("Home");

    private Button addPostButton = new Button("Add post");

    private H2 notFoundedH2 = new H2("Posts not founded.");

    private Tabs sortBar = new Tabs();
    private Tab newestTab = new Tab("Newest");

    private TextField searchPostField = new TextField();

    private Icon searchPostFieldIcon = new Icon(VaadinIcon.SEARCH);

    private String sortTab;
    private String filter;
    private int page = 1;
    private Map<String, List<String>> qparams;

    public GlobeView() {
        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();
        headerLayout.addClassName("content-header");

        headerUpLayout.setWidth("100%");

        homeLabel.addClassName("content-label");

        addPostButton.addClassName("main-button");
        addPostButton.addClassName("rs-cmp");
        addPostButton.addClassName("margin-r-16px");

        headerUpLayout.add(homeLabel, addPostButton);

        headerLowerLayout.setWidth("100%");

        searchPostField.setPlaceholder("Filter by post title");
        searchPostField.setPrefixComponent(searchPostFieldIcon);
        searchPostField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchPostField);

        sortBar.add(newestTab);
        sortBar.addClassName("rs-cmp");
        sortBar.addClassName("tabs-bar");

        sortTab = sortBar.getSelectedTab().getLabel();

        headerLowerLayout.add(searchPostField, sortBar);

        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        addPostButton.addClickListener(event -> {
            UI.getCurrent().navigate(EditorPostView.class);
        });

        sortBar.addSelectedChangeListener(event -> {
            if (sortBar.getSelectedTab() != null) {
                String selectedTab = event.getSource().getSelectedTab().getLabel();

                if (selectedTab.equals(newestTab.getLabel())) {
                    UI.getCurrent().navigate("globe", QueryParametersManager.queryParametersBuild(newestTab.getLabel()));
                }
            }
        });

        searchPostFieldIcon.addClickListener(event -> searchFieldProcess());

        searchPostField.addKeyPressListener(Key.ENTER, keyEventListener -> searchFieldProcess());
    }

    private void searchFieldProcess() {
        sortBar.setSelectedTab(null);
        UI.getCurrent().navigate("globe", QueryParametersManager.querySearchParametersBuild(searchPostField.getValue().strip()));
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        currentUser = SecurityUtils.getPrincipal();
        queryParametersSetter();

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else if (sortTab.equals(newestTab.getLabel())) {
                sortBar.setSelectedTab(newestTab);
            }
        }

        if (qparams.containsKey("search")) {
            postsBrowserBuild(page, sortTab, event.getLocation().getPath(), filter);
        } else
            postsBrowserBuild(page, sortTab, event.getLocation().getPath(), "");
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

    private void postsBrowserBuild(int page, String sortTab, String locationPath, String filter) {
        int pageLimit;
        List<Post> posts = null;
        float count = 0;

        if (!filter.isEmpty()) {
            posts = postService.findPostsByTitle(page, POST_ON_PAGE_LIMIT, filter);
            count = postService.countPostsByTitle(filter);
        } else if (sortTab.equals(newestTab.getLabel())) {
            posts = postService.sortNewestPosts(page, POST_ON_PAGE_LIMIT);
            count = postService.count();
        }

        bodyLayout.removeAll();

        if (!posts.isEmpty()) {
            for (Post post : posts) {
                PostComponent postComponent = new PostComponent(post);
                bodyLayout.add(postComponent);
            }

            pageLimit = FilterDataManager.pageLimit(count, POST_ON_PAGE_LIMIT);

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, QueryParametersManager.queryParametersBuild(page, sortTab)));
        } else {
            bodyLayout.add(notFoundedH2);
        }
    }
}
