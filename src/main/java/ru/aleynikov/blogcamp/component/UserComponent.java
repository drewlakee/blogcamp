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
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class UserComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout contentBodyLeftLayout = new VerticalLayout();
    private VerticalLayout contentBodyRightLayout = new VerticalLayout();
    private VerticalLayout contentFootLayout = new VerticalLayout();

    private HorizontalLayout contentBodyLayout = new HorizontalLayout();
    private HorizontalLayout usernameLayout = new HorizontalLayout();

    private RouterLink userLink = new RouterLink();

    private Span userStatus = new Span();
    private Span itsYouSpan = new Span("It's you");
    private Span userFullName = new Span();
    private Span userFromSpan = new Span();

    private Image userAvatar = new Image();

    private UserDetailDialog userDetailDialog;

    public UserComponent(User currentUser) {
        addClassName("user-block");

        contentLayout.addClassName("padding-none");

        contentBodyLayout.setSizeFull();

        contentBodyLeftLayout.setWidth(null);
        contentBodyLeftLayout.addClassName("padding-none");

        userAvatar.addClassName("user-img");
        userAvatar.setAlt("avatar");
        if (currentUser.getAvatar() != null)
            userAvatar.setSrc(currentUser.getAvatar());
        else {
            userAvatar.addClassName("padding-trl-1px");
            userAvatar.setSrc(StaticResources.DEFAULT_USER_AVATAR);
        }

        contentBodyLeftLayout.add(userAvatar);

        contentBodyRightLayout.addClassName("padding-none");

        userLink.addClassName("user-link");
        userLink.setText(currentUser.getUsername());

        usernameLayout.add(userLink);

        if (SecurityUtils.getPrincipal().getUsername().equals(currentUser.getUsername())) {
            itsYouSpan.addClassName("you");
            usernameLayout.add(itsYouSpan);
        }

        contentBodyRightLayout.add(usernameLayout);

        if (currentUser.getFullName() != null) {
            userFullName.addClassName("user-fullname");
            userFullName.addClassName("margin-none");
            userFullName.setText(currentUser.getFullName());
            contentBodyRightLayout.add(userFullName);
        }

        if (currentUser.getCity() != null & currentUser.getCountry() != null) {
            userFromSpan.addClassName("user-from");
            userFromSpan.addClassName("margin-none");
            userFromSpan.setText(currentUser.getCity() + ", " + currentUser.getCountry());
            contentBodyRightLayout.add(userFromSpan);
        }

        contentBodyLayout.add(contentBodyLeftLayout, contentBodyRightLayout);

        contentLayout.add(contentBodyLayout);

        if (currentUser.getStatus() != null) {
            contentFootLayout.setSizeFull();
            contentFootLayout.addClassName("padding-none");
            contentFootLayout.addClassName("user-about");

            userStatus.addClassName("user-about-content");
            userStatus.setText(currentUser.getStatus());

            contentFootLayout.add(userStatus);

            contentLayout.add(contentFootLayout);
        }

        add(contentLayout);

        userDetailDialog = new UserDetailDialog(currentUser);

        addClickListener(event -> {
            userDetailDialog.open();
        });
    }
}
