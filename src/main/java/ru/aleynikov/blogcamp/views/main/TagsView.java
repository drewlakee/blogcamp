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
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.FilterDataManager;
import ru.aleynikov.blogcamp.service.PostService;
import ru.aleynikov.blogcamp.service.QueryParametersManager;
import ru.aleynikov.blogcamp.service.TagService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Route(value = "tags", layout = MainLayout.class)
@PageTitle("Tags - Blogcamp")
@StyleSheet(StaticResources.MAIN_STYLES)
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
    private Tab newestTab = new Tab("Newest");
    private Tab aToZTab = new Tab("A-Z");

    private HashMap<String, Object> pageParametersMap = new HashMap<>(
            Map.of("tab", "",
                    "search","",
                    "page", "1")
    );
    private static Set<String> pageParametersKeySet = Set.of("tab", "search", "page");
    private Map<String, List<String>> qparams;

    public TagsView() {
        getContent().setSizeFull();

        contentLayout.setHeight(null);
        contentLayout.addClassName("padding-none");

        headerUpLayout.setSizeFull();

        tagsLabel.addClassName("content-label");

        headerUpLayout.add(tagsLabel);

        headerLowerLayout.setSizeFull();

        searchField.setPlaceholder("Filter by tag");
        searchField.setPrefixComponent(searchTagFieldIcon);
        searchField.setClearButtonVisible(true);
        headerLowerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, searchField);

        sortBar.add(newestTab, aToZTab);
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

                if (selectedTab.equals(aToZTab.getLabel())) {
                    customQueryParams.put("tab", aToZTab.getLabel().toLowerCase());
                } else {
                    customQueryParams.put("tab", newestTab.getLabel().toLowerCase());
                }

                UI.getCurrent().navigate("tags", new QueryParameters(QueryParametersManager.buildQueryParams(customQueryParams)));
            }
        });

        searchField.addKeyPressListener(Key.ENTER, keyEventListener ->
                searchFieldProcess());
        searchTagFieldIcon.addClickListener(event ->
                searchFieldProcess());
    }

    private void searchFieldProcess() {
        if (!searchField.isEmpty()) {
            sortBar.setSelectedTab(null);
            HashMap<String, Object> customQueryParams = new HashMap<>();
            customQueryParams.put("search", searchField.getValue().strip());
            UI.getCurrent().navigate("tags", new QueryParameters(QueryParametersManager.buildQueryParams(customQueryParams)));
        } else
            UI.getCurrent().navigate("tags");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        qparams = event.getLocation().getQueryParameters().getParameters();
        QueryParametersManager.setQueryParams(qparams, pageParametersMap, pageParametersKeySet);

        if (sortBar.getSelectedTab() != null) {
            if (qparams.containsKey("search")) {
                sortBar.setSelectedTab(null);
            } else if (pageParametersMap.get("tab").equals(aToZTab.getLabel().toLowerCase())) {
                sortBar.setSelectedTab(aToZTab);
                pageParametersMap.replace("tab", aToZTab.getLabel().toLowerCase());
            } else {
                sortBar.setSelectedTab(newestTab);
                pageParametersMap.replace("tab", newestTab.getLabel().toLowerCase());
            }
        }

        buildTagsBrowser(
                Integer.parseInt(pageParametersMap.get("page").toString()),
                pageParametersMap.get("tab").toString(),
                event.getLocation().getPath(),
                pageParametersMap.get("search").toString()
        );
    }

    private void buildTagsBrowser(int page, String sortTab, String locationPath, String search) {
        int rowLimit = 4;
        int pageLimit = 0;
        int counter = 0;
        HorizontalLayout row = new HorizontalLayout();
        List<Tag> tagList;
        float countTags;
        HashMap<String, Object> customQueryParams = new HashMap<>();

        customQueryParams.put("page", page);

        bodyLayout.removeAll();

        row.setWidth("100%");

        if (!search.isEmpty()) {
            tagList = tagService.getTagListBySearch(page, TAGS_ON_PAGE_LIMIT, search);
            countTags = tagService.getTagsCountBySearch(search);
            customQueryParams.put("search", search);
        } else if (pageParametersMap.get("tab").equals(aToZTab.getLabel().toLowerCase())) {
            tagList = tagService.findTagsSortedByNameAsc(page, TAGS_ON_PAGE_LIMIT);
            countTags = tagService.getAllTagsCount();
            customQueryParams.put("tab", sortTab);
        } else {
            tagList = tagService.getNewestTagList(page, TAGS_ON_PAGE_LIMIT);
            countTags = tagService.getAllTagsCount();
            customQueryParams.put("tab", sortTab);
        }
        
        if (!tagList.isEmpty()) {
            for (Tag tag : tagList) {
                counter += 1;
                row.add(new TagComponent(tag, tagService));

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

            bodyLayout.add(new PageSwitcherComponent(page, pageLimit, locationPath, QueryParametersManager.buildQueryParams(customQueryParams)));
        } else {
            bodyLayout.add(notFoundedH2);
        }
    }
}
