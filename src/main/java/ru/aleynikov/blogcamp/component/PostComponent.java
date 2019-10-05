package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@StyleSheet(StaticResources.POST_STYLES)
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class PostComponent extends Div {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout middleLayout = new VerticalLayout();

    private HorizontalLayout upperLayout = new HorizontalLayout();
    private HorizontalLayout lowerLayout = new HorizontalLayout();

    private Span usernameSpan = new Span();
    private Span dotFirstSpan = new Span("•");
    private Span dotSecondSpan = new Span("•");
    private Span createdDateSpan = new Span();

    private H2 postTitleH2 = new H2();

    private Image postImage = new Image();

    private RouterLink readLink = new RouterLink();

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    public PostComponent(Post post) {
        addClassName("post-block");

        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        upperLayout.setWidth("100%");
        upperLayout.addClassName("padding-10px");

        usernameSpan.addClassName("post-username");
        usernameSpan.setText(post.getUser().getUsername());

        dotFirstSpan.addClassName("grey-light");
        dotFirstSpan.addClassName("margin-l-5px");

        createdDateSpan.addClassName("grey-light");
        createdDateSpan.addClassName("margin-l-5px");
        createdDateSpan.setText("Posted in " + createdDateFormat.format(post.getCreatedDate()));
        createdDateSpan.setTitle(detailCreatedDateFormat.format(post.getCreatedDate()));

        upperLayout.add(usernameSpan, dotFirstSpan, createdDateSpan);

        dotSecondSpan.addClassName("grey-light");
        dotSecondSpan.addClassName("margin-l-5px");

        upperLayout.add(dotSecondSpan);
        addTagsToPost(post.getTags());

        middleLayout.setSizeFull();
        middleLayout.addClassName("padding-none");

        postTitleH2.addClassName("margin-none");
        postTitleH2.addClassName("padding-l-10px");
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

        readLink.setText("READ");
        readLink.addClassName("user-link");

        lowerLayout.add(readLink);

        contentLayout.add(upperLayout, middleLayout, lowerLayout);

        add(contentLayout);
    }

    private void addTagsToPost(List<Tag> tags) {
        for (Tag tag : tags) {
            TagComponent tagComponent = new TagComponent(tag, true);
            tagComponent.addClassName("margin-l-5px");
            upperLayout.add(tagComponent);
        }
    }
}
