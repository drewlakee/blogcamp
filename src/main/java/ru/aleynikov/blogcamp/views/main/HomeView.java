package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.aleynikov.blogcamp.component.UserComponent;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.Date;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@PageTitle("Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class HomeView extends Composite<Div> implements HasComponents {

    public HomeView() {

    }
}
