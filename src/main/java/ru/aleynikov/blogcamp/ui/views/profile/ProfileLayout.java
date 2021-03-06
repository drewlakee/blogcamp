package ru.aleynikov.blogcamp.ui.views.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.domain.models.City;
import ru.aleynikov.blogcamp.domain.models.Country;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.security.SessionState;
import ru.aleynikov.blogcamp.ui.web.JavaScript;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;
import ru.aleynikov.blogcamp.ui.views.main.MainLayout;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@RoutePrefix(value = "profile")
@ParentLayout(MainLayout.class)
@PageTitle("Profile - Blogcamp")
@StyleSheet(StaticContent.MAIN_STYLES)
@StyleSheet(StaticContent.PROFILE_STYLES)
public class ProfileLayout extends Composite<Div> implements HasComponents, RouterLayout, BeforeEnterObserver {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionState sessionState;

    private User userInSession = SecurityUtils.getPrincipal();

    private int currYear = Calendar.getInstance().get(Calendar.YEAR);

    private VerticalLayout contentLayout = new VerticalLayout();
    private VerticalLayout userVerticalLayout = new VerticalLayout();
    private VerticalLayout switchLayout = new VerticalLayout();
    private VerticalLayout avatarLayout = new VerticalLayout();
    private VerticalLayout infoLayout = new VerticalLayout();

    private HorizontalLayout userHorizontalLayout = new HorizontalLayout();

    private Span userUsernameSpan = new Span();
    private Span userFullNameSpan = new Span();
    private Span userFromSpan = new Span();
    private Span userBirthdaySpan = new Span();
    private Span userStatusSpan = new Span();

    private Image userAvatarImage = new Image("", "avatar");

    private Button uploadAvatarButton = new Button("Upload avatar");
    private Button setExternalAvatarButton = new Button("Update");

    private Div contentDiv = new Div();
    private Div uploadButtonDiv = new Div();
    private Div uploadExternalImageDiv = new Div();

    private Dialog externalImageSetDialog = new Dialog();

    private TextField externalImageSourceField = new TextField();

    private Tabs switchBar = new Tabs();
    private Tab postsTab = new Tab("Posts");
    private Tab aboutTab = new Tab("About");
    private Tab accountTab = new Tab("Account settings");

