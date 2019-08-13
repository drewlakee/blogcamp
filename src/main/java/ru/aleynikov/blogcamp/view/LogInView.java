package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.aleynikov.blogcamp.security.CustomRequestCache;
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

    public LogInView(AuthenticationManager authenticationManager,
                     CustomRequestCache requestCache) {
        loginLayout.setSizeFull();
        loginLayout.setClassName("login-layout");
        loginLayout.setAlignItems(Alignment.CENTER);

        logoImage.setClassName("logo-login");

        loginFormLayout.setSizeFull();
        loginFormLayout.setWidth("360px");
        loginFormLayout.setClassName("login-form");

        loginFormLayout.setHorizontalComponentAlignment(Alignment.START, loginLabel);
        loginFormLayout.setAlignItems(Alignment.CENTER);

        usernameField.getElement().setAttribute("name", "username");
        usernameField.setLabel("Username");
        usernameField.setClassName("field");
        usernameField.setWidth("100%");
        usernameField.setMaxLength(30);
        usernameField.setMinLength(6);
        usernameField.setRequired(true);

        passwordField.getElement().setAttribute("name", "password");
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

        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addClickListener(clickEvent -> {
            try {
                // try to authenticate with given credentials, should always return not null or throw an {@link AuthenticationException}
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(usernameField.getValue().trim(), passwordField.getValue().trim()));

                // if authentication was successful we will update the security context and redirect to the page requested first
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UI.getCurrent().navigate(requestCache.resolveRedirectUrl());

            } catch (AuthenticationException ex) {

                // TODO:
                // show default error message
                // Note: You should not expose any detailed information here like "username is known but password is wrong"
                // as it weakens security.
            }
        });
    }
}
