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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.aleynikov.blogcamp.daoImpl.UserDaoImpl;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@PageTitle("Password restore")
@Route(value = "restore", layout = AuthLayout.class)
@StyleSheet(StaticResources.PASS_RESTORE_STYLES)
public class PasswordRestoreView extends HorizontalLayout {

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private VerticalLayout passRestoreFormLayout = new VerticalLayout();

    private HorizontalLayout passRestoreErrorLayout = new HorizontalLayout();

    private Label errorAccountNotExistLabel = new Label("This account doesn't exist.");
    private Label enterUsernameLabel = new Label("Enter your account username:");
    private Label answerOnQuestionLabel = new Label("Answer on your own question:");
    private Label questionLabel = new Label();
    private Label errorAnswerLabel = new Label("Answer is not right.");
    private Label changeNewPassLabel = new Label("Change password:");

    private H2 passRestoreLabel = new H2("Password restore");

    private TextField usernameField = new TextField();
    private TextField answerField = new TextField();

    private PasswordField newPassField = new PasswordField();
    private PasswordField repeatNewPassField = new PasswordField();

    private Button continueButton = new Button("Continue");
    private Button changePassButton = new Button("Change new password");

    private User existingAccount;

    private RouterLink backToLoginLink = new RouterLink("Back to Login", LoginView.class);

    public PasswordRestoreView() {
        setSizeFull();

        errorAccountNotExistLabel.setClassName("error-label-pass-restore");

        errorAnswerLabel.setClassName("error-label-pass-restore");

        passRestoreErrorLayout.setSizeFull();
        passRestoreErrorLayout.setClassName("error-pass-restore");
        passRestoreErrorLayout.add(errorAccountNotExistLabel, errorAnswerLabel);
        passRestoreErrorLayout.setVisible(false);

        passRestoreLabel.setClassName("pass-restore-form-label");

        passRestoreFormLayout.setSizeFull();
        passRestoreFormLayout.setWidth("360px");
        passRestoreFormLayout.setClassName("pass-restore-form");
        passRestoreFormLayout.setHorizontalComponentAlignment(Alignment.START, passRestoreLabel);
        passRestoreFormLayout.setHorizontalComponentAlignment(Alignment.START, enterUsernameLabel);
        passRestoreFormLayout.setHorizontalComponentAlignment(Alignment.START, answerOnQuestionLabel);
        passRestoreFormLayout.setHorizontalComponentAlignment(Alignment.START, changeNewPassLabel);
        passRestoreFormLayout.setHorizontalComponentAlignment(Alignment.CENTER, backToLoginLink);
        passRestoreFormLayout.setAlignItems(Alignment.CENTER);

        answerOnQuestionLabel.setVisible(false);

        questionLabel.setVisible(false);

        answerField.setVisible(false);
        answerField.setRequired(true);
        answerField.setLabel("Your own answer");
        answerField.setWidth("100%");

        usernameField.setLabel("Username");
        usernameField.addClassName("field");
        usernameField.setWidth("100%");
        usernameField.setMaxLength(30);
        usernameField.setMinLength(6);
        usernameField.setRequired(true);
        usernameField.setErrorMessage("Minimal length is " + usernameField.getMinLength() + " characters, without white spaces.");

        continueButton.setClassName("button");

        changeNewPassLabel.setVisible(false);

        newPassField.setWidth("100%");
        newPassField.setLabel("Your new password");
        newPassField.setVisible(false);
        newPassField.setClassName("field");
        newPassField.setMaxLength(30);
        newPassField.setMinLength(8);
        newPassField.setRequired(true);
        newPassField.setErrorMessage("Minimal length of password is " + newPassField.getMinLength() + " characters.");

        repeatNewPassField.setWidth("100%");
        repeatNewPassField.setLabel("Repeat new password");
        repeatNewPassField.setVisible(false);
        repeatNewPassField.setClassName("field");
        repeatNewPassField.setMaxLength(30);
        repeatNewPassField.setMinLength(8);
        repeatNewPassField.setRequired(true);

        changePassButton.setClassName("button");
        changePassButton.setVisible(false);

        passRestoreFormLayout.add(passRestoreErrorLayout, passRestoreLabel, changeNewPassLabel, answerOnQuestionLabel, enterUsernameLabel,
                questionLabel, answerField, usernameField, newPassField, repeatNewPassField, continueButton, changePassButton, backToLoginLink);

        add(passRestoreFormLayout);

        continueButton.addClickShortcut(Key.ENTER).setEventPropagationAllowed(!changePassButton.isVisible());
        continueButton.addClickListener(clickEvent -> {
            if (usernameField.isVisible() || answerField.isVisible()) {
                if (isInputFieldValid() && isAccountExist()) {
                    enterUsernameLabel.setVisible(false);
                    usernameField.setVisible(false);

                    answerOnQuestionLabel.setVisible(true);
                    questionLabel.setVisible(true);
                    answerField.setVisible(true);
                    questionLabel.setText(existingAccount.getSecretQuestion());

                    if (encoder.matches(answerField.getValue().strip(), existingAccount.getSecretAnswer())) {
                        passRestoreErrorLayout.setVisible(false);
                        answerOnQuestionLabel.setVisible(false);
                        errorAnswerLabel.setVisible(false);
                        questionLabel.setVisible(false);
                        answerField.setVisible(false);
                        continueButton.setVisible(false);

                    } else if (!answerField.isEmpty()) {
                        answerField.setInvalid(true);
                        passRestoreErrorLayout.setVisible(true);
                        errorAnswerLabel.setVisible(true);
                        errorAccountNotExistLabel.setVisible(false);

                    } else if (!usernameField.isVisible() && answerField.isEmpty())
                        answerField.focus();
                }
            }

            if (!usernameField.isVisible() && !answerField.isVisible()) {
                changeNewPassLabel.setVisible(true);
                newPassField.setVisible(true);
                repeatNewPassField.setVisible(true);
                changePassButton.setVisible(true);
            }
        });

        changePassButton.addClickShortcut(Key.ENTER).setEventPropagationAllowed(!continueButton.isVisible());
        changePassButton.addClickListener(clickEvent -> {
            if (isNewPasswordValid()) {
                userDao.updatePasswordByUsername(usernameField.getValue().strip(), encoder.encode(newPassField.getValue().strip()));
                UI.getCurrent().getUI().ifPresent(ui -> ui.navigate("login"));
            }
        });
    }

