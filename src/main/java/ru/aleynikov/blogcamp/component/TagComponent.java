package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.JavaScriptUtils;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.service.TagService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.*;

@StyleSheet(StaticResources.MAIN_STYLES)
@StyleSheet(StaticResources.TAG_STYLES)
public class TagComponent extends Div {

    private User userInSession = SecurityUtils.getPrincipal();

    private Span tagNameSpan = new Span();
    private Span tagPostCountSpan = new Span();
    private Span tagDescSpan = new Span();
    private Span createdDateSpan = new Span();

    private Span editSpan = new Span("Edit");

    private Paragraph br = new Paragraph("");

    private TextField descriptionField = new TextField();

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private HorizontalLayout contentLayout = new HorizontalLayout();
    private HorizontalLayout upperLayout = new HorizontalLayout();

    public TagComponent(Tag tag) {
        contentLayout.addClassName("tag");

        tagNameSpan.setText(tag.getName());

        if (tag.getDescription() != null) {
            tagNameSpan.setTitle(tag.getDescription());
        }

        contentLayout.add(tagNameSpan);

        add(contentLayout);

        tagNameSpan.addClickListener(event -> {
            HashMap<String, Object> qparam = new HashMap<>();
            qparam.put("tag", tag.getName());
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.buildQueryParams(qparam)));
            JavaScriptUtils.scrollPageTop();
        });
    }

    public TagComponent(Tag tag, TagService tagService) {
        addClassName("tag-block");

        contentLayout.addClassName("tag");

        tagNameSpan.setText(tag.getName());

        contentLayout.add(tagNameSpan);

        if (userInSession.isAdmin())
            contentLayout.add(editSpan);

        if (tag.getPostCount() > 0) {
            tagPostCountSpan.setText(String.valueOf(tag.getPostCount()));
            tagPostCountSpan.addClassName("tag-post-count");
            contentLayout.add(tagPostCountSpan);
        }

        upperLayout.add(contentLayout);

        if (userInSession.isAdmin()) {
            editSpan.addClassName("link");
            editSpan.addClassName("fs-12px");
            editSpan.addClassName("margin-l-5px");
            upperLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, editSpan);
            upperLayout.add(editSpan);
        }

        if (tag.getDescription() != null) {
            tagDescSpan.setText(tag.getDescription());
        }

        tagDescSpan.addClassName("tag-desc");

        descriptionField.setWidth("100%");
        descriptionField.setMaxLength(100);
        descriptionField.setVisible(false);

        add(upperLayout, tagDescSpan, br, descriptionField);

        createdDateSpan.addClassName("tag-date");
        createdDateSpan.setText("created " + createdDateFormat.format(tag.getCreatedDate()));
        createdDateSpan.setTitle(detailCreatedDateFormat.format(tag.getCreatedDate()));

        add(createdDateSpan);

        editSpan.addClickListener(event -> {
            descriptionField.setValue(tagDescSpan.getText());
            descriptionField.setVisible(true);
            tagDescSpan.setVisible(false);
        });

        descriptionField.addKeyDownListener(Key.ENTER, event -> {
            String value = descriptionField.getValue().strip();
            tagService.updateDescriptionById(value, tag.getId());
            tagDescSpan.setText(value);
            descriptionField.setVisible(false);
            tagDescSpan.setVisible(true);
        });

        tagNameSpan.addClickListener(event -> {
            HashMap<String, Object> qparam = new HashMap<>();
            qparam.put("tag", tag.getName());
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.buildQueryParams(qparam)));
            JavaScriptUtils.scrollPageTop();
        });
    }
}
