package ru.aleynikov.blogcamp.views;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.staticResources.StaticResources;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Page Not Found")
@StyleSheet(StaticResources.NOT_FOUND_PAGE_STYLES)
public class CustomNotFoundView extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        return HttpServletResponse.SC_NOT_FOUND;
    }


}


