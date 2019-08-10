package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@PageTitle("Log in")
@Route("login")
@StyleSheet(StaticResources.LOGIN_VIEW_STYLES)
public class LogInView extends HorizontalLayout {

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");
    private VerticalLayout loginLayout = new VerticalLayout();
    private VerticalLayout loginFormLayout = new VerticalLayout();
    private H2 loginLabel = new H2("Log in");

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button loginButton = new Button("Log in");
    private Button forgotPasswordButton = new Button("Forgot password");

    private RouterLink signUpLink = new RouterLink("Sign up", SignUpView.class);

    public LogInView() {
        loginLayout.setSizeFull();
        loginLayout.setClassName("login-layout");
        loginLayout.setAlignItems(Alignment.CENTER);

        logoImage.setClassName("logo-login");

        loginFormLayout.setSizeFull();
        loginFormLayout.setWidth("360px");
        loginFormLayout.setClassName("login-form");

        loginFormLayout.setHorizontalComponentAlignment(Alignment.START, loginLabel);
        loginFormLayout.setAlignItems(Alignment.CENTER);

        usernameField.setLabel("Username");
        usernameField.setClassName("field");
        usernameField.setWidth("100%");
        usernameField.setMaxLength(30);
        usernameField.setMinLength(6);
        usernameField.setRequired(true);

        passwordField.setLabel("Password");
        passwordField.setClassName("field");
        passwordField.setWidth("100%");
        passwordField.setMaxLength(30);
        passwordField.setMinLength(8);
        passwordField.setRequired(true);

        loginButton.setClassName("button");

        forgotPasswordButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        signUpLink.setClassName("signup-link");

        loginFormLayout.add(loginLabel, usernameField,
                passwordField, loginButton, forgotPasswordButton, signUpLink);

        loginLayout.add(logoImage, loginFormLayout);

        setVerticalComponentAlignment(Alignment.CENTER, loginLayout);

        add(loginLayout);
    }
}
