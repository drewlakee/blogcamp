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
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.service.TagService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route(value = "tags", layout = MainLayout.class)
@PageTitle("Tags - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class TagsView extends Composite<Div> implements HasComponents, HasUrlParameter<String> {

    @Autowired
    private TagService tagService;

    private static final int TAGS_ON_PAGE_LIMIT = 24;

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout headerLayout = new VerticalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    private HorizontalLayout headerUpLayout = new HorizontalLayout();
    private HorizontalLayout headerLowerLayout = new HorizontalLayout();

    private TextField searchField = new TextField();

    private Icon searchTagFieldIcon = new Icon(VaadinIcon.SEARCH);

    private Label tagsLabel = new Label("Tags");

    private H2 notFoundedH2 = new H2("Tags not founded.");

    private Tabs sortBar = new Tabs();
    private Tab popularTab = new Tab("Popular");
    private Tab newestTab = new Tab("Newest");

    private HashMap<String, Object> pageParametersMap = new HashMap<>(
            Map.of("tab", "",
                    "search","",
                    "page", "1")
    );
    private static Set<String> pageParametersKeySet = Set.of("tab", "search", "page");
    private Map<String, List<String>> qparams;

    public TagsView() {
        getContent().setSizeFull();

        contentLayout.setSizeFull();
        contentLayout.getStyle().set("padding", "0");

        headerUpLayout.setSizeFull();

        tagsLabel.addClassName("content-label");

        headerUpLayout.add(tagsLabel);

        headerLowerLayout.setSizeFull();

        searchField.setPlaceholder("Filter by tag");
        searchField.setPrefixComponent(searchTagFieldIcon);
        searchField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchField);

        sortBar.add(popularTab);
        sortBar.add(newestTab);
        sortBar.addClassName("rs-cmp");
        sortBar.addClassName("tabs-bar");

        headerLowerLayout.add(searchField, sortBar);

        headerLayout.addClassName("content-header");
        headerLayout.add(headerUpLayout, headerLowerLayout);

        bodyLayout.setSizeFull();
        bodyLayout.addClassName("content-body");

        contentLayout.add(headerLayout, bodyLayout);

        add(contentLayout);

        sortBar.addSelectedChangeListener(event -> {
            if (sortBar.getSelectedTab() != null) {
                String selectedTab = event.getSource().getSelectedTab().getLabel();
                HashMap<String, Object> customQueryParams = new HashMap<>();

                if (selectedTab.equals(popularTab.getLabel())) {
                    customQueryParams.put("tab", popularTab.getLabel().toLowerCase());
                    UI.getCurrent().navigate("tags", new QueryParameters(QueryParametersManager.qparamsBuild(customQueryParams)));
                } else if (selectedTab.equals(newestTab.getLabel())) {
                    customQueryParams.put("tab", newestTab.getLabel().toLowerCase());
                    UI.getCurrent().navigate("tags", new QueryParameters(QueryParametersManager.qparamsBuild(customQueryParams)));
                }
            }
        });

        searchField.addKeyPressListener(Key.ENTER, keyEventListener -> searchFieldProcess());

        searchTagFieldIcon.addClickListener(event -> searchFieldProcess());
    }

    private void searchFieldProcess() {
        if (!searchField.isEmpty()) {
            sortBar.setSelectedTab(null);
            HashMap<String, Object> customQueryParams = new HashMap<>();
            customQueryParams.put("search", searchField.getValue().strip());
            UI.getCurrent().navigate("tags", new QueryParameters(QueryParametersManager.qparamsBuild(customQueryParams)));
        } else
            UI.getCurrent().navigate("tags");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        setQueryParams(qparams);

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else if (pageParametersMap.get("tab").equals(newestTab.getLabel().toLowerCase())) {
                sortBar.setSelectedTab(newestTab);
                pageParametersMap.replace("tab", newestTab.getLabel().toLowerCase());
            } else {
                sortBar.setSelectedTab(popularTab);
                pageParametersMap.replace("tab", popularTab.getLabel().toLowerCase());
            }
        }

        tagsBrowserBuild(
                Integer.parseInt(pageParametersMap.get("page").toString()),
                pageParametersMap.get("tab").toString(),
                event.getLocation().getPath(),
                pageParametersMap.get("search").toString()
        );
    }

    private void setQueryParams(Map<String, List<String>> qparams) {
        for (String parameter : pageParametersKeySet) {
            if (!parameter.equals("page")) {
                pageParametersMap.replace(parameter, "");
            } else
                pageParametersMap.replace(parameter, "1");
        }

        for (String parameter : pageParametersKeySet) {
            if (qparams.containsKey(parameter)) {
                pageParametersMap.replace(parameter, qparams.get(parameter).get(0));
            }
        }
    }

    private void tagsBrowserBuild(int page, String sortTab, String locationPath, String search) {
        int rowLimit = 4;
        int pageLimit;
        int counter = 0;
        HorizontalLayout row = new HorizontalLayout();
        List<Tag> tagList;
        float countTags;
        HashMap<String, Object> customQueryParams = new HashMap<>();

        customQueryParams.put("page", page);

        bodyLayout.removeAll();

        row.setWidth("100%");

        if (!search.isEmpty()) {
            tagList = tagService.getFilterTagList(page, TAGS_ON_PAGE_LIMIT, search);
            countTags = tagService.getFilterTagsCount(search);
            customQueryParams.put("search", search);
        } else if (sortTab.equals(newestTab.getLabel().toLowerCase())) {
            tagList = tagService.getNewestTagList(page, TAGS_ON_PAGE_LIMIT);
            countTags = tagService.getAllTagsCount();
            customQueryParams.put("tab", sortTab);
        } else {
            tagList = tagService.getPopularTagList(page, TAGS_ON_PAGE_LIMIT);
            countTags = tagService.getAllTagsCount();
            customQueryParams.put("tab", sortTab);
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

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, QueryParametersManager.qparamsBuild(customQueryParams)));
        } else {
            bodyLayout.add(notFoundedH2);
        }
    }
}
