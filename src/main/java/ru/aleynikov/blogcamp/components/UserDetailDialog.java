package ru.aleynikov.blogcamp.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import ru.aleynikov.blogcamp.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.services.JavaScriptUtils;
import ru.aleynikov.blogcamp.services.QueryParametersManager;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


@StyleSheet(StaticResources.USER_STYLES)
@StyleSheet(StaticResources.MAIN_STYLES)
public class UserDetailDialog extends Dialog {

    private VerticalLayout detailUserLayout = new VerticalLayout();
    private VerticalLayout leftSideLayout = new VerticalLayout();
    private VerticalLayout avatarLayout = new VerticalLayout();
    private VerticalLayout rightSideLayout = new VerticalLayout();

    private int currYear = Calendar.getInstance().get(Calendar.YEAR);

    private HorizontalLayout detailUserHorizontalLayout = new HorizontalLayout();

    private Span detailUserUsername = new Span();
    private Span detailUserFullNameSpan = new Span();
    private Span detailUserFromSpan = new Span();
    private Span detailUserBirthdaySpan = new Span();
    private Span detailUserStatusSpan = new Span();
    private Span postsFindLinkSpan = new Span("Find posts");
    private Span banUserSpan = new Span("Ban user");
    private Span unBunUserSpan = new Span("Unban user");

    private Image userAvatar = new Image();

    private User userInSession = SecurityUtils.getPrincipal();

    public UserDetailDialog(User postUser, UserService userService) {
        detailUserLayout.setWidth("100%");

        detailUserHorizontalLayout.setSizeFull();

        leftSideLayout.setWidth("100%");
        leftSideLayout.addClassName("padding-none");
        leftSideLayout.addClassName("margin-none");

        avatarLayout.addClassName("detail-avatar-block");
        avatarLayout.addClassName("margin-none");
        avatarLayout.addClassName("padding-none");
        avatarLayout.setWidth(null);

        userAvatar.addClassName("detail-avatar-img");
        userAvatar.setAlt("user avatar");
        userAvatar.setSrc(postUser.getAvatar());

        avatarLayout.add(userAvatar);

        postsFindLinkSpan.addClassName("link");
        postsFindLinkSpan.addClassName("find-posts-link");

        leftSideLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, userAvatar);
        leftSideLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, postsFindLinkSpan);
        leftSideLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, banUserSpan);
        leftSideLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, unBunUserSpan);

        banUserSpan.addClassName("fs-30px");
        banUserSpan.addClassName("attention");
        banUserSpan.addClassName("fw-600");
        banUserSpan.addClassName("margin-none");
        banUserSpan.setVisible(false);

        unBunUserSpan.addClassName("fs-30px");
        unBunUserSpan.addClassName("warning");
        unBunUserSpan.addClassName("fw-600");
        unBunUserSpan.addClassName("margin-none");
        unBunUserSpan.setVisible(false);

        if (!userInSession.getUsername().equals(postUser.getUsername())) {
            if (userInSession.isAdmin() && !postUser.isBanned() && !postUser.isAdmin()) {
                banUserSpan.setVisible(true);
            } else if (userInSession.isAdmin() && postUser.isBanned() && !postUser.isAdmin()) {
                unBunUserSpan.setVisible(true);
                postsFindLinkSpan.setVisible(false);
            }
        }

        leftSideLayout.add(userAvatar, postsFindLinkSpan, banUserSpan, unBunUserSpan);

        rightSideLayout.setSizeFull();
        rightSideLayout.addClassName("margin-none");

        detailUserUsername.addClassName("username");
        detailUserUsername.setText(postUser.getUsername());
        rightSideLayout.add(detailUserUsername);

        if (postUser.getFullName() != null) {
            detailUserFullNameSpan.addClassName("grey-light");
            detailUserFullNameSpan.addClassName("margin-none");
            detailUserFullNameSpan.addClassName("padding-l-2px");
            detailUserFullNameSpan.setText(postUser.getFullName());
            rightSideLayout.add(detailUserFullNameSpan);
        }

        if (postUser.getCity() != null & postUser.getCountry() != null) {
            detailUserFromSpan.addClassName("grey-light");
            detailUserFromSpan.addClassName("margin-none");
            detailUserFromSpan.addClassName("padding-l-2px");
            detailUserFromSpan.setText(postUser.getCity().getName() + ", " + postUser.getCountry().getName());
            rightSideLayout.add(detailUserFromSpan);
        }

        if (postUser.getBirthday() != null) {
            detailUserBirthdaySpan.addClassName("grey-light");
            detailUserBirthdaySpan.addClassName("margin-none");
            detailUserBirthdaySpan.addClassName("padding-l-2px");
            detailUserBirthdaySpan.setText(postUser.getBirthday().toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM YYYY", Locale.ENGLISH)) + " (" + (currYear - postUser.getBirthday().toLocalDate().getYear()) + " years old)");
            rightSideLayout.add(detailUserBirthdaySpan);
        }

        if (postUser.getStatus() != null) {
            detailUserStatusSpan.setText(postUser.getStatus());
            rightSideLayout.add(detailUserStatusSpan);
        }

        detailUserHorizontalLayout.add(leftSideLayout, rightSideLayout);

        detailUserLayout.add(detailUserHorizontalLayout);

        add(detailUserLayout);

        postsFindLinkSpan.addClickListener(event -> {
            HashMap<String, Object> qparams = new HashMap<>();
            qparams.put("user", postUser.getUsername());
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.buildQueryParams(qparams)));
            JavaScriptUtils.scrollPageTop();
            close();
        });

        banUserSpan.addClickListener(event -> {
            banUserSpan.setVisible(false);
            postsFindLinkSpan.setVisible(false);
            unBunUserSpan.setVisible(true);

            userService.banById(postUser.getId());
            Notification.show(postUser.getUsername() + " was banned.");
        });

        unBunUserSpan.addClickListener(event -> {
            unBunUserSpan.setVisible(false);
            banUserSpan.setVisible(true);
            postsFindLinkSpan.setVisible(true);

            userService.unbanById(postUser.getId());
            Notification.show(postUser.getUsername() + " was unbanned.");
        });
    }
}
