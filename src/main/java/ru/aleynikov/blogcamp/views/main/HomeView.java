package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.aleynikov.blogcamp.component.PostComponent;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.sql.Date;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@PageTitle("Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class HomeView extends Composite<Div> implements HasComponents {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private Label homeLabel = new Label("Home");

    private Button addPostButton = new Button("Add post");

    public HomeView() {
        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();
        headerLayout.addClassName("content-header");

        headerUpLayout.setWidth("100%");

        homeLabel.addClassName("content-label");

        headerUpLayout.add(homeLabel);

        headerLowerLayout.setWidth("100%");

        addPostButton.addClassName("main-button");
        addPostButton.addClassName("rs-cmp");

        headerLowerLayout.add(addPostButton);

        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");


        // TEST
        Post postadd = new Post();

        User user = new User();
        user.setUsername("drewlakee");

        postadd.setUser(user);

        postadd.setCreatedDate(new Date(System.currentTimeMillis()));

        PostComponent post = new PostComponent(postadd);

        bodyLayout.add(post);
        // TEST

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        addPostButton.addClickListener(event -> {
            UI.getCurrent().navigate("posts/add");
        });
    }
}
