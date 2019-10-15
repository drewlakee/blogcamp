package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.aleynikov.blogcamp.model.Comment;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.Locale;

@StyleSheet(StaticResources.COMMENT_STYLES)
@StyleSheet(StaticResources.MAIN_STYLES)
public class CommentComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout leftSideLayout = new VerticalLayout();
    private VerticalLayout rightSideLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout contentHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout headerLayout = new HorizontalLayout();

    private Image avatarImage = new Image();

    private Span usernameSpan = new Span();
    private Span createdDateSpan = new Span();
    private Span dotSpan = new Span("•");

    private Paragraph commentTextParagraph = new Paragraph();

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy HH:mm:ss", Locale.ENGLISH);

    private UserDetailDialog userDetailDialog;

    public CommentComponent(Comment comment, UserService userService) {
        contentLayout.addClassName("padding-none");

        contentHorizontalLayout.setSizeFull();

        leftSideLayout.setWidth(null);
        leftSideLayout.addClassName("padding-b-none");

        avatarImage.addClassName("user-comment-avatar");
        avatarImage.setAlt("avatar");
        avatarImage.setSrc(comment.getUser().getAvatar());

        leftSideLayout.add(avatarImage);

        rightSideLayout.addClassName("margin-none");
        rightSideLayout.addClassName("margin-l-none");
        rightSideLayout.addClassName("padding-b-none");
        rightSideLayout.setWidth(null);

        usernameSpan.addClassName("link");
        usernameSpan.setText(comment.getUser().getUsername());

        dotSpan.addClassName("margin-l-5px");
        dotSpan.addClassName("grey-light");
        dotSpan.addClassName("fs-12px");
        headerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, dotSpan);

        createdDateSpan.addClassName("margin-l-5px");
        createdDateSpan.addClassName("grey-light");
        createdDateSpan.addClassName("fs-12px");
        createdDateSpan.setText(createdDateFormat.format(comment.getCreatedDate()));
        headerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, createdDateSpan);

        headerLayout.add(usernameSpan, dotSpan, createdDateSpan);

        bodyLayout.addClassName("padding-none");
        bodyLayout.addClassName("comment-body");
        bodyLayout.setWidth(null);

        commentTextParagraph.setText(comment.getText());

        bodyLayout.add(commentTextParagraph);

        rightSideLayout.add(headerLayout, bodyLayout);

        contentHorizontalLayout.add(leftSideLayout, rightSideLayout);

        contentLayout.add(contentHorizontalLayout);

        add(contentLayout);

        userDetailDialog = new UserDetailDialog(comment.getUser(), userService);

        usernameSpan.addClickListener(event -> {
            userDetailDialog.open();
        });
    }
}
