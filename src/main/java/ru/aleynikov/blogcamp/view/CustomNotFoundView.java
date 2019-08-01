package ru.aleynikov.blogcamp.view;


import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.StaticResources;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Page Not Found")
public class CustomNotFoundView extends RouteNotFoundError {
    private Image errorImage = new Image(StaticResources.ERROR404_IMAGE, "404");

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        return HttpServletResponse.SC_NOT_FOUND;
    }
}


