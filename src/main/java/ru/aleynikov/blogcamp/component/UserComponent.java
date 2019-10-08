package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@StyleSheet(StaticResources.USER_COMPONENT_STYLES)
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class UserComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout contentBodyLeftLayout = new VerticalLayout();
    private VerticalLayout contentBodyRightLayout = new VerticalLayout();
    private VerticalLayout contentFootLayout = new VerticalLayout();
    private VerticalLayout detailUserLayout = new VerticalLayout();
    private VerticalLayout avatarLayout = new VerticalLayout();
    private VerticalLayout detailUserInfoLayout = new VerticalLayout();

    private int currYear = Calendar.getInstance().get(Calendar.YEAR);

    private HorizontalLayout contentBodyLayout = new HorizontalLayout();
    private HorizontalLayout usernameLayout = new HorizontalLayout();
    private HorizontalLayout detailUserHorizontalLayout = new HorizontalLayout();

    private Dialog detailUserInfoDialog = new Dialog();

    private RouterLink userLink = new RouterLink();

    private Span userStatus = new Span();
    private Span itsYouSpan = new Span("It's you");
    private Span userFullName = new Span();
    private Span userFromSpan = new Span();
    private Span detailUserUsername = new Span();
    private Span detailUserFullNameSpan = new Span();
    private Span detailUserFromSpan = new Span();
    private Span detailUserBirthdaySpan = new Span();
    private Span detailUserStatusSpan = new Span();

    private Image userAvatar = new Image();
    private Image userDetailAvatar = new Image();

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

        // detail pop-up info

        detailUserLayout.setWidth("100%");

        detailUserHorizontalLayout.setSizeFull();

        avatarLayout.addClassName("detail-avatar-block");
        avatarLayout.addClassName("margin-none");
        avatarLayout.addClassName("padding-none");
        avatarLayout.setWidth(null);

        userDetailAvatar.addClassName("detail-avatar-img");
        userDetailAvatar.setAlt("user avatar");
        userDetailAvatar.setSrc(currentUser.getAvatar());

        avatarLayout.add(userDetailAvatar);

        detailUserInfoLayout.setSizeFull();
        detailUserInfoLayout.addClassName("margin-none");

        detailUserUsername.addClassName("username");
        detailUserUsername.setText(currentUser.getUsername());
        detailUserInfoLayout.add(detailUserUsername);

        if (currentUser.getFullName() != null) {
            detailUserFullNameSpan.addClassName("grey-light");
            detailUserFullNameSpan.addClassName("margin-none");
            detailUserFullNameSpan.addClassName("padding-l-2px");
            detailUserFullNameSpan.setText(currentUser.getFullName());
            detailUserInfoLayout.add(detailUserFullNameSpan);
        }

        if (currentUser.getCity() != null & currentUser.getCountry() != null) {
            detailUserFromSpan.addClassName("grey-light");
            detailUserFromSpan.addClassName("margin-none");
            detailUserFromSpan.addClassName("padding-l-2px");
            detailUserFromSpan.setText(currentUser.getCity() + ", " + currentUser.getCountry());
            detailUserInfoLayout.add(detailUserFromSpan);
        }

        if (currentUser.getBirthday() != null) {
            detailUserBirthdaySpan.addClassName("grey-light");
            detailUserBirthdaySpan.addClassName("margin-none");
            detailUserBirthdaySpan.addClassName("padding-l-2px");
            detailUserBirthdaySpan.setText(currentUser.getBirthday().toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM YYYY", Locale.ENGLISH)) + " (" + (currYear - currentUser.getBirthday().toLocalDate().getYear()) + " years old)");
            detailUserInfoLayout.add(detailUserBirthdaySpan);
        }

        if (currentUser.getStatus() != null) {
            detailUserStatusSpan.setText(currentUser.getStatus());
            detailUserInfoLayout.add(detailUserStatusSpan);
        }

        detailUserHorizontalLayout.add(avatarLayout, detailUserInfoLayout);

        detailUserLayout.add(detailUserHorizontalLayout);

        detailUserInfoDialog.add(detailUserLayout);

        addClickListener(event -> {
           detailUserInfoDialog.open();
        });
    }
}
