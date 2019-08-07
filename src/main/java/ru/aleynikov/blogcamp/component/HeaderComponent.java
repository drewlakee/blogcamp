package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@StyleSheet(StaticResources.HEADER_COMPONENT_STYLES)
public class HeaderComponent extends HorizontalLayout {

    private HorizontalLayout leftSideLayout = new HorizontalLayout();
    private HorizontalLayout rightSideLayout = new HorizontalLayout();
    private HorizontalLayout mainSideLayout = new HorizontalLayout();

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");
    private Button signUpButton = new Button("Sign up");
    private Button logInButton = new Button("Log in");
    private TextField searchField = new TextField();

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
        mainSideLayout.setWidth("1100px");
        mainSideLayout.getStyle().set("margin", "0 auto");
    }

    public void initHeaderButtonProperties() {
        signUpButton.setClassName("signUpBtn");
        logInButton.setClassName("loginBtn");

        logInButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        setVerticalComponentAlignment(Alignment.CENTER, signUpButton);
        setVerticalComponentAlignment(Alignment.CENTER, logInButton);
    }

    public void initHeaderLeftSide() {
        leftSideLayout.setWidth("100%");
        leftSideLayout.setJustifyContentMode(JustifyContentMode.START);

        logoImage.setClassName("logo-header");
        setVerticalComponentAlignment(Alignment.CENTER, logoImage);

        searchField.setPlaceholder("Search...");
        searchField.setClearButtonVisible(true);
        searchField.setAutoselect(true);
        Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        searchField.setPrefixComponent(searchIcon);
        searchField.setClassName("search-textfield-header");
        setVerticalComponentAlignment(Alignment.CENTER, searchField);

        leftSideLayout.add(logoImage, searchField);

        mainSideLayout.add(leftSideLayout);
    }

    public void initHeaderRightSide() {
        rightSideLayout.setJustifyContentMode(JustifyContentMode.END);
        rightSideLayout.add(logInButton, signUpButton);
        rightSideLayout.setWidth("100%");
        mainSideLayout.add(rightSideLayout);

        add(mainSideLayout);
    }

    public void initHeaderListeners() {

        logoImage.addClickListener(imageClickEvent -> UI.getCurrent().getUI().ifPresent(ui -> ui.navigate("feed")));

        logInButton.addClickListener(ClickEvent -> getUI().ifPresent(ui -> ui.navigate("login")));

        signUpButton.addClickListener(ClickEvent -> getUI().ifPresent(ui -> ui.navigate("registration")));

        searchField.addKeyPressListener(Key.ENTER, KeyEventListener -> {
            Notification.show(searchField.getValue());
        });

    }

}
