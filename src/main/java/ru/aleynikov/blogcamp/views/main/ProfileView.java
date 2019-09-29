package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@RoutePrefix(value = "profile")
@ParentLayout(MainLayout.class)
@PageTitle("Profile - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
@StyleSheet(StaticResources.PROFILE_VIEW_STYLES)
public class ProfileView extends Composite<Div> implements HasComponents, RouterLayout, BeforeEnterObserver {

    private User currentUser = SecurityUtils.getPrincipal();

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
    private Span userAboutSpan = new Span();

    private Image userAvatarImage = new Image("", "avatar");

    private MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private Upload avatarUpload = new Upload(buffer);

    private Button uploadAvatarButton = new Button("Upload avatar");

    private Div contentDiv = new Div();
    private Div uploadButtonDiv = new Div();

    private Tabs switchBar = new Tabs();
    private Tab aboutTab = new Tab("About");
    private Tab accountTab = new Tab("Account settings");

    public ProfileView() {
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

        avatarUpload.setDropAllowed(false);
        avatarUpload.setUploadButton(uploadAvatarButton);
        avatarUpload.setSizeFull();

        uploadButtonDiv.setSizeFull();
        uploadButtonDiv.addClassName("upload-btn-div");
        uploadButtonDiv.addClassName("margin-none");
        uploadButtonDiv.add(avatarUpload);

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
        switchBar.add(aboutTab, accountTab);

        switchLayout.add(switchBar);

        contentDiv.setSizeFull();
        contentDiv.addClassName("margin-none");

        contentLayout.add(userVerticalLayout, switchLayout, contentDiv);

        add(contentLayout);

        switchBar.addSelectedChangeListener(event -> {
            String selectedTab = event.getSource().getSelectedTab().getLabel();

            if (aboutTab.getLabel().equals(selectedTab)) {
                UI.getCurrent().navigate("profile/about");
            } else if (accountTab.getLabel().equals(selectedTab)) {
                UI.getCurrent().navigate("profile/account");
            }
        });
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

        userProfileLayoutBuild();
    }

    public void userProfileLayoutBuild() {
        if (currentUser.getAvatar() != null) {
            userAvatarImage.setSrc(currentUser.getAvatar());
        } else {
            userAvatarImage.addClassName("padding-trl-1px");
            userAvatarImage.setSrc(StaticResources.DEFAULT_USER_AVATAR);
        }

        infoLayout.removeAll();

        userUsernameSpan.setText(currentUser.getUsername());
        infoLayout.add(userUsernameSpan);

        if (currentUser.getFullName() != null) {
            userFullNameSpan.setText(currentUser.getFullName());
            infoLayout.add(userFullNameSpan);
        }

        if (currentUser.getCity() != null & currentUser.getCountry() != null) {
            userFromSpan.setText(currentUser.getCity() + ", " + currentUser.getCountry());
            infoLayout.add(userFromSpan);
        }

        if (currentUser.getBirthday() != null) {
            userBirthdaySpan.setText(currentUser.getBirthday().format(DateTimeFormatter.ofPattern("d MMMM YYYY", Locale.ENGLISH)) + " (" + (currYear - currentUser.getBirthday().getYear()) + " years old)");
            infoLayout.add(userBirthdaySpan);
        }

        if (currentUser.getAbout() != null) {
            userAboutSpan.setText(currentUser.getAbout());
            infoLayout.add(userAboutSpan);
        }
    }

}
