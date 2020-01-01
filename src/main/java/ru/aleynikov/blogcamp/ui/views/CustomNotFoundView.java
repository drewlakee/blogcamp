package ru.aleynikov.blogcamp.ui.views;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.*;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Page Not Found")
@StyleSheet(StaticContent.NOT_FOUND_PAGE_STYLES)
public class CustomNotFoundView extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        return HttpServletResponse.SC_NOT_FOUND;
    }
}


