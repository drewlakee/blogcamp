package ru.aleynikov.blogcamp.views.auth;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import ru.aleynikov.blogcamp.statics.StaticContent;

@StyleSheet(StaticContent.LOGIN_STYLES)
@StyleSheet(StaticContent.PASS_RESTORE_STYLES)
@StyleSheet(StaticContent.SIGN_UP_STYLES)
public class AuthLayout extends Composite<Div> implements HasComponents, RouterLayout {

    private Image logoImage = new Image(StaticContent.LOGO_IMAGE, "logo");

    private VerticalLayout loginLayout = new VerticalLayout();

    private HorizontalLayout mainLayout = new HorizontalLayout();

    private Div contentDiv = new Div();

    AuthLayout() {
        getContent().setSizeFull();

        mainLayout.setSizeFull();

        loginLayout.setWidth(null);
        loginLayout.getStyle().set("margin", "auto");
        loginLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        logoImage.setClassName("logo-login");

        loginLayout.add(logoImage, contentDiv);

        mainLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, loginLayout);
        mainLayout.add(loginLayout);

        add(mainLayout);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentDiv.getElement().appendChild(content.getElement());
    }
}
