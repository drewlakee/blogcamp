package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.staticResources.StaticResources;



@PageTitle("Blogcamp.")
@Route("")
@StyleSheet(StaticResources.MAIN_VIEW_STYLES)
public class MainView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(MainView.class);

    private HeaderComponent headerComponent = new HeaderComponent();

    private HorizontalLayout containerLayout =  new HorizontalLayout();

    private VerticalLayout leftSideBarLayout = new VerticalLayout();
    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout contentHeaderLayout = new VerticalLayout();
    private VerticalLayout contentBodyLayout = new VerticalLayout();

    private Label contentLabel = new Label();
    private static final String HOME = "Your own Blogposts";
    private static final String BLOGCAMPDOT = "All Blogposts";
    private static final String TAGS = "All Tags";
    private static final String USERS = "All Users";

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

        contentLabel.addClassName("content-label");
        contentLabel.setText(HOME);

        leftSideBarLayout.add(navigationBar);

        contentLayout.addClassName("content");
        contentLayout.setSizeFull();

        contentHeaderLayout.addClassName("content-header");
        contentHeaderLayout.add(contentLabel);

        contentBodyLayout.addClassName("content-body");

        contentLayout.add(contentHeaderLayout, contentBodyLayout);





        containerLayout.add(leftSideBarLayout, contentLayout);

        add(headerComponent, containerLayout);

        navigationBar.addSelectedChangeListener(event -> {
            String selectedTab = event.getSource().getSelectedTab().getLabel();

            if (selectedTab.equals("Home")) {
                contentLabel.setText(HOME);
            } else if (selectedTab.equals("Blogcamp.")) {
                contentLabel.setText(BLOGCAMPDOT);
            } else if (selectedTab.equals("Tags")) {
                contentLabel.setText(TAGS);
            } else if (selectedTab.equals("Users")) {
                contentLabel.setText(USERS);
            }
        });

    }
}
