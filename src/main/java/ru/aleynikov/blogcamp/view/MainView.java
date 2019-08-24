package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.component.HeaderComponent;
import ru.aleynikov.blogcamp.staticResources.StaticResources;



@PageTitle("Blogcamp.")
@Route("")
@StyleSheet(StaticResources.MAIN_VIEW_STYLES)
public class MainView extends VerticalLayout {
    private HeaderComponent headerComponent = new HeaderComponent();

    private RichTextEditor richTextEditor = new RichTextEditor();
    public MainView() {
        setSizeFull();
        setClassName("mainView");


        add(headerComponent);
    }
}
