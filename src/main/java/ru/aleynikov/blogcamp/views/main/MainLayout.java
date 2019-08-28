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
import com.vaadin.flow.router.RouterLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.staticResources.StaticResources;



@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class MainLayout extends Composite<VerticalLayout> implements HasComponents, RouterLayout, BeforeEnterObserver {

    private static final Logger log = LoggerFactory.getLogger(MainLayout.class);

    private Div contentDiv = new Div();

    private HeaderComponent headerComponent = new HeaderComponent();

    private HorizontalLayout containerLayout =  new HorizontalLayout();

    private VerticalLayout leftSideBarLayout = new VerticalLayout();

    private Tabs navigationBar = new Tabs();
    private Tab homeTab = new Tab("Home");
    private Tab publicTabTitle = new Tab("Public");
    private Tab globalTab = new Tab("Blogcamp.");
    private Tab tagsTab = new Tab("Tags");
    private Tab usersTab = new Tab("Users");

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

        globalTab.addClassName("navigation-tab");
        globalTab.addClassName("child-tab");
        globalTab.addComponentAsFirst(new Icon(VaadinIcon.GLOBE));

        tagsTab.addClassName("navigation-tab");
        tagsTab.addClassName("child-tab");
        tagsTab.addClassName("under-icon-tab");

        usersTab.addClassName("navigation-tab");
        usersTab.addClassName("child-tab");
        usersTab.addClassName("under-icon-tab");

        navigationBar.add(homeTab, publicTabTitle, globalTab, tagsTab, usersTab);
        navigationBar.addClassName("navigation-bar");
        navigationBar.setOrientation(Tabs.Orientation.VERTICAL);

        leftSideBarLayout.add(navigationBar);

        contentDiv.addClassName("content");

        containerLayout.add(leftSideBarLayout, contentDiv);

        add(headerComponent, containerLayout);

        navigationBar.addSelectedChangeListener(event -> {
            String selectedTab = event.getSource().getSelectedTab().getLabel();

            if (selectedTab.equals(homeTab.getLabel())) {
                UI.getCurrent().navigate("home");
            } else if (selectedTab.equals(globalTab.getLabel())) {
                UI.getCurrent().navigate("global");
            } else if (selectedTab.equals(tagsTab.getLabel())) {
                UI.getCurrent().navigate("tags");
            } else if (selectedTab.equals(usersTab.getLabel())) {
                UI.getCurrent().navigate("users");
            }
        });
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentDiv.getElement().appendChild(content.getElement());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEvent) {
        if (!navigationBar.getSelectedTab().toString().toLowerCase().contains(beforeEvent.getLocation().getPath())) {
            if (beforeEvent.getNavigationTarget().getSimpleName().equals(HomeView.class.getSimpleName())) {
                if (beforeEvent.getLocation().getPath().equals("")) {
                    UI.getCurrent().navigate("home");
                }
                navigationBar.setSelectedTab(homeTab);
            } else if (beforeEvent.getNavigationTarget().getSimpleName().equals(GlobalView.class.getSimpleName())) {
                navigationBar.setSelectedTab(globalTab);
            } else if (beforeEvent.getNavigationTarget().getSimpleName().equals(TagsView.class.getSimpleName())) {
                navigationBar.setSelectedTab(tagsTab);
            } else if (beforeEvent.getNavigationTarget().getSimpleName().equals(UsersView.class.getSimpleName())) {
                navigationBar.setSelectedTab(usersTab);
            }
        }
    }
}
