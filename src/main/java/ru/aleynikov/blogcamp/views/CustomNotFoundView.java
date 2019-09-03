package ru.aleynikov.blogcamp.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.HomeView;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Page Not Found")
@StyleSheet(StaticResources.NOT_FOUND_PAGE_STYLES)
public class CustomNotFoundView extends RouteNotFoundError implements BeforeLeaveObserver {

    private Image errorContent = new Image(StaticResources.CODE404_IMAGE, "404");

    private VerticalLayout mainLayout = new VerticalLayout();

    private HorizontalLayout childLayout = new HorizontalLayout();

    private RouterLink returnToHomeLabel = new RouterLink("Return to Home", HomeView.class);

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        mainLayout.setVisible(true);

        mainLayout.setSizeFull();
        mainLayout.addClassName("background");

        childLayout.setSizeFull();
        childLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, errorContent);
        childLayout.add(errorContent);
        errorContent.setClassName("image404");

        returnToHomeLabel.addClassName("return-label");
        mainLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, returnToHomeLabel);

        mainLayout.add(childLayout, returnToHomeLabel);

        UI.getCurrent().add(mainLayout);

        return HttpServletResponse.SC_NOT_FOUND;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        mainLayout.setVisible(false);
    }
}


