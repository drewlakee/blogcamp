package ru.aleynikov.blogcamp.views.main;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.component.CommentComponent;
import ru.aleynikov.blogcamp.component.TagComponent;
import ru.aleynikov.blogcamp.component.UserDetailDialog;
import ru.aleynikov.blogcamp.model.Comment;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.CommentService;
import ru.aleynikov.blogcamp.service.JavaScriptUtils;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Route(value = "globe/post", layout = MainLayout.class)
@StyleSheet(StaticResources.MAIN_STYLES)
@StyleSheet(StaticResources.POST_STYLES)
public class PostView extends Composite<Div> implements HasComponents, HasUrlParameter<Integer>, HasDynamicTitle {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    private User userInSession = SecurityUtils.getPrincipal();

    private Post currentPost;

    private int commentsCount;

    private int commentsOffset = 0;
    private static final int commentsLimitOfLoad = 10;

    private String dynamicTitle = "Post not exist";

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();
    private VerticalLayout bodyFootLayout = new VerticalLayout();
    private VerticalLayout userInfoLeftSideLayout = new VerticalLayout();
    private VerticalLayout userInfoRightSideLayout = new VerticalLayout();
    private VerticalLayout footLayout = new VerticalLayout();
    private VerticalLayout footCommentsLayout = new VerticalLayout();
    private VerticalLayout notPostExistLayout = new VerticalLayout();

    private HorizontalLayout headerUpperLayout = new HorizontalLayout();
    private HorizontalLayout bodyFootUpperLayout = new HorizontalLayout();
    private HorizontalLayout bodyFootLowerLayout = new HorizontalLayout();
    private HorizontalLayout userInfoLayout = new HorizontalLayout();

    private Div userInfoDiv = new Div();
    private Div htmlDiv = new Div();

    private H2 titleH2 = new H2();
    private H2 notExistH2 = new H2("Post not exist.");

    private Image postImage = new Image();
    private Image userImage = new Image();

    private Span createdDateSpan = new Span();
    private Span userFullNameSpan = new Span();
    private Span userLink = new Span();
    private Span updateComments = new Span("Update comments");
    private Span loadMoreCommentsSpan = new Span("More");
    private Span commentsCountSpan = new Span();
    private Span editSpan = new Span("EDIT");
    private Span deleteSpan = new Span("DELETE");

    private Icon commentIcon = new Icon(VaadinIcon.COMMENT);

    private TextArea commentArea = new TextArea();

    private Button addCommentButton = new Button("Comment");
    private Button backButton = new Button("Back");

    private Icon backIcon = new Icon(VaadinIcon.CHEVRON_LEFT_SMALL);

    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private UserDetailDialog userDetailDialog;

    public PostView() {
        notPostExistLayout.setSizeFull();
        notPostExistLayout.setVisible(false);

        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();
        headerLayout.addClassName("content-header");

        titleH2.addClassName("margin-t-none");

        headerUpperLayout.setWidth("100%");

        backButton.addClassName("rs-cmp");
        backButton.addClassName("back-link");
        backButton.setIcon(backIcon);
        backButton.setIconAfterText(true);
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        headerUpperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, backButton);

        headerUpperLayout.add(titleH2, backButton);

        createdDateSpan.addClassName("margin-none");

