package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@StyleSheet(StaticResources.TAG_COMPONENT_STYLES)
public class TagComponent extends Div {

    private Span tagNameSpan = new Span();
    private Span tagPostCountSpan = new Span();
    private Span tagDescSpan = new Span();

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
            }

        } else
            onlyTagBuilder(tag);
    }

    private void onlyTagBuilder(Tag tag) {
        contentLayout.addClassName("tag");

        tagNameSpan.setText(tag.getName());

        contentLayout.add(tagNameSpan);

        add(contentLayout);
    }
}
