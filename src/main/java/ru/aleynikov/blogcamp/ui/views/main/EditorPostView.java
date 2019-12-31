package ru.aleynikov.blogcamp.ui.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.domain.models.Post;
import ru.aleynikov.blogcamp.domain.models.Tag;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.ui.web.JavaScript;
import ru.aleynikov.blogcamp.services.PostService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

import java.sql.Timestamp;
import java.util.*;

@Route(value = "addpost", layout = MainLayout.class)
@RouteAlias(value = "editpost", layout = MainLayout.class)
@StyleSheet(StaticContent.MAIN_STYLES)
@StyleSheet(StaticContent.POST_STYLES)
public class EditorPostView extends Composite<Div> implements HasComponents, HasUrlParameter<Integer>, HasDynamicTitle {

    @Autowired
    private PostService postService;

    private String dynamicTitle = "Add post - Blogcamp";

    private User userInSession = SecurityUtils.getPrincipal();

    private Post postForEdit;

    private int externalImageWidth = 500;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpperLayout = new HorizontalLayout();

    private TextField titleField = new TextField();
    private TextField tagsField = new TextField();
    private TextField externalLinkOnImageField = new TextField();

    private TextArea textBodyArea = new TextArea();

    private Div htmlAreaDiv = new Div();
    private Div uploadedImageDiv = new Div();

    private H2 labelH2 = new H2("Add a public post");

    private Span titleSpan = new Span("Post title");
    private Span uploadSpan = new Span("Upload post intro image");
    private Span textEditorSpan = new Span("Post body");
    private Span tagsSpan = new Span("Tags");

    private Button createPostButton = new Button("Create post");
    private Button updatePostButton = new Button("Update");
    private Button backButton = new Button("Back");

    private Icon backIcon = new Icon(VaadinIcon.CHEVRON_LEFT_SMALL);

    private RadioButtonGroup<String> imageLoadGroup = new RadioButtonGroup<>();
    private static final String EXTERNAL_IMAGE = "Set external image";
    private static final String NONE_IMAGE = "Without image";

    public EditorPostView() {
        contentLayout.setSizeFull();
        contentLayout.addClassName("padding-none");

        headerLayout.setSizeFull();

        headerUpperLayout.setWidth("100%");

        labelH2.addClassName("margin-t-none");

        backButton.addClassName("rs-cmp");
        backButton.addClassName("back-link");
        backButton.setIcon(backIcon);
        backButton.setIconAfterText(true);
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        headerUpperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, backButton);

        headerUpperLayout.add(labelH2, backButton);

        headerLayout.add(headerUpperLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("margin-none");

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

        textBodyArea.setWidth("100%");
        textBodyArea.getStyle().set("resize", "vertical");
        textBodyArea.addClassName("margin-none");
        textBodyArea.getStyle().set("overflow-y", "auto");
        textBodyArea.setPlaceholder("<h1>Html editor!</h1>");
        textBodyArea.setRequired(true);
        textBodyArea.setMinLength(30);
        textBodyArea.setMaxLength(10000);
        textBodyArea.setErrorMessage("Minimal " + textBodyArea.getMinLength() + " length of body.");

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

        updatePostButton.addClassName("main-button");
        updatePostButton.setVisible(false);

        bodyLayout.add(titleSpan, titleField, uploadSpan, imageLoadGroup, externalLinkOnImageField, uploadedImageDiv,
                textEditorSpan, textBodyArea, htmlAreaDiv,
                tagsSpan, tagsField, createPostButton, updatePostButton);

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        imageLoadGroup.addValueChangeListener(event -> {
           if (imageLoadGroup.getValue().equals(EXTERNAL_IMAGE)) {
               externalLinkOnImageField.setVisible(true);
               externalLinkOnImageField.setRequired(true);
           } else if (imageLoadGroup.getValue().equals(NONE_IMAGE)) {
               externalLinkOnImageField.setVisible(false);
               externalLinkOnImageField.setRequired(false);
               JavaScript.innerHtml("show-image", "");
           }
        });

        externalLinkOnImageField.addValueChangeListener(event -> {
            if (isExternalSourceValid())
                JavaScript.innerHtml("show-image", "<img style=\"width:" + externalImageWidth + "px\" src=" + externalLinkOnImageField.getValue().strip() + ">");
        });

        textBodyArea.addValueChangeListener(event -> {
            JavaScript.innerHtml(htmlAreaDiv.getId().get(), textBodyArea.getValue());
        });

        createPostButton.addClickListener(event -> {
           if (isPostFormValid()) {
               HashMap<String, Object> newPost = new LinkedHashMap<>();
               newPost.put("title", titleField.getValue().strip());
               if (!imageLoadGroup.isEmpty() && imageLoadGroup.getValue().equals(EXTERNAL_IMAGE) && isExternalSourceValid())
                   newPost.put("intro_image", externalLinkOnImageField.getValue().strip());
               newPost.put("text", textBodyArea.getValue().strip());
               newPost.put("user", SecurityUtils.getPrincipal().getId());
               postService.savePost(newPost);

               Set<String> selectedTags = new LinkedHashSet<>();
               Arrays.stream(tagsField.getValue().split(" ")).forEach((x) -> selectedTags.add(x.toLowerCase().replaceAll(",", "")));
               selectedTags.removeIf((x) -> x.equals(""));
               postService.setTagsToPost(selectedTags, newPost);

               UI.getCurrent().navigate(GlobeView.class);
           }
        });

        updatePostButton.addClickListener(event -> {
            if (isPostFormValid()) {
                HashMap<String, Object> updatedPost = new LinkedHashMap<>();
                updatedPost.put("post_id", postForEdit.getId());
                updatedPost.put("title", titleField.getValue().strip());
                updatedPost.put("text", textBodyArea.getValue().strip());
                updatedPost.put("user", postForEdit.getUser().getId());
                if (imageLoadGroup.getValue().equals(EXTERNAL_IMAGE))
                    updatedPost.put("intro_image", externalLinkOnImageField.getValue().strip());
                else
                    updatedPost.put("intro_image", null);
                updatedPost.put("created_date", new Timestamp(System.currentTimeMillis()));
                postService.update(updatedPost);

                Set<String> newTags = new LinkedHashSet<>();
                Arrays.stream(tagsField.getValue().split(" ")).filter(tag -> !tag.strip().isEmpty()).forEach((x) -> newTags.add(x.toLowerCase().replaceAll(",", "")));

                Set<String> oldTags = new LinkedHashSet<>();
                postForEdit.getTags().stream().forEach(tag -> oldTags.add(tag.getName()));

                Set<String> intersectionOfNewAndOldTags = new HashSet<>();
                intersectionOfNewAndOldTags.addAll(newTags);
                intersectionOfNewAndOldTags.retainAll(oldTags);

                newTags.removeAll(intersectionOfNewAndOldTags);
                oldTags.removeAll(intersectionOfNewAndOldTags);

                postService.setTagsToPost(newTags, updatedPost);
                postService.removeTagsFromPost(oldTags, updatedPost);

                UI.getCurrent().navigate(PostView.class, postForEdit.getId());
            }
        });

        backButton.addClickListener(event -> UI.getCurrent().getPage().getHistory().back());
    }

