package ru.aleynikov.blogcamp.services;

import com.vaadin.flow.component.UI;

public class JavaScriptUtils {

    public static void scrollPageTop() {
        UI.getCurrent().getPage().executeJavaScript("window.scrollTo(top)");
    }

    public static void innerHtml(String elementId, String html) {
        UI.getCurrent().getPage().executeJavaScript("document.getElementById(\'" + elementId + "\').innerHTML = \'" + FilterDataManager.shieldHtml(html) + "\'");
    }

}
