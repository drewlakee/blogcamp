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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.component.TagComponent;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.service.JavaScriptUtils;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.*;

@Route(value = "globe/post", layout = MainLayout.class)
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
@StyleSheet(StaticResources.POST_STYLES)
public class PostView extends Composite<Div> implements HasComponents, HasUrlParameter<Integer>, HasDynamicTitle {

    @Autowired
    private PostService postService;

    private Post currentPost;

    private String dynamicTitle;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();
    private VerticalLayout bodyFootLayout = new VerticalLayout();
    private VerticalLayout userInfoLeftSideLayout = new VerticalLayout();
    private VerticalLayout userInfoRightSideLayout = new VerticalLayout();


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
    private Button backButton = new Button("Back");

    private Icon backIcon = new Icon(VaadinIcon.CHEVRON_LEFT_SMALL);

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    public PostView() {
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

        bodyFootLowerLayout.add(userInfoDiv);

        bodyFootLayout.add(bodyFootUpperLayout, bodyFootLowerLayout);

        bodyLayout.add(postImage, htmlDiv, bodyFootLayout);

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        backButton.addClickListener(event -> UI.getCurrent().getPage().getHistory().back());
    }

    private boolean isValidParameter(Integer parameter) {
        Post post = postService.findPostById(parameter);

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

            createdDateSpan.setText("created " + createdDateFormat.format(currentPost.getCreatedDate()));
            createdDateSpan.setTitle(detailCreatedDateFormat.format(currentPost.getCreatedDate()));

            if (currentPost.getIntroImage() != null) {
                postImage.addClassName("post-image");
                postImage.setAlt("post image");
                postImage.setSrc(currentPost.getIntroImage());
            }

            JavaScriptUtils.innerHtml(htmlDiv.getId().get(), currentPost.getText());

            boolean firstTag = true;
            for (Tag tag : currentPost.getTags()) {
                if (firstTag) {
                    TagComponent tagComponent = new TagComponent(tag, true);
                    bodyFootUpperLayout.add(tagComponent);
                    firstTag = false;
                } else {
                    TagComponent tagComponent = new TagComponent(tag, true);
                    tagComponent.addClassName("margin-l-5px");
                    bodyFootUpperLayout.add(tagComponent);
                }
            }

            userImage.setSrc(currentPost.getUser().getAvatar());
            userLink.setText(currentPost.getUser().getUsername());
            userFullNameSpan.setText(currentPost.getUser().getFullName());

            userLink.addClickListener(clickEvent -> {
                HashMap<String, Object> qparams = new HashMap<>();
                qparams.put("user", currentPost.getUser().getUsername());
                UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.buildQueryParams(qparams)));
            });

        } else {
            headerLayout.add(notExistH2);
        }
    }

    @Override
    public String getPageTitle() {
        return dynamicTitle;
    }
}
