package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@StyleSheet(StaticResources.USER_COMPONENT_STYLES)
public class UserComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout contentBodyLeftLayout = new VerticalLayout();
    private VerticalLayout contentBodyRightLayout = new VerticalLayout();
    private VerticalLayout contentFootLayout = new VerticalLayout();

    private HorizontalLayout contentBodyLayout = new HorizontalLayout();
    private HorizontalLayout usernameLayout = new HorizontalLayout();


    private RouterLink userLink = new RouterLink();

    private Span userAbout = new Span();
    private Span itsYouSpan = new Span("It's you");

    private Image userAvatar = new Image();

    public UserComponent(User user) {
        addClassName("user-block");

        contentLayout.addClassName("padding-none");

        contentBodyLayout.setSizeFull();

        contentBodyLeftLayout.setWidth(null);
        contentBodyLeftLayout.addClassName("padding-none");

        userAvatar.addClassName("user-img");
        userAvatar.setAlt("avatar");
        userAvatar.setSrc(StaticResources.DEFAULT_USER_AVATAR);

        contentBodyLeftLayout.add(userAvatar);

        contentBodyRightLayout.addClassName("padding-none");

        userLink.addClassName("user-link");
        userLink.setText(user.getUsername());

        usernameLayout.add(userLink);

        if (SecurityUtils.getUsername().equals(user.getUsername())) {
            itsYouSpan.addClassName("you");
            usernameLayout.add(itsYouSpan);
        }

        contentBodyRightLayout.add(usernameLayout);

        contentBodyLayout.add(contentBodyLeftLayout, contentBodyRightLayout);

        contentLayout.add(contentBodyLayout);

        if (user.getAbout() != null) {
            contentFootLayout.setSizeFull();
            contentFootLayout.addClassName("padding-none");
            contentFootLayout.addClassName("user-about");

            userAbout.addClassName("user-about-content");
            userAbout.setText(user.getAbout());

            contentFootLayout.add(userAbout);

            contentLayout.add(contentFootLayout);
        }

        add(contentLayout);
    }
}
