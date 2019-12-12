package ru.aleynikov.blogcamp.web;

public class Html {

    public static String shieldHtml(String html) {
        return html.
                replaceAll("\n", " ").
                replaceAll("'", "\\\\'");
    }
}
