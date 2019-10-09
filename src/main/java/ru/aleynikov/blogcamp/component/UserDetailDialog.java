package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@StyleSheet(StaticResources.USER_COMPONENT_STYLES)
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class UserDetailDialog extends Dialog {

    private VerticalLayout detailUserLayout = new VerticalLayout();
    private VerticalLayout avatarLayout = new VerticalLayout();
    private VerticalLayout detailUserInfoLayout = new VerticalLayout();

    private int currYear = Calendar.getInstance().get(Calendar.YEAR);

    private HorizontalLayout detailUserHorizontalLayout = new HorizontalLayout();

    private Span detailUserUsername = new Span();
    private Span detailUserFullNameSpan = new Span();
    private Span detailUserFromSpan = new Span();
    private Span detailUserBirthdaySpan = new Span();
    private Span detailUserStatusSpan = new Span();

    private Image userDetailAvatar = new Image();

    public UserDetailDialog(User user) {
        detailUserLayout.setWidth("100%");

        detailUserHorizontalLayout.setSizeFull();

        avatarLayout.addClassName("detail-avatar-block");
        avatarLayout.addClassName("margin-none");
        avatarLayout.addClassName("padding-none");
        avatarLayout.setWidth(null);

        userDetailAvatar.addClassName("detail-avatar-img");
        userDetailAvatar.setAlt("user avatar");
        userDetailAvatar.setSrc(user.getAvatar());

        avatarLayout.add(userDetailAvatar);

        detailUserInfoLayout.setSizeFull();
        detailUserInfoLayout.addClassName("margin-none");

        detailUserUsername.addClassName("username");
        detailUserUsername.setText(user.getUsername());
        detailUserInfoLayout.add(detailUserUsername);

        if (user.getFullName() != null) {
            detailUserFullNameSpan.addClassName("grey-light");
            detailUserFullNameSpan.addClassName("margin-none");
            detailUserFullNameSpan.addClassName("padding-l-2px");
            detailUserFullNameSpan.setText(user.getFullName());
            detailUserInfoLayout.add(detailUserFullNameSpan);
        }

        if (user.getCity() != null & user.getCountry() != null) {
            detailUserFromSpan.addClassName("grey-light");
            detailUserFromSpan.addClassName("margin-none");
            detailUserFromSpan.addClassName("padding-l-2px");
            detailUserFromSpan.setText(user.getCity() + ", " + user.getCountry());
            detailUserInfoLayout.add(detailUserFromSpan);
        }

        if (user.getBirthday() != null) {
            detailUserBirthdaySpan.addClassName("grey-light");
            detailUserBirthdaySpan.addClassName("margin-none");
            detailUserBirthdaySpan.addClassName("padding-l-2px");
            detailUserBirthdaySpan.setText(user.getBirthday().toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM YYYY", Locale.ENGLISH)) + " (" + (currYear - user.getBirthday().toLocalDate().getYear()) + " years old)");
            detailUserInfoLayout.add(detailUserBirthdaySpan);
        }

        if (user.getStatus() != null) {
            detailUserStatusSpan.setText(user.getStatus());
            detailUserInfoLayout.add(detailUserStatusSpan);
        }

        detailUserHorizontalLayout.add(avatarLayout, detailUserInfoLayout);

        detailUserLayout.add(detailUserHorizontalLayout);

        add(detailUserLayout);
    }
}