    public ProfileLayout() {
        getContent().setSizeFull();

        contentLayout.setSizeFull();

        userVerticalLayout.setWidth("100%");

        userHorizontalLayout.setSizeFull();

        avatarLayout.addClassName("profile-avatar");
        avatarLayout.addClassName("margin-none");
        avatarLayout.addClassName("padding-none");
        avatarLayout.setWidth(null);

        userAvatarImage.addClassName("avatar-img");

        uploadAvatarButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        uploadAvatarButton.setSizeFull();
        uploadAvatarButton.addClassName("margin-t-none");

        uploadButtonDiv.setWidth("100%");
        uploadButtonDiv.addClassName("upload-btn-div");
        uploadButtonDiv.addClassName("margin-none");

        uploadButtonDiv.add(uploadAvatarButton);

        avatarLayout.add(userAvatarImage, uploadButtonDiv);

        infoLayout.setSizeFull();
        infoLayout.addClassName("margin-none");
        infoLayout.setWidth("50%");

        userUsernameSpan.addClassName("username");

        userFullNameSpan.addClassName("grey-light");
        userFullNameSpan.addClassName("margin-none");
        userFullNameSpan.addClassName("padding-l-2px");

        userFromSpan.addClassName("grey-light");
        userFromSpan.addClassName("margin-none");
        userFromSpan.addClassName("padding-l-2px");

        userBirthdaySpan.addClassName("grey-light");
        userBirthdaySpan.addClassName("margin-none");
        userBirthdaySpan.addClassName("padding-l-2px");

        userHorizontalLayout.add(avatarLayout, infoLayout);

        userVerticalLayout.add(userHorizontalLayout);

        switchLayout.setWidth("100%");
        switchLayout.addClassName("switch");
        switchLayout.addClassName("padding-none");

        switchBar.addClassName("tabs-bar");
        switchBar.add(postsTab, aboutTab, accountTab);

        switchLayout.add(switchBar);

        contentDiv.setSizeFull();
        contentDiv.addClassName("margin-none");

        contentLayout.add(userVerticalLayout, switchLayout, contentDiv);

        add(contentLayout);

        switchBar.addSelectedChangeListener(event -> {
            String selectedTab = event.getSource().getSelectedTab().getLabel();

            if (postsTab.getLabel().equals(selectedTab)) {
                UI.getCurrent().navigate("profile/posts");
            } else if (aboutTab.getLabel().equals(selectedTab)) {
                UI.getCurrent().navigate("profile/about");
            } else if (accountTab.getLabel().equals(selectedTab)) {
                UI.getCurrent().navigate("profile/account");
            }
        });

        uploadExternalImageDiv.setId("external-img");

        externalImageSourceField.addClassName("external-img-field");
        externalImageSourceField.setRequired(true);
        externalImageSourceField.setPlaceholder("(https://image.png etc.)");
        externalImageSourceField.setMinLength(4);
        externalImageSourceField.setMaxLength(500);
        externalImageSourceField.setErrorMessage("Must be external link with correct format: jpg, jpeg, png, gif.");

        setExternalAvatarButton.addClassName("main-button");
        setExternalAvatarButton.addClassName("margin-l-10px");
        setExternalAvatarButton.setVisible(false);

        externalImageSetDialog.add(uploadExternalImageDiv, externalImageSourceField, setExternalAvatarButton);

        uploadAvatarButton.addClickListener(event -> {
           externalImageSetDialog.open();
        });

        externalImageSourceField.addInputListener(event -> {
           externalImageSourceField.setLabel("Tap on form for load picture");
        });

        externalImageSourceField.addValueChangeListener(event -> {
            if (!externalImageSourceField.isEmpty() && isExternalSourceValid()) {
                setExternalAvatarButton.setVisible(true);

                int imgWidth = 200;
                JavaScript.innerHtml(uploadExternalImageDiv.getId().get(), "<img style=\"width:" + imgWidth + "px\" src=" + externalImageSourceField.getValue().strip() + ">");
                externalImageSourceField.setLabel("");
            } else
                setExternalAvatarButton.setVisible(false);
        });

        setExternalAvatarButton.addClickListener(event -> {
            userService.updateUserAvatarByUserId(externalImageSourceField.getValue().strip(), userInSession.getId());
            externalImageSetDialog.close();
            externalImageSourceField.clear();

            sessionState.updateUserPrincipals();

            userAvatarImage.setSrc(SecurityUtils.getPrincipal().getAvatar());

            Notification.show("Avatar was successfully updated.");
        });

        externalImageSetDialog.addDialogCloseActionListener(event -> {
            externalImageSetDialog.close();
            setExternalAvatarButton.setVisible(false);
            externalImageSourceField.setInvalid(false);
            JavaScript.innerHtml(uploadExternalImageDiv.getId().get(), "");
        });
    }

    private boolean isExternalSourceValid() {
        String sourceValue = externalImageSourceField.getValue().toLowerCase();
        boolean isFieldNotEmpty = !sourceValue.isEmpty();
        boolean isSourceExternal = sourceValue.startsWith("http") && sourceValue.contains("://");
        boolean isSourceImage = sourceValue.endsWith("png") || sourceValue.endsWith("jpg") ||
                sourceValue.endsWith("gif") || sourceValue.endsWith("jpeg");

        if (!isFieldNotEmpty || !isSourceExternal || !isSourceImage) {
            externalImageSourceField.setInvalid(true);
            JavaScript.innerHtml(uploadExternalImageDiv.getId().get(), "");
        }

        return isSourceExternal && isSourceImage && isFieldNotEmpty;
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentDiv.getElement().appendChild(content.getElement());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getPath().endsWith("about"))
            switchBar.setSelectedTab(aboutTab);
        else if (event.getLocation().getPath().endsWith("account"))
            switchBar.setSelectedTab(accountTab);

        setUserCurrentProfileInfo(userInSession);
    }

    public void setUserCurrentProfileInfo(User user) {
        userAvatarImage.setSrc(user.getAvatar());

        infoLayout.removeAll();

        userUsernameSpan.setText(user.getUsername());
        infoLayout.add(userUsernameSpan);

        if (user.getFullName().isPresent()) {
            userFullNameSpan.setText(user.getFullName().get());
            infoLayout.add(userFullNameSpan);
        }

        if (user.getCity().isPresent() & user.getCountry().isPresent()) {
            userFromSpan.setText(user.getCity().map(City::getName).get() + ", " + user.getCountry().map(Country::getName).get());
            infoLayout.add(userFromSpan);
        }

        if (user.getBirthday().isPresent()) {
            userBirthdaySpan.setText(user.getBirthday().get().toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM YYYY", Locale.ENGLISH)) + " (" + (currYear - user.getBirthday().get().toLocalDate().getYear()) + " years old)");
            infoLayout.add(userBirthdaySpan);
        }

        if (user.getStatus().isPresent()) {
            userStatusSpan.setText(user.getStatus().get());
            infoLayout.add(userStatusSpan);
        }
    }

}
