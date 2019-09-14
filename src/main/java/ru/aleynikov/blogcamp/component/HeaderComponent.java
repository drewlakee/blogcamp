package ru.aleynikov.blogcamp.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.staticResources.StaticResources;


@StyleSheet(StaticResources.HEADER_COMPONENT_STYLES)
public class HeaderComponent extends HorizontalLayout {

    private HorizontalLayout mainSideLayout = new HorizontalLayout();

    private Image logoImage = new Image(StaticResources.LOGO_IMAGE, "logo");
    private Image userAvatarImage = new Image("", "avatar");

    private TextField searchField = new TextField();

    private Icon searchIcon = new Icon(VaadinIcon.SEARCH);
    private Icon logoutIcon = new Icon(VaadinIcon.SIGN_OUT);

    private Div userAvatarDiv = new Div();
    private Div logoDiv = new Div();
    private Div logoutDiv = new Div();


    public HeaderComponent() {
        setWidth("100%");
        setHeight("10px");
        setClassName("site-header");

        mainSideLayout.setWidth("1274px");
        mainSideLayout.getStyle().set("margin", "0 auto");

        logoImage.setClassName("logo-header");


        logoDiv.setTitle("Home");
        logoDiv.setClassName("div-component");
        logoDiv.getStyle().set("margin-left", "25px");
        logoDiv.add(logoImage);

        searchField.setPlaceholder("Global search");
        searchField.setClearButtonVisible(true);
        searchField.setAutoselect(true);
        searchField.setPrefixComponent(searchIcon);
        searchField.setClassName("search-textfield-header");
        setVerticalComponentAlignment(Alignment.CENTER, searchField);

        userAvatarImage.addClassName("user-avatar");
        userAvatarImage.setSrc(StaticResources.DEFAULT_USER_AVATAR);
        userAvatarImage.setTitle(SecurityUtils.getUsername());


        userAvatarDiv.addClassName("div-component");
        userAvatarDiv.addClassName("right-side-component");
        userAvatarDiv.add(userAvatarImage);

        logoutIcon.addClassName("icon");

        logoutDiv.setTitle("Logout");
        logoutDiv.addClassName("div-component");
        logoutDiv.add(logoutIcon);

        mainSideLayout.add(logoDiv, searchField, userAvatarDiv, logoutDiv);

        add(mainSideLayout);

        logoImage.addClickListener(imageClickEvent -> UI.getCurrent().navigate(""));
        logoutIcon.addClickListener(clickEvent -> SecurityUtils.destroySession());

        searchIcon.addClickListener(iconClickEvent -> searchFieldProcess());
        searchField.addKeyPressListener(Key.ENTER, keyEventListener -> searchFieldProcess());

        userAvatarDiv.addClickListener(event -> UI.getCurrent().navigate("profile/about"));
    }

    private void searchFieldProcess() {
        Notification.show(searchField.getValue());
    }
}