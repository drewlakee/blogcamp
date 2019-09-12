package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@StyleSheet(StaticResources.USER_COMPONENT_STYLES)
public class UserComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout contentBodyLeftLayout = new VerticalLayout();
    private VerticalLayout contentBodyRightLayout = new VerticalLayout();
    private VerticalLayout contentFoot = new VerticalLayout();

    private HorizontalLayout contentBody = new HorizontalLayout();


    private RouterLink userLink = new RouterLink();

    private Span aboutTitle = new Span("About");
    private Span userAbout = new Span();

    private Image userAvatar = new Image();

    public UserComponent(User user) {
        addClassName("user-block");

        contentLayout.addClassName("padding-none");

        contentBody.setSizeFull();

        contentBodyLeftLayout.setWidth(null);
        contentBodyLeftLayout.addClassName("padding-none");

        userAvatar.addClassName("user-img");
        userAvatar.setAlt("avatar");
        userAvatar.setSrc(StaticResources.DEFAULT_USER_AVATAR);

        contentBodyLeftLayout.add(userAvatar);

        contentBodyRightLayout.addClassName("padding-none");

        userLink.addClassName("user-link");
        userLink.setText(user.getUsername());

        contentBodyRightLayout.add(userLink);

        contentBody.add(contentBodyLeftLayout, contentBodyRightLayout);

        contentLayout.add(contentBody);

        if (user.getAbout() != null) {
            contentFoot.setSizeFull();
            contentFoot.addClassName("padding-none");
            contentFoot.addClassName("user-about");

            userAbout.addClassName("user-about-content");
            userAbout.setText(user.getAbout());

            contentFoot.add(aboutTitle, userAbout);

            contentLayout.add(contentFoot);
        }

        add(contentLayout);
    }
}
