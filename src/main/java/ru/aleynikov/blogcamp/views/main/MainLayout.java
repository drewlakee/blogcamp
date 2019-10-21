package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouterLayout;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@StyleSheet(StaticResources.MAIN_STYLES)
public class MainLayout extends Composite<VerticalLayout> implements HasComponents, RouterLayout, BeforeEnterObserver {

    private Div contentDiv = new Div();

    private HeaderComponent headerComponent = new HeaderComponent();

    private HorizontalLayout containerLayout =  new HorizontalLayout();

    private VerticalLayout leftSideBarLayout = new VerticalLayout();

    private Tabs navigationBar = new Tabs();
    private Tab publicTabTitle = new Tab("Public");
    private Tab postsTab = new Tab("Blogcamp");
    private Tab tagsTab = new Tab("Tags");
    private Tab usersTab = new Tab("Users");

    private QueryParameters qparams;

    private boolean isGlobalPostView;

    public MainLayout() {
        getContent().setSizeFull();
        getContent().setClassName("main-body");

        containerLayout.setSizeFull();
        containerLayout.addClassName("container");

        leftSideBarLayout.setSizeFull();
        leftSideBarLayout.addClassName("left-sidebar");

        publicTabTitle.setEnabled(false);
        publicTabTitle.addClassName("title-tab");

        postsTab.addClassName("navigation-tab");
        postsTab.addClassName("child-tab");
        postsTab.addComponentAsFirst(new Icon(VaadinIcon.GLOBE));

        tagsTab.addClassName("navigation-tab");
        tagsTab.addClassName("child-tab");
        tagsTab.addClassName("under-icon-tab");

        usersTab.addClassName("navigation-tab");
        usersTab.addClassName("child-tab");
        usersTab.addClassName("under-icon-tab");

        navigationBar.add(publicTabTitle, postsTab, tagsTab, usersTab);
        navigationBar.addClassName("navigation-bar");
        navigationBar.setOrientation(Tabs.Orientation.VERTICAL);

        leftSideBarLayout.add(navigationBar);

        contentDiv.addClassName("content");

        containerLayout.add(leftSideBarLayout, contentDiv);

        add(headerComponent, containerLayout);

        navigationBar.addSelectedChangeListener(event -> {
            String selectedTab = event.getSource().getSelectedTab().getLabel();

            if (selectedTab.equals(postsTab.getLabel())) {
                if (!isGlobalPostView)
                    UI.getCurrent().navigate("globe", qparams);
            } else if (selectedTab.equals(tagsTab.getLabel()) ) {
                UI.getCurrent().navigate("tags", qparams);
            } else if (selectedTab.equals(usersTab.getLabel())) {
                UI.getCurrent().navigate("users", qparams);
            }
        });
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentDiv.getElement().appendChild(content.getElement());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        isGlobalPostView = false;
        qparams = event.getLocation().getQueryParameters();
        Map<String, List<String>> emptyQParams = new HashMap<>();

       if (event.getNavigationTarget().getSimpleName().equals(GlobeView.class.getSimpleName())) {
            navigationBar.setSelectedTab(postsTab);
            leftSideBarLayout.setVisible(true);
        } else if (event.getNavigationTarget().getSimpleName().equals(TagsView.class.getSimpleName())) {
            navigationBar.setSelectedTab(tagsTab);
        } else if (event.getNavigationTarget().getSimpleName().equals(UsersView.class.getSimpleName())) {
            navigationBar.setSelectedTab(usersTab);
        } else if (event.getLocation().getPath().startsWith("profile")) {
            leftSideBarLayout.setVisible(false);
        } else if (event.getLocation().getPath().startsWith("globe/post")) {
            leftSideBarLayout.setVisible(true);
            isGlobalPostView = true;
        } else
            leftSideBarLayout.setVisible(true);

        qparams = new QueryParameters(emptyQParams);
    }
}
