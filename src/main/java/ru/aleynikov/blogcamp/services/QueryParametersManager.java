package ru.aleynikov.blogcamp.services;

import java.util.*;

public class QueryParametersManager {

    public static Map<String, List<String>> buildQueryParams(HashMap<String, Object> parameters) {
        Map<String, List<String>> qmap = new HashMap<>();
        List<String> param;

        for (String key : parameters.keySet()) {
            param = new ArrayList<>();
            param.add(parameters.get(key).toString());
            qmap.put(key, param);
        }

        return qmap;
    }

    public static void setQueryParams(Map<String, List<String>> qparams, HashMap<String, Object> pageParametersMap, Set<String> pageParametersKeySet) {

        for (String parameter : pageParametersKeySet) {
            if (!parameter.equals("page")) {
                pageParametersMap.replace(parameter, "");
            } else
                pageParametersMap.replace(parameter, "1");
        }

        for (String parameter : pageParametersKeySet) {
            if (qparams.containsKey(parameter)) {
                pageParametersMap.replace(parameter, qparams.get(parameter).get(0));
            }
        }
    }
}
