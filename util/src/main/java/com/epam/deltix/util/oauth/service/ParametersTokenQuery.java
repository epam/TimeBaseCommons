package com.epam.deltix.util.oauth.service;

import java.util.HashMap;
import java.util.Map;

class ParametersTokenQuery implements TokenQuery {

    private final Map<String, String> parameters = new HashMap<>();

    ParametersTokenQuery(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}
