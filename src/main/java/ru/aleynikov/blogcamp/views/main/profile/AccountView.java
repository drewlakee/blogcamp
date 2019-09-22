package ru.aleynikov.blogcamp.views.main.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.ProfileView;

import java.util.Arrays;

@Route(value = "account", layout = ProfileView.class)
@PageTitle("Profile - Account")
@StyleSheet(StaticResources.PROFILE_VIEW_STYLES)
public class AccountView extends Composite<Div> implements HasComponents, BeforeEnterObserver {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserService userService;

    private User currentUser;

    private VerticalLayout mainLayout = new VerticalLayout();

    private TextField secretQuestionField = new TextField();
    private TextField secretAnswerField = new TextField();
    private TextField newPassField = new TextField();
    private TextField newPassRepeatField = new TextField();

    private Button changeSecretButton = new Button("Change");
    private Button changePassButton = new Button("Change password");

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

        newPassField.setWidth("100%");
        newPassField.setLabel("New password");
        newPassField.setMaxLength(60);
        newPassField.setMinLength(8);

        newPassRepeatField.setWidth("100%");
        newPassRepeatField.addClassName("margin-none");
        newPassRepeatField.setLabel("Repeat new password");
        newPassRepeatField.setMaxLength(60);
        newPassRepeatField.setMinLength(8);

        changePassButton.addClassName("main-button");

        mainLayout.add(secretQuestionField, secretAnswerField, changeSecretButton,
                newPassField, newPassRepeatField, changePassButton);

        add(mainLayout);

        changeSecretButton.addClickListener(event -> {
            if (isSecretQuestionValid()) {
                userService.updateUserSecret(secretQuestionField.getValue().trim(), encoder.encode(secretAnswerField.getValue().trim()), currentUser.getId());
            }
        });

        changePassButton.addClickListener(event -> {

        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        currentUser = SecurityUtils.getPrincipal();

        secretQuestionField.setValue(currentUser.getSecretQuestion());
    }

    private boolean isSecretQuestionValid() {
        boolean isQuestionValid = !secretQuestionField.isInvalid() && !secretQuestionField.isEmpty() && isHaveTwoWords(secretQuestionField.getValue().trim());
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
