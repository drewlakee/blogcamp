package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.PostView;

import java.text.SimpleDateFormat;
import java.util.Locale;

@StyleSheet(StaticResources.POST_STYLES)
@StyleSheet(StaticResources.MAIN_STYLES)
public class PostCutComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();

    private HorizontalLayout contentHorizontalLayout = new HorizontalLayout();

    private HorizontalLayout contentHorLeftSideLayout = new HorizontalLayout();
    private HorizontalLayout contentHorRightSideLayout = new HorizontalLayout();

    private H3 titleH2 = new H3();

    private Span commentsCount = new Span();
    private Span readLinkSpan = new Span("READ");
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
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, titleH2);

        createdDateSpan.addClassName("rs-cmp");
        createdDateSpan.addClassName("margin-l-5px");
        createdDateSpan.addClassName("grey-light");
        createdDateSpan.addClassName("fs-12px");
        createdDateSpan.setText(createdDateFormat.format(post.getCreatedDate()));
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, createdDateSpan);

        readLinkSpan.addClassName("link");
        readLinkSpan.addClassName("fw-600");
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, readLinkSpan);

        editLinkSpan.addClassName("warning");
        editLinkSpan.addClassName("fw-600");
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, editLinkSpan);

        deleteSpan.addClassName("attention");
        deleteSpan.addClassName("fw-600");
        contentHorLeftSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, deleteSpan);

        contentHorLeftSideLayout.setWidth("100%");
        contentHorLeftSideLayout.add(titleH2, createdDateSpan, readLinkSpan, editLinkSpan, deleteSpan);

        commentsCount.setText(String.valueOf(post.getCommentCount()));
        commentsCount.addClassName("rs-cmp");
        contentHorRightSideLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, commentsCount);

        commentIcon.addClassName("margin-l-5px");
        commentsCount.add(commentIcon);

        contentHorRightSideLayout.setWidth("20%");
        contentHorRightSideLayout.add(commentsCount);

        contentHorizontalLayout.add(contentHorLeftSideLayout, contentHorRightSideLayout);

        contentLayout.add(contentHorizontalLayout);

        add(contentLayout);

        readLinkSpan.addClickListener(event -> UI.getCurrent().navigate(PostView.class, post.getId()));

        deleteSpan.addClickListener(event -> {
            postService.deleteById(post.getId());

            this.setVisible(false);
            Notification.show("Post was deleted.");
        });
    }
}
