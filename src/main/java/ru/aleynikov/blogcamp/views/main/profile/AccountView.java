package ru.aleynikov.blogcamp.views.main.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.views.main.ProfileView;

@Route(value = "account", layout = ProfileView.class)
@PageTitle("Profile - Account")
public class AccountView extends Composite<Div> implements HasComponents {

    public AccountView() {
        add(new H2("Account"));
    }
}