    @Override
    public String getPageTitle() {
        return dynamicTitle;
    }

    private boolean isPermissionsAvailable(Integer parameter) {
        Optional<Post> post = postService.findPostById(parameter);
        boolean isAvailable = false;

        if (post.isPresent() && post.map(Post::getUser).orElseThrow().getUsername().equals(userInSession.getUsername())) {
            postForEdit = post.get();
            isAvailable = true;
        }

        return isAvailable;
    }

    private boolean isValidParameter(Integer parameter) {
        boolean isNotNull = parameter != null;
        boolean isCorrect = false;

        if (isNotNull)
            isCorrect = parameter > 0;

        return isNotNull && isCorrect;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer parameter) {
        if (event.getLocation().getPath().startsWith("editpost") && isValidParameter(parameter) && isPermissionsAvailable(parameter)) {
            titleField.setValue(postForEdit.getTitle());

            if (postForEdit.getIntroImage() != null) {
                imageLoadGroup.setValue(EXTERNAL_IMAGE);
                externalLinkOnImageField.setValue(postForEdit.getIntroImage());
            } else
                imageLoadGroup.setValue(NONE_IMAGE);

            textBodyArea.setValue(postForEdit.getText());

            for (Tag tag : postForEdit.getTags()) {
                tagsField.setValue(tagsField.getValue() + " " + tag.getName());
            }

            createPostButton.setVisible(false);
            updatePostButton.setVisible(true);

            dynamicTitle = "Edit post - Blogcamp";
        } else if (!event.getLocation().getPath().startsWith("addpost")) {
            UI.getCurrent().getPage().getHistory().back();
        }
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
        boolean isBodyValid = !textBodyArea.isInvalid() && !textBodyArea.isEmpty() && textBodyArea.getValue().strip().length() > textBodyArea.getMinLength();
        boolean isTagsValid = !tagsField.isInvalid() && !tagsField.isEmpty();
        long tagsLength = Arrays.stream(tagsField.getValue().split(" ")).filter(tag -> !tag.strip().isEmpty()).count();
        boolean isTagsLengthValid = tagsLength > 0 && tagsLength < 6;
        boolean isTagsValidValue = Arrays.stream(tagsField.getValue().split(" ")).filter(tag -> !tag.strip().isEmpty()).allMatch((tag) -> tag.length() > 1 && tag.length() < 21);
        boolean isImageUploadValid = !imageLoadGroup.isEmpty();

        if (!isTitleValid)
            titleField.setInvalid(true);

        if (!isBodyValid)
            textBodyArea.setInvalid(true);

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
