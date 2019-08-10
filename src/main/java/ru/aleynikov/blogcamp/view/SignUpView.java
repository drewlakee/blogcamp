package ru.aleynikov.blogcamp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import ru.aleynikov.blogcamp.staticResources.StaticResources;



@PageTitle("Sign up")
@Route("registration")
@StyleSheet(StaticResources.SIGNUP_VIEW_STYLES)
public class SignUpView extends HorizontalLayout {

    private VerticalLayout signUpLayout = new VerticalLayout();
    private VerticalLayout signUpFormLayout = new VerticalLayout();
    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");
    private H2 signUpLabel = new H2("Sign up");

    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private PasswordField repeatPasswordField = new PasswordField();
    private TextField secretQuestionField = new TextField();
    private TextField secretAnswerField = new TextField();

    private Button continueButton = new Button("Continue");
    private Button signUpButton = new Button("Sign Up");

    private RouterLink LogInLink = new RouterLink("Log in", LogInView.class);

    public SignUpView() {
        signUpLayout.setSizeFull();

        logoImage.setClassName("logo-signup");

        signUpLayout.setClassName("signup-layout");
        signUpLayout.setAlignItems(Alignment.CENTER);

        signUpFormLayout.setSizeFull();
        signUpFormLayout.setWidth("360px");
        signUpFormLayout.setClassName("signup-form");

        signUpFormLayout.setHorizontalComponentAlignment(Alignment.START, signUpLabel);
        signUpFormLayout.setAlignItems(Alignment.CENTER);

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
        secretQuestionField.setMinLength(12);
        secretQuestionField.setRequired(true);
        secretQuestionField.setVisible(false);

        secretAnswerField.setLabel("Your answer");
        secretAnswerField.setClassName("field");
        secretAnswerField.setWidth("100%");
        secretAnswerField.setMaxLength(20);
        secretAnswerField.setMinLength(3);
        secretAnswerField.setRequired(true);
        secretAnswerField.setVisible(false);

        signUpButton.setClassName("button");
        signUpButton.setVisible(false);

        signUpFormLayout.add(signUpLabel, usernameField,
                passwordField, repeatPasswordField, continueButton,
                secretQuestionField, secretAnswerField, signUpButton, LogInLink);

        signUpLayout.add(logoImage, signUpFormLayout);

        setVerticalComponentAlignment(Alignment.CENTER, signUpLayout);

        add(signUpLayout);
    }
}
