package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.staticResources.StaticResources;



@PageTitle("Blogcamp.")
@Route("")
@StyleSheet(StaticResources.MAIN_VIEW_STYLES)
public class MainView extends VerticalLayout {
    private HeaderComponent headerComponent = new HeaderComponent();

    private HorizontalLayout containerLayout =  new HorizontalLayout();

    private VerticalLayout leftSideBarLayout = new VerticalLayout();

    private Tabs navigationBar = new Tabs();
    private Tab homeTab = new Tab("Home");
    private Tab publicTabTitle = new Tab("Public");
    private Tab globeTab = new Tab("Blogcamp.");
    private Tab tagsTab = new Tab("Tags");
    private Tab usersTab = new Tab("Users");

    public MainView() {
        setSizeFull();
        setClassName("main-body");

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

        containerLayout.add(leftSideBarLayout);

        add(headerComponent, containerLayout);
    }
}
