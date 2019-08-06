package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@PageTitle("blogcamp.")
@Route("feed")
@StyleSheet(StaticResources.FEED_VIEW_STYLES)
public class FeedView extends VerticalLayout {
    HeaderComponent headerComponent = new HeaderComponent();

    public FeedView() {
        setSizeFull();
        setClassName("feedView");

        add(headerComponent);


    }
}
