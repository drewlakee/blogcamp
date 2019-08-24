package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LoginView extends HorizontalLayout {

    private static Logger log = LoggerFactory.getLogger(LoginView.class);

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");

    private HorizontalLayout loginErrorLayout = new HorizontalLayout();

    private VerticalLayout loginLayout = new VerticalLayout();
    private VerticalLayout loginFormLayout = new VerticalLayout();

    private H2 loginLabel = new H2("Log in");

    private Label errorLoginLabel = new Label("Invalid username or password.");

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button loginButton = new Button("Log in");
    private Button forgotPasswordButton = new Button("Forgot password?");

    private RouterLink signUpLink = new RouterLink("Sign up", SignUpView.class);

    public LoginView(AuthenticationManager authenticationManager,
                     CustomRequestCache requestCache) {
        loginLayout.setSizeFull();
        loginLayout.setClassName("login-layout");
        loginLayout.setAlignItems(Alignment.CENTER);

        logoImage.setClassName("logo-login");

        loginErrorLayout.setSizeFull();
        loginErrorLayout.setClassName("error-login");
        loginErrorLayout.add(errorLoginLabel);
        loginErrorLayout.setAlignItems(Alignment.CENTER);
        loginErrorLayout.setVisible(false);

        errorLoginLabel.setClassName("error-label-login");

        loginFormLayout.setSizeFull();
        loginFormLayout.setWidth("360px");
        loginFormLayout.setClassName("login-form");
        loginFormLayout.setHorizontalComponentAlignment(Alignment.START, loginLabel);
        loginFormLayout.setAlignItems(Alignment.CENTER);

        loginLabel.setClassName("login-label");

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

        loginFormLayout.add(loginErrorLayout, loginLabel, usernameField,
                passwordField, loginButton, forgotPasswordButton, signUpLink);

        loginLayout.add(logoImage, loginFormLayout);

        setVerticalComponentAlignment(Alignment.CENTER, loginLayout);

        add(loginLayout);

        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addClickListener(clickEvent -> {
            if (isLoginFormValid()) {
                try {
                    /**
                     *   try to authenticate with given credentials, should always return not null or throw an {@link AuthenticationException}
                     */

                    log.info("User trying authenticates with username [{}]", usernameField.getValue().trim());
                    log.info("select username, password, active from usr where username={}", usernameField.getValue().trim());
                    final Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(usernameField.getValue().trim(), passwordField.getValue().trim()));

                    /**
                     *   if authentication was successful we will update the security context and redirect to the page requested first
                     */

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("User was authenticated [{}] with authorities {}",
                            SecurityContextHolder.getContext().getAuthentication().getName(),
                            SecurityContextHolder.getContext().getAuthentication().getAuthorities());

                    UI.getCurrent().navigate(requestCache.resolveRedirectUrl());
                } catch (AuthenticationException ex) {
                    loginErrorLayout.setVisible(true);
                }
            }
        });

        forgotPasswordButton.addClickListener(clickEvent -> UI.getCurrent().navigate("restore"));
    }

    private boolean isLoginFormValid() {
        boolean isUsernameValid = !usernameField.isInvalid() && !usernameField.isEmpty() && !usernameField.getValue().contains(" ");
        boolean isPasswordValid = !passwordField.isInvalid() && !passwordField.isEmpty();

        if (isUsernameValid && isPasswordValid) {
            loginErrorLayout.setVisible(false);
            return true;
        } else {
            if (!isUsernameValid && !isUsernameValid) {
                usernameField.setInvalid(true);
                passwordField.setInvalid(true);
            }

            if (!isUsernameValid) {
                usernameField.setInvalid(true);
                usernameField.focus();
            }

            if (!isPasswordValid && isUsernameValid) {
                passwordField.setInvalid(true);
                passwordField.focus();
            }

            return false;
        }
    }
}
