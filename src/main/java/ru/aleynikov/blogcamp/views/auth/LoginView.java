package ru.aleynikov.blogcamp.views.auth;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.HomeView;

@PageTitle("Log in")
@Route("login")
@StyleSheet(StaticResources.LOGIN_STYLES)
public class LoginView extends HorizontalLayout {

    @Autowired
    private UserService userService;

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

    private RouterLink forgotPasswordButton = new RouterLink("Forgot password?", PasswordRestoreView.class);

    private RouterLink signUpLink = new RouterLink("Sign up", SignUpView.class);

    public LoginView(AuthenticationManager authenticationManager) {
        setSizeFull();

        loginLayout.setWidth(null);
        loginLayout.setClassName("login-layout");
        loginLayout.setAlignItems(Alignment.CENTER);

        logoImage.setClassName("logo-login");

        loginErrorLayout.setSizeFull();
        loginErrorLayout.setClassName("error-login");
        loginErrorLayout.add(errorLoginLabel);
        loginErrorLayout.setAlignItems(Alignment.CENTER);
        loginErrorLayout.setVisible(false);

        errorLoginLabel.setClassName("error-label-login");

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

        loginFormLayout.add(loginErrorLayout, loginLabel, usernameField,
                passwordField, loginButton, forgotPasswordButton, signUpLink);

        loginLayout.add(logoImage, loginFormLayout);

        setVerticalComponentAlignment(Alignment.CENTER, loginLayout);

        add(loginLayout);

        loginButton.addClickShortcut(Key.ENTER);
        loginButton.addClickListener(clickEvent -> {
            if (isLoginFormValid() && !isUserBanned()) {
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

    private boolean isUserBanned() {
        boolean isUserBanned = false;
        User user = userService.findUserByUsername(usernameField.getValue().strip());

        if (user != null) {
            isUserBanned = user.isBanned();
        }

        if (isUserBanned) {
            errorLoginLabel.setText("You are was banned.");
            loginErrorLayout.setVisible(true);
        }

        return isUserBanned;
    }

    private boolean isLoginFormValid() {
        boolean isUsernameValid = !usernameField.isInvalid() && !usernameField.isEmpty() && !usernameField.getValue().strip().contains(" ");
        boolean isPasswordValid = !passwordField.isInvalid() && !passwordField.isEmpty();

        if (isUsernameValid && isPasswordValid) {
            loginErrorLayout.setVisible(false);
            return true;
        } else {
            if (!isUsernameValid && !isPasswordValid) {
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
