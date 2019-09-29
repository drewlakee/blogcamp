package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.richtexteditor.RichTextEditorVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.aleynikov.blogcamp.service.JavaScriptUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@Route(value = "posts/add", layout = MainLayout.class)
@PageTitle("Add post - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
@StyleSheet(StaticResources.POST_STYLES)
public class AddPostView extends Composite<Div> implements HasComponents {

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private TextField titleField = new TextField();

    private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload introImageUpload = new Upload(buffer);

    private TextArea textEditor = new TextArea();

    private Div htmlAreaDiv = new Div();

    private H2 labelH2 = new H2("Add a public post");

    private Span titleSpan = new Span("Post title");
    private Span uploadSpan = new Span("Upload post intro image");
    private Span textEditorSpan = new Span("Post body");

    public AddPostView() {
        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();

        headerLayout.add(labelH2);

        bodyLayout.setSizeFull();

        titleField.addClassName("margin-none");
        titleField.setWidth("100%");
        titleField.setPlaceholder("Five things that inspiring me");

        introImageUpload.addClassName("margin-none");
        introImageUpload.setDropAllowed(false);

        textEditor.setWidth("100%");
        textEditor.getStyle().set("resize", "vertical");
        textEditor.addClassName("margin-none");
        textEditor.getStyle().set("overflow-y", "auto");
        textEditor.setPlaceholder("<h1>Html editor!</h1>");

        htmlAreaDiv.addClassName("html-shower");
        htmlAreaDiv.setId("format");

        bodyLayout.add(titleSpan, titleField, uploadSpan, introImageUpload, textEditorSpan, textEditor, htmlAreaDiv);

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);
        textEditor.addValueChangeListener(event -> {
            if (!isContainCustomWidthOrHeightStyles(textEditor.getValue())) {
                JavaScriptUtils.innerHtml(htmlAreaDiv.getId().get(), textEditor.getValue());
                textEditor.setInvalid(false);
            } else
                textEditor.setInvalid(true);
        });
    }

    private boolean isContainCustomWidthOrHeightStyles(String postHtml) {
        return postHtml.contains("width") || postHtml.contains("height");
    }
}
