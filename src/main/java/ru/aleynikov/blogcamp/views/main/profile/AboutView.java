package ru.aleynikov.blogcamp.views.main.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.views.main.ProfileView;

@Route(value = "about", layout = ProfileView.class)
@PageTitle("Profile - About")
public class AboutView extends Composite<Div> implements HasComponents {

    public AboutView() {
        add(new H2("About"));
    }
}
