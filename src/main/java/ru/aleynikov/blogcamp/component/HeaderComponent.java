package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@StyleSheet(StaticResources.HEADER_COMPONENT_STYLES)
public class HeaderComponent extends HorizontalLayout {

    private HorizontalLayout leftSideLayout = new HorizontalLayout();
    private HorizontalLayout rightSideLayout = new HorizontalLayout();
    private HorizontalLayout mainSideLayout = new HorizontalLayout();

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");

    private Button logoutButton = new Button("Logout");

    private TextField searchField = new TextField();

    private Div logoDiv = new Div();
    private Div logoutDiv = new Div();


    public HeaderComponent() {
        setWidth("100%");
        setHeight("10px");
        setClassName("site-header");

        mainSideLayout.setWidth("1100px");
        mainSideLayout.getStyle().set("margin", "0 auto");

        leftSideLayout.setWidth("100%");
        leftSideLayout.setJustifyContentMode(JustifyContentMode.START);

        logoImage.setClassName("logo-header");
        setVerticalComponentAlignment(Alignment.CENTER, logoImage);

        logoDiv.setClassName("div-component");
        logoDiv.setHeight("100%");
        logoDiv.getStyle().set("margin-left", "25px");
        logoDiv.add(logoImage);

        searchField.setPlaceholder("Search...");
        searchField.setClearButtonVisible(true);
        searchField.setAutoselect(true);
        searchField.setWidth("100%");
        Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        searchField.setPrefixComponent(searchIcon);
        searchField.setClassName("search-textfield-header");
        setVerticalComponentAlignment(Alignment.CENTER, searchField);

        leftSideLayout.add(logoDiv, searchField);

        rightSideLayout.setJustifyContentMode(JustifyContentMode.END);
        rightSideLayout.setWidth("100%");


        logoutDiv.addClassName("div-component");
        logoutDiv.setHeight("100%");

        logoutButton.addClassName("logoutBtn");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        logoutDiv.add(logoutButton);

        rightSideLayout.add(logoutDiv);

        mainSideLayout.add(leftSideLayout, rightSideLayout);

        add(mainSideLayout);

        logoImage.addClickListener(imageClickEvent -> UI.getCurrent().navigate(""));
        logoutButton.addClickListener(clickEvent -> SecurityUtils.destroySession());

        searchField.addKeyPressListener(Key.ENTER, KeyEventListener -> {
            Notification.show(searchField.getValue());
        });
    }
}
