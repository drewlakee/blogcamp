package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
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
public class HomeView extends Composite<VerticalLayout> implements HasComponents {

    public HomeView() {
        Date date = new Date();
        date.setTime(2123124);

        User user = new User(1,"Username1", "1111", "sadsdasd", "1", true, date, "I'm like playing in video games, also like programming on Java.");

        add(new UserComponent(user));
    }
}
