package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.time.format.DateTimeFormatter;
import java.util.*;

@StyleSheet(StaticResources.USER_COMPONENT_STYLES)
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
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

    private Image userAvatar = new Image();

    public UserDetailDialog(User user) {
        detailUserLayout.setWidth("100%");
        detailUserLayout.setHeight("286px");

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
        userAvatar.setSrc(user.getAvatar());

        avatarLayout.add(userAvatar);

        postsFindLinkSpan.addClassName("link");
        postsFindLinkSpan.addClassName("find-posts-link");

        leftSideLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, userAvatar);
        leftSideLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, postsFindLinkSpan);

        leftSideLayout.add(userAvatar, postsFindLinkSpan);

        rightSideLayout.setSizeFull();
        rightSideLayout.addClassName("margin-none");

        detailUserUsername.addClassName("username");
        detailUserUsername.setText(user.getUsername());
        rightSideLayout.add(detailUserUsername);

        if (user.getFullName() != null) {
            detailUserFullNameSpan.addClassName("grey-light");
            detailUserFullNameSpan.addClassName("margin-none");
            detailUserFullNameSpan.addClassName("padding-l-2px");
            detailUserFullNameSpan.setText(user.getFullName());
            rightSideLayout.add(detailUserFullNameSpan);
        }

        if (user.getCity() != null & user.getCountry() != null) {
            detailUserFromSpan.addClassName("grey-light");
            detailUserFromSpan.addClassName("margin-none");
            detailUserFromSpan.addClassName("padding-l-2px");
            detailUserFromSpan.setText(user.getCity() + ", " + user.getCountry());
            rightSideLayout.add(detailUserFromSpan);
        }

        if (user.getBirthday() != null) {
            detailUserBirthdaySpan.addClassName("grey-light");
            detailUserBirthdaySpan.addClassName("margin-none");
            detailUserBirthdaySpan.addClassName("padding-l-2px");
            detailUserBirthdaySpan.setText(user.getBirthday().toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM YYYY", Locale.ENGLISH)) + " (" + (currYear - user.getBirthday().toLocalDate().getYear()) + " years old)");
            rightSideLayout.add(detailUserBirthdaySpan);
        }

        if (user.getStatus() != null) {
            detailUserStatusSpan.setText(user.getStatus());
            rightSideLayout.add(detailUserStatusSpan);
        }

        detailUserHorizontalLayout.add(leftSideLayout, rightSideLayout);

        detailUserLayout.add(detailUserHorizontalLayout);

        add(detailUserLayout);

        postsFindLinkSpan.addClickListener(event -> {
            HashMap<String, Object> qparams = new HashMap<>();
            qparams.put("user", user.getUsername());
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.buildQueryParams(qparams)));
            close();
        });
    }
}
