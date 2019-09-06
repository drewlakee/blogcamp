package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageSwitcherComponent extends HorizontalLayout {

    private Span dotsSpan = new Span("...");

    private HorizontalLayout buttonsLayout = new HorizontalLayout();

    private Button firstPageButton = new Button();
    private Button previousPageButton = new Button();
    private Button currentPageButton = new Button();
    private Button nextPageButton = new Button();
    private Button lastPageButton = new Button();

    public PageSwitcherComponent(int currentPage, int lastPage, String location, Map<String, List<String>> qparams) {
        setWidth("100%");

        buttonsLayout.getStyle().set("margin-left", "auto");
        buttonsLayout.getStyle().set("margin-right", "50px");

        firstPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        previousPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        currentPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        nextPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        lastPageButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        if (currentPage > 2) {
            firstPageButton.setText("1");
            firstPageButton.addClickListener(event -> clickOnPageButtonHandler(firstPageButton, location, qparams));
            buttonsLayout.add(firstPageButton, dotsSpan);
        }

        if (currentPage - 1 > 0) {
            previousPageButton.setText(String.valueOf(currentPage - 1));
            previousPageButton.addClickListener(event -> clickOnPageButtonHandler(previousPageButton, location, qparams));

            buttonsLayout.add(previousPageButton);
        }

        if (currentPage > 0) {
            currentPageButton.setText(String.valueOf(currentPage));
            currentPageButton.addClickListener(event -> clickOnPageButtonHandler(currentPageButton, location, qparams));
            currentPageButton.setEnabled(false);

            buttonsLayout.add(currentPageButton);
        }

        if (currentPage + 1 <= lastPage) {
            nextPageButton.setText(String.valueOf(currentPage + 1));
            nextPageButton.addClickListener(event -> clickOnPageButtonHandler(nextPageButton, location, qparams));

            buttonsLayout.add(nextPageButton);
        }

        if (currentPage <= lastPage - 2) {
            lastPageButton.setText(String.valueOf(lastPage));
            lastPageButton.addClickListener(event -> clickOnPageButtonHandler(lastPageButton, location, qparams));

            buttonsLayout.add(dotsSpan, lastPageButton);
        }

        add(buttonsLayout);
    }

    private void clickOnPageButtonHandler(Button button, String location, Map<String, List<String>> qparams) {
        List<String> page = new ArrayList<>();
        page.add(button.getText());

        qparams.replace("page", page);

        UI.getCurrent().navigate(location, new QueryParameters(qparams));
    }

}