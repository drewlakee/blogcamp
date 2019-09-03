package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@Route(value = "global", layout = MainLayout.class)
@PageTitle("Global - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class GlobalView extends Composite<VerticalLayout> implements HasComponents {

    public GlobalView() {
        add(new H2("Hello Feed!"));
    }
}