    private boolean isInputFieldValid() {
        boolean isUsernameValid = !usernameField.isInvalid() && !usernameField.isEmpty() && !usernameField.getValue().strip().contains(" ");

        if (!isUsernameValid) {
            usernameField.setInvalid(true);
            usernameField.focus();
        }

        return isUsernameValid;
    }

    private boolean isAccountExist() {
        if (existingAccount == null)
            existingAccount = userDao.findByUsername(usernameField.getValue().strip());

        if (existingAccount != null) {
            passRestoreErrorLayout.setVisible(false);
            return true;

        } else {
            passRestoreErrorLayout.setVisible(true);
            errorAnswerLabel.setVisible(false);
            return false;
        }
    }

    private boolean isNewPasswordValid() {
        boolean isNewPasswordValid = !newPassField.isInvalid() && !newPassField.isEmpty();
        boolean isNewPasswordRepeatValid = newPassField.getValue().equals(repeatNewPassField.getValue());

        if (isNewPasswordRepeatValid && isNewPasswordValid) {
            return true;
        } else {

            if (!isNewPasswordValid) {
                newPassField.setInvalid(true);
                newPassField.focus();
            }

            if (!isNewPasswordRepeatValid && isNewPasswordValid) {
                repeatNewPassField.setInvalid(true);
                repeatNewPassField.focus();
            }

            return false;
        }
    }
}
