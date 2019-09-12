package ru.aleynikov.blogcamp.service;

import com.vaadin.flow.component.UI;

public class JavaScriptUtils {

    public static void scrollPageTop() {
        UI.getCurrent().getPage().executeJavaScript("window.scrollTo(top)");
    }
}
