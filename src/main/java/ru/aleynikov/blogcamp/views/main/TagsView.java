package ru.aleynikov.blogcamp.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.component.TagComponent;
import ru.aleynikov.blogcamp.daoImpl.TagDaoImpl;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.service.TagService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.MainLayout;

import java.util.List;
import java.util.Map;

@Route(value = "tags", layout = MainLayout.class)
@PageTitle("Tags - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class TagsView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagDaoImpl tagDao;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private Label allTagsLabel = new Label("All Tags");

    private Tabs sortBar = new Tabs();
    private Tab popularTab = new Tab("Popular");

    private int page = 1;

    public TagsView() {
        getContent().setSizeFull();
        getContent().getStyle().set("z-index", "2");

        contentLayout.setSizeFull();
        contentLayout.getStyle().set("padding", "0");

        headerUpLayout.setSizeFull();

        allTagsLabel.addClassName("content-label");

        headerUpLayout.add(allTagsLabel);

        headerLowerLayout.setSizeFull();

        sortBar.add(popularTab);
        sortBar.addClassName("left-side-component");
        sortBar.addClassName("sort-bar");

        headerLowerLayout.add(sortBar);

        headerLayout.addClassName("content-header");
        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> qparams = queryParameters.getParameters();

        if (qparams.containsKey("page")) {
            page = Integer.parseInt(qparams.get("page").get(0));
        }


        tagsBrowserBuilder(page);
    }

        public void tagsBrowserBuilder(int page) {
        int rowTagLimit = 4;
        int counter = 0;
        HorizontalLayout row;
        List<Tag> tagList;

        row = new HorizontalLayout();
        row.setWidth("100%");

        tagList = tagService.getPopularTagList(page);
        for (Tag tag : tagList) {
            counter += 1;
            row.add(new TagComponent(tag, false));

            if (counter == rowTagLimit || tagList.indexOf(tag) == tagList.size() - 1) {
                bodyLayout.add(row);
                if (!(tagList.indexOf(tag) == tagList.size() - 1)) {
                    row = new HorizontalLayout();
                    row.setWidth("100%");
                }
                counter = 0;
            }
        }
    }
}
