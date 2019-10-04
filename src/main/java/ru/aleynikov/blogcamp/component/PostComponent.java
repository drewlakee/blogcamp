package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.Locale;

@StyleSheet(StaticResources.POST_STYLES)
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class PostComponent extends Div {

    @Autowired
    private UserService userService;

    private Post post;

    private User postUser;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout middleLayout = new VerticalLayout();

    private HorizontalLayout upperLayout = new HorizontalLayout();
    private HorizontalLayout lowerLayout = new HorizontalLayout();

    private Span usernameSpan = new Span();
    private Span dotSpan = new Span();
    private Span createdDateSpan = new Span();

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    public PostComponent(Post post) {
        this.post = post;

        addClassName("post-block");

        contentLayout.setSizeFull();

        upperLayout.setWidth("100%");

        usernameSpan.addClassName("username");
        usernameSpan.setText(post.getUser().getUsername());

        dotSpan.addClassName("grey-light");
        dotSpan.addClassName("margin-l-5px");
        dotSpan.setText("•");

        createdDateSpan.addClassName("grey-light");
        createdDateSpan.addClassName("margin-l-5px");
        createdDateSpan.setText("Posted in " + createdDateFormat.format(post.getCreatedDate()));
        createdDateSpan.setTitle(detailCreatedDateFormat.format(post.getCreatedDate()));

        upperLayout.add(usernameSpan, dotSpan, createdDateSpan);

        contentLayout.add(upperLayout);

        add(contentLayout);
    }
}
