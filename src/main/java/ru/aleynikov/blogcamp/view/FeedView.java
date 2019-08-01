package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.component.HeaderComponent;

@PageTitle("blogcamp.")
@Route("feed")
@StyleSheet("styles/stylesFeed.css")
public class FeedView extends VerticalLayout {
    HeaderComponent headerComponent = new HeaderComponent();

    public FeedView() {
        setSizeFull();
        setClassName("feedView");

        add(headerComponent);


    }
}
