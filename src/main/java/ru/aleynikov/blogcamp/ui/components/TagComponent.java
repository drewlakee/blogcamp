package ru.aleynikov.blogcamp.ui.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.QueryParameters;
import ru.aleynikov.blogcamp.domain.models.Tag;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.ui.web.JavaScript;
import ru.aleynikov.blogcamp.services.QueryParametersConstructor;
import ru.aleynikov.blogcamp.services.TagService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

import java.text.SimpleDateFormat;
import java.util.*;

@StyleSheet(StaticContent.MAIN_STYLES)
@StyleSheet(StaticContent.TAG_STYLES)
public class TagComponent extends Div {

    private User userInSession = SecurityUtils.getPrincipal();

    private Span tagNameSpan = new Span();
    private Span tagDescSpan = new Span();
    private Span createdDateSpan = new Span();

    private Span editSpan = new Span("Edit");

    private Paragraph br = new Paragraph("");

    private TextArea descriptionArea = new TextArea();

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
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersConstructor.buildQueryParams(qparam)));
            JavaScript.scrollPageTop();
        });
    }

    public TagComponent(Tag tag, TagService tagService) {
        addClassName("tag-block");

        contentLayout.addClassName("tag");

        tagNameSpan.setText(tag.getName());

        contentLayout.add(tagNameSpan);

        if (userInSession.isAdmin())
            contentLayout.add(editSpan);

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

        descriptionArea.setWidth("100%");
        descriptionArea.setMaxLength(100);
        descriptionArea.setVisible(false);

        add(upperLayout, tagDescSpan, br, descriptionArea);

        createdDateSpan.addClassName("tag-date");
        createdDateSpan.setText("created " + createdDateFormat.format(tag.getCreatedDate()));
        createdDateSpan.setTitle(detailCreatedDateFormat.format(tag.getCreatedDate()));

        add(createdDateSpan);

        editSpan.addClickListener(event -> {
            descriptionArea.setValue(tagDescSpan.getText());
            descriptionArea.setVisible(true);
            tagDescSpan.setVisible(false);
        });

        descriptionArea.addValueChangeListener(event -> {
            String value = descriptionArea.getValue().strip();
            tagService.updateDescriptionById(value, tag.getId());
            tagDescSpan.setText(value);
            descriptionArea.setVisible(false);
            tagDescSpan.setVisible(true);
        });

        descriptionArea.addKeyDownListener(Key.ENTER, event -> descriptionArea.setValue(descriptionArea.getValue()));

        tagNameSpan.addClickListener(event -> {
            HashMap<String, Object> qparam = new HashMap<>();
            qparam.put("tag", tag.getName());
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersConstructor.buildQueryParams(qparam)));
            JavaScript.scrollPageTop();
        });
    }
}
