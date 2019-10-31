package ru.aleynikov.blogcamp.views.auth;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.aleynikov.blogcamp.models.User;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.staticResources.RedditAvatars;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.HomeView;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


@PageTitle("Sign up")
@Route(value = "registration", layout = AuthLayout.class)
@StyleSheet(StaticResources.SIGN_UP_STYLES)
public class SignUpView extends HorizontalLayout {

    private static Logger log = LoggerFactory.getLogger(SignUpView.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private VerticalLayout signUpFormLayout = new VerticalLayout();

    private HorizontalLayout signUpErrorLayout = new HorizontalLayout();

    private Label errorUsernameAlreadyExistLabel = new Label("Username already exist.");

    private H2 signUpLabel = new H2("Sign up");

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private PasswordField repeatPasswordField = new PasswordField();
    private TextField secretQuestionField = new TextField();
    private TextField secretAnswerField = new TextField();

    private Button continueButton = new Button("Continue");
    private Button signUpButton = new Button("Sign Up");

    private RouterLink logInLink = new RouterLink("Log in", LoginView.class);

    private Map<String, Object> newUserData = new LinkedHashMap<>();

    public SignUpView(AuthenticationManager authenticationManager) {
        setSizeFull();

        errorUsernameAlreadyExistLabel.setClassName("error-label-signup");

        signUpErrorLayout.setSizeFull();
        signUpErrorLayout.setClassName("error-signup");
        signUpErrorLayout.add(errorUsernameAlreadyExistLabel);
        signUpErrorLayout.setVisible(false);

        signUpLabel.setClassName("signup-form-label");

        signUpFormLayout.setSizeFull();
        signUpFormLayout.setWidth("360px");
        signUpFormLayout.setClassName("signup-form");
        signUpFormLayout.setHorizontalComponentAlignment(Alignment.START, signUpLabel);
        signUpFormLayout.setAlignItems(Alignment.CENTER);

        usernameField.setLabel("Username");
        usernameField.setClassName("field");
        usernameField.setWidth("100%");
        usernameField.setMaxLength(20);
        usernameField.setMinLength(6);
        usernameField.setRequired(true);
        usernameField.setErrorMessage("Minimal length is " + usernameField.getMinLength() + " characters, without white spaces.");

        passwordField.setLabel("Password");
        passwordField.setClassName("field");
        passwordField.setWidth("100%");
        passwordField.setMaxLength(30);
        passwordField.setMinLength(8);
        passwordField.setRequired(true);
        passwordField.setErrorMessage("Minimal length of password is " + passwordField.getMinLength() + " characters.");

        repeatPasswordField.setLabel("Repeat password");
        repeatPasswordField.setClassName("field");
        repeatPasswordField.setWidth("100%");
        repeatPasswordField.setMaxLength(30);
        repeatPasswordField.setMinLength(8);
        repeatPasswordField.setRequired(true);

        continueButton.setClassName("button");

        secretQuestionField.setLabel("Your own secret question");
        secretQuestionField.setSuffixComponent(new Span("?"));
        secretQuestionField.setClassName("field");
        secretQuestionField.setWidth("100%");
        secretQuestionField.setMaxLength(40);
        secretQuestionField.setMinLength(5);
        secretQuestionField.setRequired(true);
        secretQuestionField.setVisible(false);
        secretQuestionField.setErrorMessage("Must contain at least two words.");

        secretAnswerField.setLabel("Your answer");
        secretAnswerField.setClassName("field");
        secretAnswerField.setWidth("100%");
        secretAnswerField.setMaxLength(20);
        secretAnswerField.setMinLength(2);
        secretAnswerField.setRequired(true);
        secretAnswerField.setVisible(false);
        secretAnswerField.setErrorMessage("Minimal length is " + secretAnswerField.getMinLength() + " characters.");

        signUpButton.setClassName("button");
        signUpButton.setVisible(false);

        signUpFormLayout.add(signUpErrorLayout, signUpLabel, usernameField,
                passwordField, repeatPasswordField, continueButton,
                secretQuestionField, secretAnswerField, signUpButton, logInLink);

        add(signUpFormLayout);

        continueButton.addClickShortcut(Key.ENTER).setEventPropagationAllowed(!signUpButton.isVisible());
        continueButton.addClickListener(clickEvent -> {
           if (isFormValid() && isUsernameUnique()) {
               usernameField.setVisible(false);
               passwordField.setVisible(false);
               repeatPasswordField.setVisible(false);
               continueButton.setVisible(false);

               secretQuestionField.setVisible(true);
               secretAnswerField.setVisible(true);
               signUpButton.setVisible(true);
           }
        });

        signUpButton.addClickShortcut(Key.ENTER).setEventPropagationAllowed(!continueButton.isVisible());
        signUpButton.addClickListener(clickEvent -> {
           if (isSecretQuestionValid()) {
               newUserData.put("username", usernameField.getValue().strip());
               newUserData.put("password", passwordEncoder.encode(passwordField.getValue().strip()));
               newUserData.put("secret_question", secretQuestionField.getValue().strip().replaceAll("/?", "") + "?");
               newUserData.put("secret_answer", passwordEncoder.encode(secretAnswerField.getValue().strip()));
               newUserData.put("avatar", RedditAvatars.getRandomAvatar());
               userService.saveUser(newUserData);

               log.info("User with username [{}] was successfully registered.", newUserData.get("username"));

               final Authentication authentication = authenticationManager
                       .authenticate(new UsernamePasswordAuthenticationToken(usernameField.getValue().strip(), passwordField.getValue().strip()));
               SecurityContextHolder.getContext().setAuthentication(authentication);
               log.info("User was authenticated [{}] with authorities {}",
                       SecurityContextHolder.getContext().getAuthentication().getName(),
                       SecurityContextHolder.getContext().getAuthentication().getAuthorities());

               UI.getCurrent().navigate(HomeView.class);
           }
        });
    }

    private boolean isFormValid() {
        boolean isUsernameValid = !usernameField.isInvalid() && !usernameField.isEmpty() && !usernameField.getValue().strip().contains(" ");
        boolean isPasswordValid = !passwordField.isInvalid() && !passwordField.isEmpty();
        boolean isPasswordRepeatValid = passwordField.getValue().equals(repeatPasswordField.getValue());

        if (isUsernameValid && isPasswordValid && isPasswordRepeatValid) {
            signUpErrorLayout.setVisible(false);
            return true;
        } else {
            if (!isUsernameValid) {
                usernameField.setInvalid(true);
                usernameField.focus();
            }

            if (!isPasswordValid && isUsernameValid) {
                passwordField.setInvalid(true);
                passwordField.focus();
            }

            if (!isPasswordRepeatValid && isPasswordValid) {
                repeatPasswordField.setInvalid(true);
                repeatPasswordField.focus();
            }

            return false;
        }
    }

    private boolean isUsernameUnique() {
        User existingUser = userService.findUserByUsername(usernameField.getValue().strip());

        if (existingUser == null)
            return true;
        else {
            signUpErrorLayout.setVisible(true);
            return false;
        }
    }

    private boolean isSecretQuestionValid() {
        boolean isQuestionValid = !secretQuestionField.isInvalid() && !secretQuestionField.isEmpty() && isHaveTwoWords(secretQuestionField.getValue().strip());
        boolean isAnswerValid = !secretAnswerField.isInvalid() && !secretAnswerField.isEmpty();

        // Warn: be carefully with using button.focus(), because was some problem with JS - TypeError: $0 is null;
        if (!isQuestionValid) {
            secretQuestionField.setInvalid(true);
            secretQuestionField.focus();
        }

        if (!isAnswerValid && isQuestionValid) secretAnswerField.focus();

        return isAnswerValid && isQuestionValid;
    }

    private boolean isHaveTwoWords(String string) {
        String[] words = string.split(" ");

        boolean isNotContainWhiteSpacesAfterWords = Arrays.stream(words).filter((x) -> x.equals("")).count() < 2;
        boolean isContainAtLeastTwoWords = Arrays.stream(words).filter((x) -> !x.equals("")).count() > 1;

        return isContainAtLeastTwoWords && isNotContainWhiteSpacesAfterWords;
    }
}
