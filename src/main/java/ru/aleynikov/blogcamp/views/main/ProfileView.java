package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class ProfileView extends Composite<Div> implements HasComponents {

    public ProfileView() {
        add(new H2("Profile"));
    }
}
