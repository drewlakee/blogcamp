package ru.aleynikov.blogcamp.view;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.staticComponents.StaticResources;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Page Not Found")
@StyleSheet(StaticResources.NOT_FOUND_PAGE_STYLES)
public class CustomNotFoundView extends RouteNotFoundError {
    private Image errorImage = new Image(StaticResources.ERROR404_IMAGE, "404");
    private VerticalLayout verticalLayout = new VerticalLayout();

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        errorImage.addClassName("image-block");
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(errorImage);

        UI.getCurrent().add(verticalLayout);

        return HttpServletResponse.SC_NOT_FOUND;
    }
}


