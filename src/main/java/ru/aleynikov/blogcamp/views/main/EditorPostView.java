package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.JavaScriptUtils;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.*;

@Route(value = "posts/add", layout = MainLayout.class)
@RouteAlias(value = "posts/edit")
@PageTitle("Add post - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
@StyleSheet(StaticResources.POST_STYLES)
public class EditorPostView extends Composite<Div> implements HasComponents {

    @Autowired
    private PostService postService;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private TextField titleField = new TextField();
    private TextField tagsField = new TextField();
    private TextField externalLinkOnImageField = new TextField();

    private TextArea textBody = new TextArea();

    private Div htmlAreaDiv = new Div();
    private Div uploadedImageDiv = new Div();

    private H2 labelH2 = new H2("Add a public post");

    private Span titleSpan = new Span("Post title");
    private Span uploadSpan = new Span("Upload post intro image");
    private Span textEditorSpan = new Span("Post body");
    private Span tagsSpan = new Span("Tags");

    private Button createPostButton = new Button("Create post");

    private RadioButtonGroup<String> imageLoadGroup = new RadioButtonGroup<>();
    private static final String EXTERNAL_IMAGE = "Set external image";
    private static final String NONE_IMAGE = "Without image";

    public EditorPostView() {
        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();

        headerLayout.add(labelH2);

        bodyLayout.setSizeFull();

        titleSpan.addClassName("title");

        titleField.addClassName("margin-none");
        titleField.setWidth("100%");
        titleField.setPlaceholder("Five things that inspiring me");
        titleField.setRequired(true);
        titleField.setMinLength(10);
        titleField.setMaxLength(100);
        titleField.setErrorMessage("Minimal " + titleField.getMinLength() + " length of title.");

        uploadSpan.addClassName("title");

        imageLoadGroup.addClassName("margin-none");
        imageLoadGroup.setItems(EXTERNAL_IMAGE, NONE_IMAGE);
        imageLoadGroup.setRequired(true);
        imageLoadGroup.setErrorMessage("Must choose one.");

        externalLinkOnImageField.setWidth("100%");
        externalLinkOnImageField.addClassName("margin-none");
        externalLinkOnImageField.setMinLength(5);
        externalLinkOnImageField.setMaxLength(500);
        externalLinkOnImageField.setPlaceholder("https://image.jpg");
        externalLinkOnImageField.setErrorMessage("Must be external link with correct format: jpg, jpeg, png, gif.");
        externalLinkOnImageField.setVisible(false);

        uploadedImageDiv.setId("show-image");

        textEditorSpan.addClassName("title");

        textBody.setWidth("100%");
        textBody.getStyle().set("resize", "vertical");
        textBody.addClassName("margin-none");
        textBody.getStyle().set("overflow-y", "auto");
        textBody.setPlaceholder("<h1>Html editor!</h1>");
        textBody.setRequired(true);
        textBody.setMinLength(30);
        textBody.setMaxLength(3000);
        textBody.setErrorMessage("Minimal " + textBody.getMinLength() + " length of body.");

        htmlAreaDiv.addClassName("html-shower");
        htmlAreaDiv.addClassName("padding-10px");
        htmlAreaDiv.setId("format");

        tagsSpan.addClassName("title");

        tagsField.setWidth("100%");
        tagsField.setPlaceholder("life, meme, books etc.");
        tagsField.addClassName("margin-none");
        tagsField.setRequired(true);
        tagsField.setMinLength(2);
        tagsField.setErrorMessage("Must be at least one tag. Maximum is 5 tags.");

        createPostButton.addClassName("main-button");

        bodyLayout.add(titleSpan, titleField, uploadSpan, imageLoadGroup, externalLinkOnImageField, uploadedImageDiv,
                textEditorSpan, textBody, htmlAreaDiv,
                tagsSpan, tagsField, createPostButton);

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        imageLoadGroup.addValueChangeListener(event -> {
           if (imageLoadGroup.getValue().equals(EXTERNAL_IMAGE)) {
               externalLinkOnImageField.setVisible(true);
               externalLinkOnImageField.setRequired(true);
           } else if (imageLoadGroup.getValue().equals(NONE_IMAGE)) {
               externalLinkOnImageField.setVisible(false);
               externalLinkOnImageField.setRequired(false);
               JavaScriptUtils.innerHtml("show-image", "");
           }
        });

        externalLinkOnImageField.addValueChangeListener(event -> {
            int imgWidth = 500;
            if (isExternalSourceValid())
                JavaScriptUtils.innerHtml("show-image", "<img style=\"width:" + imgWidth + "px\" src=" + externalLinkOnImageField.getValue().strip() + ">");
        });

        textBody.addValueChangeListener(event -> {
            JavaScriptUtils.innerHtml(htmlAreaDiv.getId().get(), textBody.getValue());
        });

        createPostButton.addClickListener(event -> {
           if (isPostFormValid()) {
               HashMap<String, Object> newPost = new LinkedHashMap<>();
               newPost.put("title", titleField.getValue().strip());
               if (!imageLoadGroup.isEmpty() && imageLoadGroup.getValue().equals(EXTERNAL_IMAGE) && isExternalSourceValid())
                   newPost.put("intro_image", externalLinkOnImageField.getValue().strip());
               newPost.put("text", textBody.getValue().strip());
               newPost.put("user", SecurityUtils.getPrincipal().getId());
               postService.savePost(newPost);

               Set<String> selectedTags = new LinkedHashSet<>();
               Arrays.stream(tagsField.getValue().split(" ")).forEach((x) -> selectedTags.add(x.toLowerCase().replaceAll(",", "")));
               selectedTags.removeIf((x) -> x.equals(""));
               postService.setTagsToPost(selectedTags, newPost);

               UI.getCurrent().navigate("home");
           }
        });
    }

    private boolean isExternalSourceValid() {
        String sourceValue = externalLinkOnImageField.getValue().toLowerCase();
        boolean isFieldNotEmpty = !sourceValue.isEmpty();
        boolean isSourceExternal = sourceValue.startsWith("http") && sourceValue.contains("://");
        boolean isSourceImage = sourceValue.endsWith("png") || sourceValue.endsWith("jpg") ||
                sourceValue.endsWith("gif") || sourceValue.endsWith("jpeg");

        return isSourceExternal && isSourceImage && isFieldNotEmpty;
    }

    private boolean isPostFormValid() {
        boolean isTitleValid = !titleField.isInvalid() && !titleField.isEmpty();
        boolean isBodyValid = !textBody.isInvalid() && !textBody.isEmpty() && textBody.getValue().strip().length() > textBody.getMinLength();
        boolean isTagsValid = !tagsField.isInvalid() && !tagsField.isEmpty();
        int tagsLength = tagsField.getValue().split(" ").length;
        boolean isTagsLengthValid = tagsLength > 0 && tagsLength < 6;
        boolean isTagsValidValue = Arrays.stream(tagsField.getValue().split(" ")).allMatch((x) -> x.length() > 1);
        boolean isImageUploadValid = !imageLoadGroup.isEmpty();

        if (!isTitleValid)
            titleField.setInvalid(true);

        if (!isBodyValid)
            textBody.setInvalid(true);

        if (!isTagsValid || !isTagsLengthValid || !isTagsValidValue) {
            tagsField.setInvalid(true);
        }

        if (!isImageUploadValid) {
            imageLoadGroup.setInvalid(true);
        }

        return isTitleValid && isBodyValid && isTagsValid && isTagsLengthValid
                && isTagsValidValue && isImageUploadValid;
    }
}
