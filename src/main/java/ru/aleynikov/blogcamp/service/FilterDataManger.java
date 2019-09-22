package ru.aleynikov.blogcamp.service;

public class FilterDataManger {

    public static int filterOffset(int page, int componentsOnPageLimit) {
        int offset = 0;

        if (page == 1 || page < 0)
            page = 0;

        if (page > 0) {
            offset += componentsOnPageLimit * (page - 1);
        }

        return offset;
    }

    public static int pageLimit(float countOfComponents, int limitOfComponentsOnPage) {
        return Math.round((countOfComponents + limitOfComponentsOnPage / 2) / limitOfComponentsOnPage);
    }

    public static int pageLimit(int countOfComponents, int limitOfComponentsOnPage) {
        return pageLimit(Float.valueOf(countOfComponents), limitOfComponentsOnPage);
    }
}
