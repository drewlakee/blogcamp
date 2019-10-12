package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.text.SimpleDateFormat;
import java.util.*;

@StyleSheet(StaticResources.TAG_STYLES)
public class TagComponent extends Div {

    private Span tagNameSpan = new Span();
    private Span tagPostCountSpan = new Span();
    private Span tagDescSpan = new Span();
    private Span createdDateSpan = new Span();

    private Paragraph br = new Paragraph("");

    private SimpleDateFormat createdDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
    private SimpleDateFormat detailCreatedDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private HorizontalLayout contentLayout = new HorizontalLayout();

    public TagComponent(Tag tag, boolean onlyTag) {
        if (!onlyTag) {
            addClassName("tag-block");

            contentLayout.addClassName("tag");

            tagNameSpan.setText(tag.getName());

            contentLayout.add(tagNameSpan);

            if (tag.getPostCount() > 0) {
                tagPostCountSpan.setText(String.valueOf(tag.getPostCount()));
                tagPostCountSpan.addClassName("tag-post-count");
                contentLayout.add(tagPostCountSpan);
            }

            add(contentLayout);

            if (tag.getDescription() != null) {
                tagDescSpan.setText(tag.getDescription());
                tagDescSpan.addClassName("tag-desc");
                add(tagDescSpan);
                add(br);
            }

            createdDateSpan.addClassName("tag-date");
            createdDateSpan.setText("created " + createdDateFormat.format(tag.getCreatedDate()));
            createdDateSpan.setTitle(detailCreatedDateFormat.format(tag.getCreatedDate()));

            add(createdDateSpan);

        } else
            onlyTagBuilder(tag);

        tagNameSpan.addClickListener(event -> {
            HashMap<String, Object> qparam = new HashMap<>();
            qparam.put("tag", tag.getName());
            UI.getCurrent().navigate("globe", new QueryParameters(QueryParametersManager.buildQueryParams(qparam)));
        });
    }

    private void onlyTagBuilder(Tag tag) {
        contentLayout.addClassName("tag");

        tagNameSpan.setText(tag.getName());

        contentLayout.add(tagNameSpan);

        add(contentLayout);
    }
}
