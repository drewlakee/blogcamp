package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@PageTitle("Feed")
@Route("feed")
@StyleSheet(StaticResources.FEED_VIEW_STYLES)
public class FeedView extends VerticalLayout {
    private HeaderComponent headerComponent = new HeaderComponent();

    private Button logoutButton = new Button("logout");

    public FeedView() {
        setSizeFull();
        setClassName("feedView");

        add(headerComponent, logoutButton);

        logoutButton.addClickListener(clickEvent -> {
            SecurityUtils.destroySession();
        });
    }
}
