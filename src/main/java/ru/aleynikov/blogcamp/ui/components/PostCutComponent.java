package ru.aleynikov.blogcamp.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.aleynikov.blogcamp.domain.models.Post;
import ru.aleynikov.blogcamp.services.PostService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;
import ru.aleynikov.blogcamp.ui.views.main.PostView;

import java.text.SimpleDateFormat;
import java.util.Locale;

@StyleSheet(StaticContent.POST_STYLES)
@StyleSheet(StaticContent.MAIN_STYLES)
public class PostCutComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();

    private HorizontalLayout contentHorizontalLayout = new HorizontalLayout();

    private HorizontalLayout contentHorLeftSideLayout = new HorizontalLayout();
    private HorizontalLayout contentHorRightSideLayout = new HorizontalLayout();

    private H3 titleH2 = new H3();

    private Span commentsCount = new Span();
    private Span editLinkSpan = new Span("EDIT");
    private Span deleteSpan = new Span("DELETE");
    private Span createdDateSpan = new Span();

    private Icon commentIcon = new Icon(VaadinIcon.COMMENT);

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm:ss", Locale.ENGLISH);

    public PostCutComponent(Post post, PostService postService) {
        setWidth("100%");

        contentLayout.setWidth("100%");

        contentLayout.addClassName("post-cut-block");

        contentHorizontalLayout.setWidth("100%");

        titleH2.setText(post.getTitle());
        titleH2.addClassName("margin-t-none");
        titleH2.addClassName("margin-b-none");
        titleH2.addClassName("post-title-link");
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, titleH2);

        createdDateSpan.addClassName("rs-cmp");
        createdDateSpan.addClassName("margin-l-5px");
        createdDateSpan.addClassName("grey-light");
        createdDateSpan.addClassName("fs-12px");
        createdDateSpan.setText(createdDateFormat.format(post.getCreatedDate()));
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, createdDateSpan);

        editLinkSpan.addClassName("warning");
        editLinkSpan.addClassName("opacity-07");
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, editLinkSpan);

        deleteSpan.addClassName("attention");
        deleteSpan.addClassName("opacity-07");
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, deleteSpan);

        if (postService == null) {
            editLinkSpan.setVisible(false);
            deleteSpan.setVisible(false);
        }

        contentHorLeftSideLayout.setWidth("100%");
        contentHorLeftSideLayout.add(titleH2, createdDateSpan, editLinkSpan, deleteSpan);

        commentsCount.setText(String.valueOf(post.getCommentCount()));
        commentsCount.addClassName("rs-cmp");
        commentsCount.addClassName("comments-link");
        contentHorRightSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, commentsCount);

        commentIcon.addClassName("margin-l-5px");
        commentsCount.add(commentIcon);

        contentHorRightSideLayout.setWidth("20%");
        contentHorRightSideLayout.add(commentsCount);

        contentHorizontalLayout.add(contentHorLeftSideLayout, contentHorRightSideLayout);

        contentLayout.add(contentHorizontalLayout);

        add(contentLayout);

        titleH2.addClickListener(event -> UI.getCurrent().navigate(PostView.class, post.getId()));
        commentsCount.addClickListener(event -> UI.getCurrent().navigate(PostView.class, post.getId()));

        if (postService != null) {
            editLinkSpan.addClickListener(event -> UI.getCurrent().navigate("editpost/" + post.getId()));

            deleteSpan.addClickListener(event -> {
                postService.deleteById(post.getId());

                this.setVisible(false);
                Notification.show("Post was deleted.");
            });
        }
    }
}
