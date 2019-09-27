package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.component.PageSwitcherComponent;
import ru.aleynikov.blogcamp.component.TagComponent;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.service.FilterDataManager;
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

    private TextField searchTagField = new TextField();

    private Icon searchTagFieldIcon = new Icon(VaadinIcon.SEARCH);

    private Label tagsLabel = new Label("Tags");

    private Tabs sortBar = new Tabs();
    private Tab popularTab = new Tab("Popular");
    private Tab newestTab = new Tab("Newest");

    private Integer page = 1;
    private String sortTab;
    private String filter;
    private Map<String, List<String>> qparams;

    public TagsView() {
        getContent().setSizeFull();

        contentLayout.setSizeFull();
        contentLayout.getStyle().set("padding", "0");

        headerUpLayout.setSizeFull();

        tagsLabel.addClassName("content-label");

        headerUpLayout.add(tagsLabel);

        headerLowerLayout.setSizeFull();

        searchTagField.setPlaceholder("Filter by tag");
        searchTagField.setPrefixComponent(searchTagFieldIcon);
        searchTagField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchTagField);

        sortBar.add(popularTab);
        sortBar.add(newestTab);
        sortBar.addClassName("left-side-component");
        sortBar.addClassName("tabs-bar");

        sortTab = sortBar.getSelectedTab().getLabel();

        headerLowerLayout.add(searchTagField, sortBar);

        headerLayout.addClassName("content-header");
        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        sortBar.addSelectedChangeListener(event -> {
            if (sortBar.getSelectedTab() != null) {
                String selectedTab = event.getSource().getSelectedTab().getLabel();

                if (selectedTab.equals(popularTab.getLabel())) {
                    UI.getCurrent().navigate("tags", queryParametersBuilder(popularTab.getLabel()));
                } else if (selectedTab.equals(newestTab.getLabel())) {
                    UI.getCurrent().navigate("tags", queryParametersBuilder(newestTab.getLabel()));
                }
            }
        });

        searchTagField.addKeyPressListener(Key.ENTER, keyEventListener -> searchFieldProcess());

        searchTagFieldIcon.addClickListener(event -> searchFieldProcess());
    }

    private void searchFieldProcess() {
        sortBar.setSelectedTab(null);
        UI.getCurrent().navigate("tags", querySearchParametersBuilder(searchTagField.getValue().trim()));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        queryParametersSetter();

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else if (sortTab.contains(popularTab.getLabel())) {
                sortBar.setSelectedTab(popularTab);
            } else if (sortTab.contains(newestTab.getLabel())) {
                sortBar.setSelectedTab(newestTab);
            }
        }

        if (qparams.containsKey("search")) {
            tagsBrowserBuilder(page, sortTab, event.getLocation().getPath(), filter);
        } else
            tagsBrowserBuilder(page, sortTab, event.getLocation().getPath(), "");
    }

    public void queryParametersSetter() {
        if (qparams.containsKey("page")) {
            page = Integer.parseInt(qparams.get("page").get(0));
        }

        if (qparams.containsKey("tab")) {
            sortTab = qparams.get("tab").get(0);
        }

        if (qparams.containsKey("search")) {
            filter = qparams.get("search").get(0);
        }
    }

    private void tagsBrowserBuilder(int page, String sortTab, String locationPath, String filter) {
        int rowLimit = 4;
        int pageLimit;
        int counter = 0;
        HorizontalLayout row = new HorizontalLayout();
        List<Tag> tagList = null;
        float countTags = 0;

        bodyLayout.removeAll();

        row.setWidth("100%");

        if (!filter.isEmpty()) {
            tagList = tagService.getFilterTagList(page, TAGS_ON_PAGE_LIMIT, filter);
            countTags = tagService.getFilterTagsCount(filter);
        } else if (sortTab.equals(newestTab.getLabel())) {
            tagList = tagService.getNewestTagList(page, TAGS_ON_PAGE_LIMIT);
            countTags = tagService.getAllTagsCount();
        } else if (sortTab.equals(popularTab.getLabel())) {
            tagList = tagService.getPopularTagList(page, TAGS_ON_PAGE_LIMIT);
            countTags = tagService.getAllTagsCount();
        }

        if (!tagList.isEmpty()) {
            for (Tag tag : tagList) {
                counter += 1;
                row.add(new TagComponent(tag, false));

                if (counter == rowLimit || tagList.indexOf(tag) == tagList.size() - 1) {
                    bodyLayout.add(row);
                    if (!(tagList.indexOf(tag) == tagList.size() - 1)) {
                        row = new HorizontalLayout();
                        row.setWidth("100%");
                    }
                    counter = 0;
                }
            }

            pageLimit = FilterDataManager.pageLimit(countTags, TAGS_ON_PAGE_LIMIT);

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, queryParametersBuilder()));
        } else {
            bodyLayout.add(new H2("Tags not founded."));
        }
    }

    private QueryParameters queryParametersBuilder(String sortTabLabel, int page, String filter) {
        Map<String, List<String>> qmap = new HashMap<>();
        List<String> param;
        QueryParameters qparams;

        if (!filter.isEmpty()) {
            param = new ArrayList<>();
            param.add(filter);
            qmap.put("search", param);
        } else if (sortTabLabel != null){
            param = new ArrayList<>();
            param.add(sortTabLabel);
            qmap.put("tab", param);
        }

        param = new ArrayList<>();
        param.add((page == 0) ? "1" : String.valueOf(page));

        qmap.put("page", param);

        qparams = new QueryParameters(qmap);

        return qparams;
    }

    private QueryParameters querySearchParametersBuilder (String filter) {
        return queryParametersBuilder(null, 0, filter);
    }

    private QueryParameters queryParametersBuilder (String sortTabLabel) {
        return queryParametersBuilder(sortTabLabel, 0, "");
    }

    private Map<String, List<String>> queryParametersBuilder() {
        Map<String, List<String>> qmap = new HashMap<>();
        List<String> param;

        param = new ArrayList<>();
        param.add(String.valueOf(page));
        qmap.put("page", param);
        param = new ArrayList<>();
        param.add(sortTab);
        qmap.put("tab", param);

        return qmap;
    }
}
