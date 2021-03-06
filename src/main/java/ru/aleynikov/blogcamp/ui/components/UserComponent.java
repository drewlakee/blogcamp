package ru.aleynikov.blogcamp.ui.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.aleynikov.blogcamp.domain.models.City;
import ru.aleynikov.blogcamp.domain.models.Country;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

@StyleSheet(StaticContent.USER_STYLES)
@StyleSheet(StaticContent.MAIN_STYLES)
public class UserComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout contentBodyLeftLayout = new VerticalLayout();
    private VerticalLayout contentBodyRightLayout = new VerticalLayout();
    private VerticalLayout contentFootLayout = new VerticalLayout();

    private HorizontalLayout contentBodyLayout = new HorizontalLayout();
    private HorizontalLayout usernameLayout = new HorizontalLayout();

    private Span userStatusSpan = new Span();
    private Span itsYouSpan = new Span("It's you");
    private Span userFullNameSpan = new Span();
    private Span userFromSpan = new Span();
    private Span usernameSpan = new Span();

    private Image userAvatar = new Image();

    private UserDetailDialog userDetailDialog;

    public UserComponent(User currentUser, UserService userService) {
        addClassName("user-block");

        contentLayout.addClassName("padding-none");

        contentBodyLayout.setSizeFull();

        contentBodyLeftLayout.setWidth(null);
        contentBodyLeftLayout.addClassName("padding-none");

        userAvatar.addClassName("user-img");
        userAvatar.setAlt("avatar");
        userAvatar.setSrc(currentUser.getAvatar());


        contentBodyLeftLayout.add(userAvatar);

        contentBodyRightLayout.addClassName("padding-none");

        usernameSpan.addClassName("link");
        usernameSpan.setText(currentUser.getUsername());

        usernameLayout.add(usernameSpan);

        if (SecurityUtils.getPrincipal().getUsername().equals(currentUser.getUsername())) {
            itsYouSpan.addClassName("you");
            usernameLayout.add(itsYouSpan);
        }

        contentBodyRightLayout.add(usernameLayout);

        if (currentUser.getFullName().isPresent()) {
            userFullNameSpan.addClassName("user-fullname");
            userFullNameSpan.addClassName("margin-none");
            userFullNameSpan.setText(currentUser.getFullName().get());
            contentBodyRightLayout.add(userFullNameSpan);
        }

        if (currentUser.getCity().isPresent() & currentUser.getCountry().isPresent()) {
            userFromSpan.addClassName("user-from");
            userFromSpan.addClassName("margin-none");
            userFromSpan.setText(currentUser.getCity().map(City::getName).get() + ", " + currentUser.getCountry().map(Country::getName).get());
            contentBodyRightLayout.add(userFromSpan);
        }

        contentBodyLayout.add(contentBodyLeftLayout, contentBodyRightLayout);

        contentLayout.add(contentBodyLayout);

        if (currentUser.getStatus().isPresent()) {
            contentFootLayout.setSizeFull();
            contentFootLayout.addClassName("padding-none");
            contentFootLayout.addClassName("user-about");

            userStatusSpan.addClassName("user-about-content");
            userStatusSpan.setText(currentUser.getStatus().get());

            contentFootLayout.add(userStatusSpan);

            contentLayout.add(contentFootLayout);
        }

        add(contentLayout);

        userDetailDialog = new UserDetailDialog(currentUser, userService);

        addClickListener(event -> {
            userDetailDialog.open();
        });
    }
}