        headerLayout.add(headerUpperLayout, createdDateSpan);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        bodyLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, postImage);

        htmlDiv.setId("html-block");
        htmlDiv.setWidth("100%");

        bodyFootLayout.addClassName("body-foot");
        bodyFootLayout.addClassName("margin-b-20px");
        bodyFootLayout.addClassName("padding-b-10px");
        bodyFootLayout.addClassName("border-b-light-grey");

        bodyFootLowerLayout.setWidth("100%");

        editSpan.addClassName("warning");
        editSpan.addClassName("opacity-07");
        editSpan.setVisible(false);

        deleteSpan.addClassName("attention");
        deleteSpan.addClassName("opacity-07");
        deleteSpan.setVisible(false);

        userInfoDiv.addClassName("user-small-block");
        userInfoDiv.addClassName("rs-cmp");

        userInfoLeftSideLayout.setWidth(null);
        userInfoLeftSideLayout.addClassName("padding-8px");

        userImage.setAlt("avatar");
        userImage.addClassName("user-small-image");

        userInfoLeftSideLayout.add(userImage);

        userInfoRightSideLayout.addClassName("user-info");
        userInfoRightSideLayout.addClassName("padding-5px");

        userLink.addClassName("link");

        userFullNameSpan.addClassName("margin-none");
        userFullNameSpan.addClassName("grey-light");

        userInfoRightSideLayout.add(userLink, userFullNameSpan);

        userInfoLayout.add(userInfoLeftSideLayout, userInfoRightSideLayout);

        userInfoDiv.add(userInfoLayout);

        bodyFootLowerLayout.add(editSpan, deleteSpan, userInfoDiv);

        bodyFootLayout.add(bodyFootUpperLayout, bodyFootLowerLayout);

        bodyLayout.add(postImage, htmlDiv, bodyFootLayout);

        footLayout.setWidth("100%");
        footLayout.addClassName("content-foot");

        commentArea.setWidth("100%");
        commentArea.setPlaceholder("Type comment here...");
        commentArea.setMaxLength(500);
        commentArea.setMinLength(1);
        commentArea.setErrorMessage("Must be not empty.");

        addCommentButton.addClassName("main-button");
        addCommentButton.addClassName("rs-cmp");

        commentIcon.addClassName("margin-l-5px");

        commentsCountSpan.addClassName("link");
        commentsCountSpan.addClassName("margin-l-5px");
        commentsCountSpan.setText("(" + commentsCount + ")");

        updateComments.add(commentsCountSpan, commentIcon);
        updateComments.addClassName("link");

        footCommentsLayout.addClassName("padding-none");

        loadMoreCommentsSpan.addClassName("fw-600");
        loadMoreCommentsSpan.addClassName("link");
        loadMoreCommentsSpan.setVisible(false);

        footLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, loadMoreCommentsSpan);

        footLayout.add(commentArea, addCommentButton, updateComments, footCommentsLayout, loadMoreCommentsSpan);

        contentLayout.add(headerLayout, bodyLayout, footLayout);

        add(notPostExistLayout, contentLayout);

        backButton.addClickListener(event -> UI.getCurrent().getPage().getHistory().back());

        addCommentButton.addClickListener(event -> {
            if (isCommentValid()) {
                HashMap<String, Object> comment = new HashMap<>();
                comment.put("text", commentArea.getValue().strip());
                comment.put("created_date", new Timestamp(System.currentTimeMillis()));
                comment.put("post_id", currentPost.getId());
                comment.put("user_id", SecurityUtils.getPrincipal().getId());
                commentService.save(comment);

                updateComments();
                commentArea.clear();
                Notification.show("Comment was posted.");
            }
        });

        updateComments.addClickListener(event -> {
            commentsOffset = 0;

            updateComments();
        });

        loadMoreCommentsSpan.addClickListener(event -> {
            commentsOffset += commentsLimitOfLoad;
            commentsCount = commentService.countByPostId(currentPost.getId());

            if (commentsOffset + commentsLimitOfLoad >= commentsCount) {
                loadMoreCommentsSpan.setVisible(false);
            }

            loadComments(commentsOffset, commentsLimitOfLoad);
        });

        deleteSpan.addClickListener(event -> {
            postService.deleteById(currentPost.getId());

            Notification.show("Post was deleted.");
            UI.getCurrent().navigate(GlobeView.class);
        });

        editSpan.addClickListener(event -> {
           UI.getCurrent().navigate("editpost/" + currentPost.getId());
        });
    }

    private boolean isCommentValid() {
        boolean isValidForm = !commentArea.isInvalid() && !commentArea.isEmpty();

        if (!isValidForm)
            commentArea.setInvalid(true);

        return isValidForm;
    }

    private boolean isValidParameter(Integer parameter) {
        Post post = postService.findById(parameter);

        if (post != null) {
            currentPost = post;
            return true;
        } else
            return false;
    }

    @Override
    public void setParameter(BeforeEvent event, Integer parameter) {
        if (isValidParameter(parameter)) {
            dynamicTitle = currentPost.getTitle();

            titleH2.setText(currentPost.getTitle());

            createdDateSpan.setText("created " + detailCreatedDateFormat.format(currentPost.getCreatedDate()));

            if (currentPost.getIntroImage() != null) {
                postImage.addClassName("post-image");
                postImage.setAlt("post image");
                postImage.setSrc(currentPost.getIntroImage());
            }

            JavaScriptUtils.innerHtml(htmlDiv.getId().get(), currentPost.getText());

            boolean firstTag = true;
            for (Tag tag : currentPost.getTags()) {
                if (firstTag) {
                    TagComponent tagComponent = new TagComponent(tag);
                    bodyFootUpperLayout.add(tagComponent);
                    firstTag = false;
                } else {
                    TagComponent tagComponent = new TagComponent(tag);
                    tagComponent.addClassName("margin-l-5px");
                    bodyFootUpperLayout.add(tagComponent);
                }
            }

            userImage.setSrc(currentPost.getUser().getAvatar());
            userLink.setText(currentPost.getUser().getUsername());
            userFullNameSpan.setText(currentPost.getUser().getFullName());

            if (userInSession.isAdmin() || currentPost.getUser().getUsername().equals(userInSession.getUsername())) {
                deleteSpan.setVisible(true);
            }

            if (currentPost.getUser().getUsername().equals(userInSession.getUsername()))
                editSpan.setVisible(true);

            userDetailDialog = new UserDetailDialog(currentPost.getUser(), userService);
            userLink.addClickListener(clickEvent -> {
                userDetailDialog.open();
            });

            loadComments(commentsOffset, commentsLimitOfLoad);
        } else {
            contentLayout.setVisible(false);
            notPostExistLayout.setVisible(true);
            notPostExistLayout.add(notExistH2);
        }
    }

    private void loadComments(int offset, int limitLoadComments) {
        List<Comment> comments;
        comments = commentService.findNewestByPostIdWithOffsetAndLimit(offset, limitLoadComments, currentPost.getId());
        commentsCount = commentService.countByPostId(currentPost.getId());
        commentsCountSpan.setText("(" + commentsCount + ")");

        for (Comment comment : comments) {
            CommentComponent commentComponent = new CommentComponent(comment, userService, commentService);
            commentComponent.addClassName("padding-none");
            footCommentsLayout.add(commentComponent);
        }

        if (offset + limitLoadComments < commentsCount) {
            loadMoreCommentsSpan.setVisible(true);
        }
    }

    private void updateComments() {
        footCommentsLayout.removeAll();
        loadComments(commentsOffset, commentsLimitOfLoad);
    }

    @Override
    public String getPageTitle() {
        return dynamicTitle;
    }
}
