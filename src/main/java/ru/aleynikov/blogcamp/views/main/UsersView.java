package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users - Blogcamp.")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class UsersView extends Composite<VerticalLayout> implements HasComponents {

    public UsersView() {
        add("users");
    }
}
