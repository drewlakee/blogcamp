package ru.aleynikov.blogcamp.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.models.Post;
import ru.aleynikov.blogcamp.models.Tag;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.PostView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@StyleSheet(StaticResources.POST_STYLES)
@StyleSheet(StaticResources.MAIN_STYLES)
public class PostComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout middleLayout = new VerticalLayout();

    private HorizontalLayout upperLayout = new HorizontalLayout();
    private HorizontalLayout lowerLayout = new HorizontalLayout();

    private Span usernameSpan = new Span();
    private Span dotFirstSpan = new Span("•");
    private Span dotSecondSpan = new Span("•");
    private Span createdDateSpan = new Span();
    private Span commentsCountSpan = new Span();

    private Icon commentIcon = new Icon(VaadinIcon.COMMENT);

    private H2 postTitleH2 = new H2();

    private Image postImage = new Image();

    private RouterLink readLink;

    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private UserDetailDialog userDetailDialog;

    public PostComponent(Post post, UserService userService) {
        addClassName("post-block");

        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        upperLayout.setWidth("100%");
        upperLayout.addClassName("padding-10px");

        usernameSpan.addClassName("post-username");
        usernameSpan.addClassName("link");
        usernameSpan.setText(post.getUser().getUsername());

        dotFirstSpan.addClassName("grey-light");
        dotFirstSpan.addClassName("margin-l-5px");
        dotFirstSpan.addClassName("fs-12px");
        upperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, dotFirstSpan);

        createdDateSpan.addClassName("grey-light");
        createdDateSpan.addClassName("margin-l-5px");
        createdDateSpan.addClassName("fs-12px");
        createdDateSpan.setText(detailCreatedDateFormat.format(post.getCreatedDate()));
        upperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, createdDateSpan);

        upperLayout.add(usernameSpan, dotFirstSpan, createdDateSpan);

        dotSecondSpan.addClassName("grey-light");
        dotSecondSpan.addClassName("fs-12px");
        dotSecondSpan.addClassName("margin-l-5px");
        upperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, dotSecondSpan);

        upperLayout.add(dotSecondSpan);
        addTagsToPost(post.getTags());

        middleLayout.setSizeFull();
        middleLayout.addClassName("padding-none");

        postTitleH2.addClassName("margin-none");
        postTitleH2.addClassName("padding-l-10px");
        postTitleH2.addClassName("post-title-link");
        postTitleH2.setText(post.getTitle());

        middleLayout.add(postTitleH2);

        if (post.getIntroImage() != null) {
            postImage.addClassName("post-image");
            postImage.setAlt("post image");
            postImage.setSrc(post.getIntroImage());

            middleLayout.add(postImage);
        }

        lowerLayout.setWidth("100%");
        lowerLayout.addClassName("padding-10px");

        readLink = new RouterLink("READ", PostView.class, post.getId());
        readLink.addClassName("link");
        readLink.addClassName("fw-600");

        commentsCountSpan.addClassName("rs-cmp");
        commentsCountSpan.addClassName("comments-link");
        commentsCountSpan.setText(String.valueOf(post.getCommentCount()));
        commentIcon.addClassName("margin-l-5px");
        commentsCountSpan.add(commentIcon);

        lowerLayout.add(readLink, commentsCountSpan);

        contentLayout.add(upperLayout, middleLayout, lowerLayout);

        add(contentLayout);

        userDetailDialog = new UserDetailDialog(post.getUser(), userService);

        usernameSpan.addClickListener(event -> {
            userDetailDialog.open();
        });

        postTitleH2.addClickListener(event -> UI.getCurrent().navigate(PostView.class, post.getId()));
        commentsCountSpan.addClickListener(event -> UI.getCurrent().navigate(PostView.class, post.getId()));
    }

    private void addTagsToPost(List<Tag> tags) {
        for (Tag tag : tags) {
            TagComponent tagComponent = new TagComponent(tag);
            tagComponent.addClassName("margin-l-5px");
            upperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, tagComponent);
            upperLayout.add(tagComponent);
        }
    }
}
