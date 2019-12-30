package ru.aleynikov.blogcamp.ui.views.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

import java.util.Arrays;

@Route(value = "account", layout = ProfileLayout.class)
@PageTitle("Profile - Account")
@StyleSheet(StaticContent.PROFILE_STYLES)
public class AccountView extends Composite<Div> implements HasComponents, BeforeEnterObserver {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserService userService;

    private User currentUser;

    private VerticalLayout mainLayout = new VerticalLayout();

    private HorizontalLayout secretHorizontalLayout = new HorizontalLayout();
    private HorizontalLayout passHorizontalLayout = new HorizontalLayout();

    private TextField secretQuestionField = new TextField();
    private TextField secretAnswerField = new TextField();

    private PasswordField newPassField = new PasswordField();
    private PasswordField newPassRepeatField = new PasswordField();

    private Button changeSecretButton = new Button("Change");
    private Button changePassButton = new Button("Change password");

    private Span successfullySecretUpdate = new Span("Successfully changed!");
    private Span successfullyPassUpdate = new Span("Password successfully changed!");

    public AccountView() {
        mainLayout.setSizeFull();

        secretQuestionField.setWidth("100%");
        secretQuestionField.setLabel("Your secret question");
        secretQuestionField.setMinLength(3);
        secretQuestionField.setMaxLength(30);
        secretQuestionField.setErrorMessage("Must contain at least two words.");

        secretAnswerField.setWidth("100%");
        secretAnswerField.setLabel("Your new answer");
        secretAnswerField.addClassName("margin-none");
        secretAnswerField.setMinLength(2);
        secretAnswerField.setMaxLength(30);
        secretAnswerField.setErrorMessage("Minimal length is " + secretAnswerField.getMinLength() + " characters.");

        changeSecretButton.addClassName("main-button");

        successfullySecretUpdate.addClassName("success");
        successfullySecretUpdate.setVisible(false);
        secretHorizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, successfullySecretUpdate);

        secretHorizontalLayout.setWidth("100%");
        secretHorizontalLayout.add(changeSecretButton, successfullySecretUpdate);

        newPassField.setWidth("100%");
        newPassField.setLabel("New password");
        newPassField.setMaxLength(60);
        newPassField.setMinLength(8);
        newPassField.setErrorMessage("Minimal length of password is " + newPassField.getMinLength() + " characters.");

        newPassRepeatField.setWidth("100%");
        newPassRepeatField.addClassName("margin-none");
        newPassRepeatField.setLabel("Repeat new password");
        newPassRepeatField.setMaxLength(60);
        newPassRepeatField.setMinLength(8);

        changePassButton.addClassName("main-button");

        successfullyPassUpdate.addClassName("success");
        successfullyPassUpdate.setVisible(false);
        passHorizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, successfullyPassUpdate);

        passHorizontalLayout.setWidth("100%");
        passHorizontalLayout.add(changePassButton, successfullyPassUpdate);

        mainLayout.add(secretQuestionField, secretAnswerField, secretHorizontalLayout,
                newPassField, newPassRepeatField, passHorizontalLayout);

        add(mainLayout);

        changeSecretButton.addClickListener(event -> {
            if (isSecretQuestionValid()) {
                userService.updateUserSecret(secretQuestionField.getValue().strip(), encoder.encode(secretAnswerField.getValue().strip()), currentUser.getId());
                successfullySecretUpdate.setVisible(true);
            }
        });

        changePassButton.addClickListener(event -> {
            if (isNewPasswordValid()) {
                userService.updateUserPassword(currentUser.getUsername(), encoder.encode(newPassField.getValue().strip()));
                successfullyPassUpdate.setVisible(true);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        currentUser = SecurityUtils.getPrincipal();

        secretQuestionField.setValue(currentUser.getSecretQuestion());
    }

    private boolean isSecretQuestionValid() {
        boolean isQuestionValid = !secretQuestionField.isInvalid() && !secretQuestionField.isEmpty() && isHaveTwoWords(secretQuestionField.getValue().strip());
        boolean isAnswerValid = !secretAnswerField.isInvalid() && !secretAnswerField.isEmpty();

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

    private boolean isNewPasswordValid() {
        boolean isNewPasswordValid = !newPassField.isInvalid() && !newPassField.isEmpty();
        boolean isNewPasswordRepeatValid = newPassField.getValue().equals(newPassRepeatField.getValue());

        if (isNewPasswordRepeatValid && isNewPasswordValid) {
            return true;
        } else {

            if (!isNewPasswordValid) {
                newPassField.setInvalid(true);
                newPassField.focus();
            }

            if (!isNewPasswordRepeatValid && isNewPasswordValid) {
                newPassRepeatField.setInvalid(true);
                newPassRepeatField.focus();
            }

            return false;
        }
    }
}
