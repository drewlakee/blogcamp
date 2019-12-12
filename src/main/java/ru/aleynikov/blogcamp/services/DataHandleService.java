package ru.aleynikov.blogcamp.services;

public class DataHandleService {

    public static int filterOffset(int page, int componentsOnPageLimit) {
        int offset = 0;

        if (page == 1 || page < 0)
            page = 0;

        if (page > 0) {
            offset += componentsOnPageLimit * (page - 1);
        }

        return offset;
    }

    public static int calculatePageLimit(float countOfComponents, int limitOfComponentsOnPage) {
        int pageLimit = Math.round((countOfComponents + limitOfComponentsOnPage / 2) / limitOfComponentsOnPage);

        if (countOfComponents != limitOfComponentsOnPage)
            return pageLimit;
        else
            return pageLimit - 1;
    }
}
