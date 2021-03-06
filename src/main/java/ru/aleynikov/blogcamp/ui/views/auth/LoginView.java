package ru.aleynikov.blogcamp.ui.views.auth;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;
import ru.aleynikov.blogcamp.ui.views.main.HomeView;

import java.util.regex.Pattern;

@PageTitle("Log in")
@Route(value = "login", layout = AuthLayout.class)
@StyleSheet(StaticContent.LOGIN_STYLES)
public class LoginView extends Composite<Div> implements HasComponents {

    private static Logger log = LoggerFactory.getLogger(LoginView.class);

    @Autowired
    @Qualifier("regularExpForUsername")
    private Pattern regularExpUsername;

    @Autowired
    @Qualifier("regularExpForPassword")
    private Pattern regularExpPassword;

    private HorizontalLayout loginErrorLayout = new HorizontalLayout();

    private VerticalLayout loginFormLayout = new VerticalLayout();

    private H2 loginLabel = new H2("Log in");

    private Label errorLoginLabel = new Label("Invalid username or password.");

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Button loginButton = new Button("Log in");

    private RouterLink forgotPasswordButton = new RouterLink("Forgot password?", PasswordRestoreView.class);

    private RouterLink signUpLink = new RouterLink("Sign up", SignUpView.class);

    public LoginView(AuthenticationManager authenticationManager) {
        getContent().setSizeFull();

        loginErrorLayout.setSizeFull();
        loginErrorLayout.setClassName("error-login");
        loginErrorLayout.add(errorLoginLabel);
        loginErrorLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        loginErrorLayout.setVisible(false);

        errorLoginLabel.setClassName("error-label-login");

        loginFormLayout.setWidth("360px");
        loginFormLayout.setClassName("login-form");
        loginFormLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.START, loginLabel);
        loginFormLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        loginLabel.setClassName("login-label");

        usernameField.getElement().setAttribute("name", "username");
        usernameField.setLabel("Username");
        usernameField.setClassName("field");
        usernameField.setWidth("100%");
        usernameField.setMaxLength(30);
        usernameField.setMinLength(6);
        usernameField.setRequired(true);
        usernameField.setErrorMessage("Must be not empty and have minimal " + usernameField.getMinLength() + " length.");

        passwordField.getElement().setAttribute("name", "password");
        passwordField.setLabel("Password");
        passwordField.setClassName("field");
        passwordField.setWidth("100%");
        passwordField.setMaxLength(30);
        passwordField.setMinLength(8);
        passwordField.setRequired(true);
        passwordField.setErrorMessage("Must be not empty and have minimal " + passwordField.getMinLength() + " length.");


        loginButton.setClassName("button");

        loginFormLayout.add(loginErrorLayout, loginLabel, usernameField,
                passwordField, loginButton, forgotPasswordButton, signUpLink);

        add(loginFormLayout);

        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addClickListener(clickEvent -> {
            if (isLoginFormValid()) {
                try {
                    final Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(usernameField.getValue().strip(), passwordField.getValue().strip()));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("User was authenticated [{}] with authorities {}",
                            SecurityContextHolder.getContext().getAuthentication().getName(),
                            SecurityContextHolder.getContext().getAuthentication().getAuthorities());

                    UI.getCurrent().navigate(HomeView.class);
                } catch (AuthenticationException ex) {
                    loginErrorLayout.setVisible(true);
                }
            }
        });
    }

    private boolean isLoginFormValid() {
        boolean isUsernameValid = regularExpUsername
                .matcher(usernameField.getValue().strip())
                .matches();
        boolean isPasswordValid = regularExpPassword
                .matcher(passwordField.getValue().strip())
                .matches();

        if (!isUsernameValid)
            usernameField.setInvalid(true);

        if (!isPasswordValid)
            passwordField.setInvalid(true);

        return isUsernameValid && isPasswordValid;
    }
}
