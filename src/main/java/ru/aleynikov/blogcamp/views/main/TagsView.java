package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.component.PageSwitcherComponent;
import ru.aleynikov.blogcamp.component.TagComponent;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.service.TagService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "tags", layout = MainLayout.class)
@PageTitle("Tags - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class TagsView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private TagService tagService;

    private static final int TAGS_ON_PAGE_LIMIT = 36;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private Label allTagsLabel = new Label("All Tags");

    private Tabs sortBar = new Tabs();
    private Tab popularTab = new Tab("Popular");
    private Tab newestTab = new Tab("Newest");

    private Integer page = 1;
    private String sortTab;

    public TagsView() {
        getContent().setSizeFull();

        contentLayout.setSizeFull();
        contentLayout.getStyle().set("padding", "0");

        headerUpLayout.setSizeFull();

        allTagsLabel.addClassName("content-label");

        headerUpLayout.add(allTagsLabel);

        headerLowerLayout.setSizeFull();

        sortBar.add(popularTab);
        sortBar.add(newestTab);
        sortBar.addClassName("left-side-component");
        sortBar.addClassName("sort-bar");

        sortTab = sortBar.getSelectedTab().getLabel().toLowerCase();

        headerLowerLayout.add(sortBar);

        headerLayout.addClassName("content-header");
        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        sortBar.addSelectedChangeListener(event -> {
            String selectedTab = event.getSource().getSelectedTab().getLabel();
            Map<String, List<String>> qmap = new HashMap<>();
            List<String> param;
            QueryParameters qparams;

            if (selectedTab.equals(popularTab.getLabel())) {
                param = new ArrayList<>();
                param.add(popularTab.getLabel().toLowerCase());
                qmap.put("tab", param);
                param = new ArrayList<>();
                param.add("1");
                qmap.put("page", param);
                qparams = new QueryParameters(qmap);

                UI.getCurrent().navigate("tags", qparams);
            } else if (selectedTab.equals(newestTab.getLabel())) {
                param = new ArrayList<>();
                param.add(newestTab.getLabel().toLowerCase());
                qmap.put("tab", param);
                param = new ArrayList<>();
                param.add("1");
                qmap.put("page", param);
                qparams = new QueryParameters(qmap);

                UI.getCurrent().navigate("tags", qparams);
            }
        });
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Map<String, List<String>> qparams = event.getLocation().getQueryParameters().getParameters();
        Map<String, List<String>> qmap = new HashMap<>();
        List<String> param;

        if (qparams.containsKey("page")) {
            page = Integer.parseInt(qparams.get("page").get(0));
        }

        if (qparams.containsKey("tab")) {
            sortTab = qparams.get("tab").get(0);
        }

        if (!sortBar.getSelectedTab().toString().toLowerCase().contains(sortTab)) {
            if (sortTab.contains(popularTab.getLabel().toLowerCase())) {
                sortBar.setSelectedTab(popularTab);
            } else if (sortTab.contains(newestTab.getLabel().toLowerCase())) {
                sortBar.setSelectedTab(newestTab);
            }
        }

        bodyLayout.removeAll();

        tagsBrowserBuilder(page, sortTab);


        param = new ArrayList<>();
        param.add(String.valueOf(page));
        qmap.put("page", param);
        param = new ArrayList<>();
        param.add(sortTab);
        qmap.put("tab", param);
        float countTags = tagService.getAllTagsCount();
        int pageLimit = Math.round(countTags/TAGS_ON_PAGE_LIMIT);

        if (pageLimit == 0) pageLimit = 1;

        bodyLayout.add(new PageSwitcherComponent(page, pageLimit, event.getLocation().getPath(), qmap));
    }

    private void tagsBrowserBuilder(int page, String sortTab) {
        int rowTagLimit = 4;
        int counter = 0;
        HorizontalLayout row;
        List<Tag> tagList;

        row = new HorizontalLayout();
        row.setWidth("100%");

        if (sortTab.equals("newest")) {
            tagList = tagService.getNewestTagList(page, TAGS_ON_PAGE_LIMIT);
        } else
            tagList = tagService.getPopularTagList(page, TAGS_ON_PAGE_LIMIT);

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
