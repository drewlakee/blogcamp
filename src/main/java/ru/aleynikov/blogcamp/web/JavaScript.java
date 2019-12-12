package ru.aleynikov.blogcamp.web;

import com.vaadin.flow.component.UI;

public class JavaScript {

    public static void scrollPageTop() {
        UI.getCurrent().getPage().executeJavaScript("window.scrollTo(top)");
    }

    public static void innerHtml(String elementId, String html) {
        UI.getCurrent().getPage().executeJavaScript("document.getElementById(\'" + elementId + "\').innerHTML = \'" + Html.shieldHtml(html) + "\'");
    }

}
