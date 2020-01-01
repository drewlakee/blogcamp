package ru.aleynikov.blogcamp.ui.web;

public class Html {

    public static String shieldHtml(String html) {
        return html.
                replaceAll("\n", " ").
                replaceAll("'", "\\\\'");
    }
}
