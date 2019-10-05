package ru.aleynikov.blogcamp.service;

import com.vaadin.flow.router.QueryParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParametersManager {

    public static QueryParameters queryParametersBuild(String sortTabLabel, int page, String filter) {
        Map<String, List<String>> qmap = new HashMap<>();
        List<String> param;
        QueryParameters qparams;

        if (!filter.isEmpty()) {
            param = new ArrayList<>();
            param.add(filter);
            qmap.put("search", param);
        } else if (sortTabLabel != null){
            param = new ArrayList<>();
            param.add(sortTabLabel);
            qmap.put("tab", param);
        }

        param = new ArrayList<>();
        param.add((page == 0) ? "1" : String.valueOf(page));

        qmap.put("page", param);

        qparams = new QueryParameters(qmap);

        return qparams;
    }

    public static QueryParameters querySearchParametersBuild(String filter) {
        return queryParametersBuild(null, 0, filter);
    }

    public static QueryParameters queryParametersBuild(String sortTabLabel) {
        return queryParametersBuild(sortTabLabel, 0, "");
    }

    public static Map<String, List<String>> queryParametersBuild(int page, String sortTab) {
        Map<String, List<String>> qmap = new HashMap<>();
        List<String> param;

        param = new ArrayList<>();
        param.add(String.valueOf(page));
        qmap.put("page", param);

        if (sortTab != null) {
            param = new ArrayList<>();
            param.add(sortTab);
            qmap.put("tab", param);
        }

        return qmap;
    }

    public static Map<String, List<String>> queryParametersBuild(int page) {
        return queryParametersBuild(page, null);
    }
}
