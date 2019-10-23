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
    private Tab homeTab = new Tab("Home");
    private Tab publicTabTitle = new Tab("Public");
    private Tab globeTab = new Tab("Blogcamp");
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

        homeTab.addClassName("navigation-tab");
        homeTab.addClassName("home-tab");

        publicTabTitle.setEnabled(false);
        publicTabTitle.addClassName("title-tab");

        globeTab.addClassName("navigation-tab");
        globeTab.addClassName("child-tab");
        globeTab.addComponentAsFirst(new Icon(VaadinIcon.GLOBE));

        tagsTab.addClassName("navigation-tab");
        tagsTab.addClassName("child-tab");
        tagsTab.addClassName("under-icon-tab");

        usersTab.addClassName("navigation-tab");
        usersTab.addClassName("child-tab");
        usersTab.addClassName("under-icon-tab");

        navigationBar.add(homeTab, publicTabTitle, globeTab, tagsTab, usersTab);
        navigationBar.addClassName("navigation-bar");
        navigationBar.setOrientation(Tabs.Orientation.VERTICAL);
        navigationBar.setSelectedTab(homeTab);

        leftSideBarLayout.add(navigationBar);

        contentDiv.addClassName("content");

        containerLayout.add(leftSideBarLayout, contentDiv);

        add(headerComponent, containerLayout);

        navigationBar.addSelectedChangeListener(event -> {
            String selectedTab = "";
            if (event.getSource().getSelectedTab() != null)
                selectedTab = event.getSource().getSelectedTab().getLabel();

            if (selectedTab.equals(homeTab.getLabel())) {
                UI.getCurrent().navigate(HomeView.class);
            } else if (selectedTab.equals(globeTab.getLabel())) {
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

        if (event.getNavigationTarget().getSimpleName().equals(HomeView.class.getSimpleName())) {
//            navigationBar.setSelectedTab(homeTab);
            leftSideBarLayout.setVisible(true);
        } else if (event.getNavigationTarget().getSimpleName().equals(GlobeView.class.getSimpleName())) {
//            navigationBar.setSelectedTab(globeTab);
            leftSideBarLayout.setVisible(true);
        } else if (event.getNavigationTarget().getSimpleName().equals(TagsView.class.getSimpleName())) {
//            navigationBar.setSelectedTab(tagsTab);
        } else if (event.getNavigationTarget().getSimpleName().equals(UsersView.class.getSimpleName())) {
//            navigationBar.setSelectedTab(usersTab);
        } else if (event.getLocation().getPath().startsWith("profile")) {
            leftSideBarLayout.setVisible(false);
        } else if (event.getLocation().getPath().startsWith("globe/post")) {
//            navigationBar.setSelectedTab(globeTab);
            leftSideBarLayout.setVisible(true);
            isGlobalPostView = true;
        } else
            leftSideBarLayout.setVisible(true);

        navigationBar.setSelectedTab(null);

        qparams = new QueryParameters(emptyQParams);
    }
}
