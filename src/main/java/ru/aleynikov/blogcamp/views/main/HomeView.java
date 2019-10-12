package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.component.CommentComponent;
import ru.aleynikov.blogcamp.model.Comment;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.sql.Timestamp;

@Route(value = "home", layout = MainLayout.class)
@PageTitle("Blogcamp")
@StyleSheet(StaticResources.MAIN_STYLES)
public class HomeView extends Composite<Div> implements HasComponents{


    public HomeView() {
        Comment comment = new Comment();
        User user = new User();
        user.setUsername("drewlakee");
        user.setAvatar("https://vignette.wikia.nocookie.net/spongebob/images/8/83/Flat%2C800x800%2C075%2Ct.jpg");
        comment.setUser(user);
        comment.setText("textetextweadfas asdasdasd asdasdas asdas dasd asd asd asd asd adasdasdasdasdasdasdasdasdasdsdasd");
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        add(new CommentComponent(comment));
        add(new CommentComponent(comment));
        add(new CommentComponent(comment));
        add(new CommentComponent(comment));
    }
}
