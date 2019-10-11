package ru.aleynikov.blogcamp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
