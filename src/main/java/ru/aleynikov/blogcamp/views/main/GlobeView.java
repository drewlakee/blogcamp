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

import java.util.*;

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

    private HashMap<String, Object>  pageParametersMap = new HashMap<>(
            Map.of("tab", "",
                    "search","",
                    "tag", "",
                    "page", 1)
    );
    private static Set<String> pageParametersKeySet = Set.of("tab", "search", "tag", "page");
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

        searchPostField.setPlaceholder("Search by post title");
        searchPostField.setPrefixComponent(searchPostFieldIcon);
        searchPostField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchPostField);

        sortBar.add(newestTab);
        sortBar.addClassName("rs-cmp");
        sortBar.addClassName("tabs-bar");

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
                HashMap<String, Object> qparams = new HashMap<>();

                if (selectedTab.equals(newestTab.getLabel())) {
                    qparams.put("tab", newestTab.getLabel().toLowerCase());
                    UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.qparamsBuild(qparams)));
                }
            }
        });

        searchPostFieldIcon.addClickListener(event -> searchFieldProcess());

        searchPostField.addKeyPressListener(Key.ENTER, keyEventListener -> searchFieldProcess());
    }

    private void searchFieldProcess() {
        sortBar.setSelectedTab(null);

        HashMap<String, Object> customQueryParams = new HashMap<>();
        customQueryParams.put("search", searchPostField.getValue().strip());
        UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.qparamsBuild(customQueryParams)));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        currentUser = SecurityUtils.getPrincipal();
        setQueryParams(qparams);

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else if (qparams.containsKey("tag")) {
                sortBar.setSelectedTab(null);
            } else {
                sortBar.setSelectedTab(newestTab);
                pageParametersMap.replace("tab", newestTab.getLabel().toLowerCase());
            }
        }

        postsBrowserBuild(
                Integer.parseInt(pageParametersMap.get("page").toString()),
                pageParametersMap.get("tab").toString(),
                event.getLocation().getPath(),
                pageParametersMap.get("search").toString(),
                pageParametersMap.get("tag").toString()
        );
    }

    private void setQueryParams(Map<String, List<String>> qparams) {
        for (String parameter : pageParametersKeySet) {
            if (!parameter.equals("page")) {
                pageParametersMap.replace(parameter, "");
            } else
                pageParametersMap.replace(parameter, "1");
        }

        for (String parameter : pageParametersKeySet) {
            if (qparams.containsKey(parameter)) {
                pageParametersMap.replace(parameter, qparams.get(parameter).get(0));
            }
        }
    }

    private void postsBrowserBuild(int page, String sortTab, String locationPath, String search, String tag) {
        int pageLimit;
        List<Post> posts;
        float count;
        HashMap<String, Object> customQueryParams = new HashMap<>();

        customQueryParams.put("page", page);

        if (!search.isEmpty()) {
            posts = postService.findPostsByTitle(page, POST_ON_PAGE_LIMIT, search);
            count = postService.countPostsByTitle(search);
            customQueryParams.put("search", search);
        } else if (!tag.isEmpty()) {
            posts = postService.findPostsByTag(page, POST_ON_PAGE_LIMIT, tag);
            count = postService.countPostsByTag(tag);
            customQueryParams.put("tag", tag);
        } else {
            posts = postService.sortNewestPosts(page, POST_ON_PAGE_LIMIT);
            count = postService.count();
            customQueryParams.put("tab", sortTab);
        }

        bodyLayout.removeAll();

        if (!posts.isEmpty()) {
            for (Post post : posts) {
                PostComponent postComponent = new PostComponent(post);
                bodyLayout.add(postComponent);
            }

            pageLimit = FilterDataManager.pageLimit(count, POST_ON_PAGE_LIMIT);

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, QueryParametersManager.qparamsBuild(customQueryParams)));
        } else {
            bodyLayout.add(notFoundedH2);
        }
    }
}
