package ru.aleynikov.blogcamp.views.main.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.component.PostCutComponent;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.ProfileView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route(value = "posts", layout = ProfileView.class)
@PageTitle("Profile - Posts")
@StyleSheet(StaticResources.PROFILE_STYLES)
public class PostsView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private PostService postService;

    private static final int LIMIT_OF_POSTS = 5;

    private int postsOffset = 0;

    private int postsCount;

    private User userInSession = SecurityUtils.getPrincipal();

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();
    private VerticalLayout footLayout = new VerticalLayout();

    private HorizontalLayout headerHorizontalLayout = new HorizontalLayout();

    private Span moreSpan = new Span("MORE");

    private TextField searchField = new TextField();

    private Icon searchIcon = new Icon(VaadinIcon.SEARCH);

    private HashMap<String, Object> pageParametersMap = new HashMap<>(
            Map.of("search","")
    );
    private static Set<String> pageParametersKeySet = Set.of("search");
    private Map<String, List<String>> qparams;

    public PostsView() {
        contentLayout.setWidth("100%");

        headerLayout.setWidth("100%");

        headerHorizontalLayout.setWidth("100%");

        searchField.setPlaceholder("Search by title");
        searchField.setPrefixComponent(searchIcon);
        searchField.setClearButtonVisible(true);
        headerHorizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, searchField);

        headerHorizontalLayout.add(searchField);

        headerLayout.add(headerHorizontalLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("margin-none");

        footLayout.setWidth("100%");

        moreSpan.addClassName("link");
        moreSpan.addClassName("fw-600");
        footLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, moreSpan);
        moreSpan.setVisible(false);

        footLayout.add(moreSpan);

        contentLayout.add(headerLayout, bodyLayout, footLayout);

        add(contentLayout);

        moreSpan.addClickListener(event -> {
           loadMorePosts(postsOffset, LIMIT_OF_POSTS, pageParametersMap.get("search").toString());
        });

        searchIcon.addClickListener(event -> search());

        searchField.addKeyPressListener(Key.ENTER, event -> search());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        QueryParametersManager.setQueryParams(qparams, pageParametersMap, pageParametersKeySet);

        buildPostsBrowser(postsOffset, LIMIT_OF_POSTS, pageParametersMap.get("search").toString());
    }

    private void search() {
        postsOffset = 0;
        bodyLayout.removeAll();
        String search = searchField.getValue().strip();
        pageParametersMap.replace("search", search);

        if (!search.isEmpty()) {
            HashMap<String, Object> qparams = new HashMap<>();
            qparams.put("search", search);

            UI.getCurrent().navigate("profile/posts", new QueryParameters(QueryParametersManager.buildQueryParams(qparams)));
        } else
            UI.getCurrent().navigate("profile/posts");
    }

    private void loadMorePosts(int offset, int limit, String search) {
        if (!search.isEmpty()) {
            buildPostsBrowser(offset, limit, search);
        } else
            buildPostsBrowser(offset, limit, "");
    }

    private void buildPostsBrowser(int offset, int limit, String search) {
        List<Post> posts;

        if (!search.isEmpty()) {
            posts = postService.findNewestPostsByUserIdAndSearchByTitle(offset, limit, userInSession.getId(), search);
            postsCount = postService.countByUserIdAndSearchByTitle(userInSession.getId(), search);
        } else {
            posts = postService.findNewestPostsByUserId(offset, limit, userInSession.getId());
            postsCount = postService.countPostsByUsername(userInSession.getUsername());
        }

        if (postsOffset + LIMIT_OF_POSTS < postsCount)
            moreSpan.setVisible(true);
        else
            moreSpan.setVisible(false);

        if (postsCount == 0) {
            bodyLayout.removeAll();
            bodyLayout.add(new H2("Posts not founded."));
        }

        postsOffset += LIMIT_OF_POSTS;

        for(Post post : posts) {
            bodyLayout.add(new PostCutComponent(post, postService));
        }
    }
}
