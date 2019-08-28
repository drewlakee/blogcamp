package ru.aleynikov.blogcamp.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Page Not Found")
@StyleSheet(StaticResources.NOT_FOUND_PAGE_STYLES)
public class CustomNotFoundView extends RouteNotFoundError {
    private Image errorImage = new Image(StaticResources.CODE404_IMAGE, "404");
    private HorizontalLayout layout = new HorizontalLayout();

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        layout.addClassName("background");
        layout.setSizeFull();
        layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, errorImage);
        layout.add(errorImage);
        errorImage.setClassName("image404");

        UI.getCurrent().add(layout);

        return HttpServletResponse.SC_NOT_FOUND;
    }
}


