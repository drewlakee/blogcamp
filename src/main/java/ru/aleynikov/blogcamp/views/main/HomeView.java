package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.components.PostCutComponent;
import ru.aleynikov.blogcamp.components.TagComponent;
import ru.aleynikov.blogcamp.components.UserDetailDialog;
import ru.aleynikov.blogcamp.models.Post;
import ru.aleynikov.blogcamp.models.Tag;
import ru.aleynikov.blogcamp.models.User;
import ru.aleynikov.blogcamp.services.PostService;
import ru.aleynikov.blogcamp.services.TagService;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.statics.StaticContent;

import java.util.List;

@Route(value = "home", layout = MainLayout.class)
@PageTitle("Blogcamp")
@StyleSheet(StaticContent.MAIN_STYLES)
public class HomeView extends Composite<Div> implements HasComponents, BeforeEnterObserver {

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    private final static int TOP_TAGS_LIMIT = 10;
    private final static int INTERESTING_POSTS_LIMIT = 5;
    private final static int TOP_ACTIVE_USERS = 10;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();
    private VerticalLayout popularTagsLayout = new VerticalLayout();
    private VerticalLayout interestingPostsLayout = new VerticalLayout();
    private VerticalLayout postsLayout = new VerticalLayout();
    private VerticalLayout activeUsersLayout = new VerticalLayout();

    private HorizontalLayout tagsLayout = new HorizontalLayout();
    private HorizontalLayout usersLayout = new HorizontalLayout();

    private H2 topPopularTagsH2 = new H2("Top " + TOP_TAGS_LIMIT + " popular tags");
    private H2 topInterestingPostsH2 = new H2("Top " + INTERESTING_POSTS_LIMIT + " interesting posts");
    private H2 topActiveUsersH2 = new H2("Top " + TOP_ACTIVE_USERS + " active users");

    public HomeView() {
        getContent().setSizeFull();

        contentLayout.setHeight(null);
        contentLayout.addClassName("padding-none");

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        activeUsersLayout.setWidth(null);

        topActiveUsersH2.addClassName("active");

        activeUsersLayout.add(topActiveUsersH2, usersLayout);

        topInterestingPostsH2.addClassName("padding-b-5px");
        topInterestingPostsH2.addClassName("pinkish");

        interestingPostsLayout.setWidth("80%");

        postsLayout.setWidth("100%");
        postsLayout.addClassName("padding-none");

        interestingPostsLayout.add(topInterestingPostsH2, postsLayout);

        popularTagsLayout.setWidth(null);

        topPopularTagsH2.setWidth("100%");
        topPopularTagsH2.addClassName("fw-600");
        topPopularTagsH2.addClassName("padding-b-5px");
        topPopularTagsH2.addClassName("blue-ocean");

        popularTagsLayout.add(topPopularTagsH2, tagsLayout);

        bodyLayout.add(activeUsersLayout, interestingPostsLayout, popularTagsLayout);

        contentLayout.add(bodyLayout);

        add(contentLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<Tag> popularTags;
        List<Post> interestingPosts;
        List<User> activeUsers;

        popularTags = tagService.getTopPopularTags(TOP_TAGS_LIMIT);

        tagsLayout.removeAll();
        popularTags.forEach(tag -> {
            tagsLayout.add(new TagComponent(tag));
        });

        interestingPosts = postService.getInterestingPostListWithLimit(INTERESTING_POSTS_LIMIT);

        postsLayout.removeAll();
        interestingPosts.forEach(post -> {
            postsLayout.add(new PostCutComponent(post, null));
        });

        usersLayout.removeAll();
        activeUsers = userService.getActiveUsersWithLimit(TOP_ACTIVE_USERS);

        activeUsers.forEach(user -> {
            Image userImg = new Image();
            UserDetailDialog userDetailDialog = new UserDetailDialog(user, userService);
            userImg.setAlt("user image");
            userImg.setSrc(user.getAvatar());
            userImg.setClassName("small-user-avatar");
            userImg.addClickListener(event1 -> userDetailDialog.open());

            usersLayout.add(userImg);
        });


    }
}

