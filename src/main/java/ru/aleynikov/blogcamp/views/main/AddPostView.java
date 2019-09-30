package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
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
    private TextField tagsField = new TextField();
    private TextField externalLinkOnImageField = new TextField();

    private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload introImageUpload = new Upload(buffer);

    private TextArea textEditor = new TextArea();

    private Div htmlAreaDiv = new Div();
    private Div uploadedImageDiv = new Div();

    private H2 labelH2 = new H2("Add a public post");

    private Span titleSpan = new Span("Post title");
    private Span uploadSpan = new Span("Upload post intro image");
    private Span textEditorSpan = new Span("Post body");
    private Span tagsSpan = new Span("Tags");

    private Button uploadButton = new Button("Upload image");
    private Button createPostButton = new Button("Create post");

    private RadioButtonGroup<String> imageLoadGroup = new RadioButtonGroup<>();

    public AddPostView() {
        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();

        headerLayout.add(labelH2);

        bodyLayout.setSizeFull();

        titleSpan.addClassName("title");

        titleField.addClassName("margin-none");
        titleField.setWidth("100%");
        titleField.setPlaceholder("Five things that inspiring me");

        uploadSpan.addClassName("title");

        imageLoadGroup.addClassName("margin-none");
        imageLoadGroup.setItems("Set external image", "Load image");

        introImageUpload.addClassName("margin-none");
        introImageUpload.setDropAllowed(false);
        introImageUpload.setUploadButton(uploadButton);
        introImageUpload.setVisible(false);

        externalLinkOnImageField.setWidth("100%");
        externalLinkOnImageField.addClassName("margin-none");
        externalLinkOnImageField.setPlaceholder("https://image.jpg");
        externalLinkOnImageField.setVisible(false);

        uploadedImageDiv.setId("show-image");

        textEditorSpan.addClassName("title");

        textEditor.setWidth("100%");
        textEditor.getStyle().set("resize", "vertical");
        textEditor.addClassName("margin-none");
        textEditor.getStyle().set("overflow-y", "auto");
        textEditor.setPlaceholder("<h1>Html editor!</h1>");

        htmlAreaDiv.addClassName("html-shower");
        htmlAreaDiv.addClassName("padding-10px");
        htmlAreaDiv.setId("format");

        tagsSpan.addClassName("title");

        tagsField.setWidth("100%");
        tagsField.setPlaceholder("life, meme, books etc.");
        tagsField.addClassName("margin-none");

        createPostButton.addClassName("main-button");

        bodyLayout.add(titleSpan, titleField, uploadSpan, imageLoadGroup,
                introImageUpload, externalLinkOnImageField, uploadedImageDiv,
                textEditorSpan, textEditor, htmlAreaDiv,
                tagsSpan, tagsField, createPostButton);

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        imageLoadGroup.addValueChangeListener(event -> {
           if (imageLoadGroup.getValue().equals("Set external image")) {
               externalLinkOnImageField.setVisible(true);
               introImageUpload.setVisible(false);
               JavaScriptUtils.innerHtml("show-image", "");
           } else if (imageLoadGroup.getValue().equals("Load image")) {
               introImageUpload.setVisible(true);
               externalLinkOnImageField.clear();
               externalLinkOnImageField.setVisible(false);
               JavaScriptUtils.innerHtml("show-image", "");
           }
        });

        externalLinkOnImageField.addValueChangeListener(event -> {
           JavaScriptUtils.innerHtml("show-image", "<img style=\"width: 500px\" src=" + externalLinkOnImageField.getValue().strip() + ">");
        });

        textEditor.addValueChangeListener(event -> {
            JavaScriptUtils.innerHtml(htmlAreaDiv.getId().get(), textEditor.getValue());
        });
    }
}
