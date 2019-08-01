package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import ru.aleynikov.blogcamp.StaticResources;

@StyleSheet("styles/stylesHeaderElements.css")
public class HeaderComponent extends HorizontalLayout {
    private HorizontalLayout leftSideLayout = new HorizontalLayout();
    private HorizontalLayout rightSideLayout = new HorizontalLayout();

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");
    private Button signUpButton = new Button("Sign up");
    private Button loginButton = new Button("Login");

    public HeaderComponent() {
        initHeaderLayoutProperties();
        initHeaderButtonProperties();

        initHeaderLeftSide();
        initHeaderRightSide();
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

        leftSideLayout.add(logoImage);

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

}
