package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import ru.aleynikov.blogcamp.staticComponents.StaticResources;

@StyleSheet(StaticResources.HEADER_COMPONENT_STYLES)
public class HeaderComponent extends HorizontalLayout {
    private HorizontalLayout leftSideLayout = new HorizontalLayout();
    private HorizontalLayout rightSideLayout = new HorizontalLayout();

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");
    private Image searchLoopImage = new Image(StaticResources.SEARCHLOOP_IMAGE, "searchLoop");
    private Button signUpButton = new Button("Sign up");
    private Button loginButton = new Button("Login");
    private TextField searchField = new TextField();
    private Button searchButton = new Button("Search");

    public HeaderComponent() {
        initHeaderLayoutProperties();
        initHeaderButtonProperties();

        initHeaderLeftSide();
        initHeaderRightSide();

        initHeaderListeners();
    }

    public void initHeaderLayoutProperties() {
        setWidth("100%");
        setClassName("site-header");
    }

    public void initHeaderButtonProperties() {
        signUpButton.setClassName("signUpBtn");
        loginButton.setClassName("loginBtn");

        loginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        setVerticalComponentAlignment(Alignment.CENTER, signUpButton);
        setVerticalComponentAlignment(Alignment.CENTER, loginButton);
    }

    public void initHeaderLeftSide() {
        leftSideLayout.setWidth("100%");
        leftSideLayout.getStyle().set("padding-left", "10px");
        leftSideLayout.setJustifyContentMode(JustifyContentMode.START);

        logoImage.setClassName("logo-header");
        setVerticalComponentAlignment(Alignment.CENTER, logoImage);

        searchLoopImage.setClassName("search-logo-header");
        setVerticalComponentAlignment(Alignment.CENTER, searchLoopImage);

        searchField.setPlaceholder("Search");
        searchField.setVisible(false);
        searchField.setClearButtonVisible(true);
        setVerticalComponentAlignment(Alignment.CENTER, searchField);

        searchButton.setVisible(false);
        searchButton.setClassName("search-button-header");
        setVerticalComponentAlignment(Alignment.CENTER, searchButton);

        leftSideLayout.add(logoImage, searchLoopImage, searchField, searchButton);

        add(leftSideLayout);
    }

    public void initHeaderRightSide() {
        rightSideLayout.setJustifyContentMode(JustifyContentMode.END);
        rightSideLayout.add(loginButton, signUpButton);
        rightSideLayout.setWidth("100%");
        rightSideLayout.getStyle().
                set("padding-right", "10px");

        add(rightSideLayout);
    }

    public void initHeaderListeners() {
        searchLoopImage.addClickListener(imageClickEvent -> {
           searchField.setVisible(true);
           searchButton.setVisible(true);
           searchLoopImage.setVisible(false);
           searchField.focus();
        });


    }

}
