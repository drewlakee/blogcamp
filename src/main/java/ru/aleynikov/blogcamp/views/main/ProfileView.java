package ru.aleynikov.blogcamp.views.main;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

@RoutePrefix(value = "profile")
@ParentLayout(MainLayout.class)
@PageTitle("Profile - Blogcamp")
@StyleSheet(StaticResources.MAIN_LAYOUT_STYLES)
public class ProfileView extends Composite<Div> implements HasComponents, RouterLayout {

    private VerticalLayout mainLayout = new VerticalLayout();
    private VerticalLayout userLayout = new VerticalLayout();
    private VerticalLayout switchLayout = new VerticalLayout();

    private Div contentDiv = new Div();

    private HorizontalLayout avatarLayout = new HorizontalLayout();
    private HorizontalLayout infoLayout = new HorizontalLayout();

    public ProfileView() {
        getContent().setSizeFull();

        mainLayout.setSizeFull();

        userLayout.setSizeFull();

        avatarLayout.setSizeFull();

        infoLayout.setSizeFull();

        userLayout.add(avatarLayout, infoLayout);

        switchLayout.setSizeFull();

        contentDiv.setSizeFull();

        mainLayout.add(userLayout, switchLayout, contentDiv);

        add(mainLayout);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        contentDiv.getElement().appendChild(content.getElement());
    }
}
