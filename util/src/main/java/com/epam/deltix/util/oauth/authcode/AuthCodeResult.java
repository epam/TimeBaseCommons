package com.epam.deltix.util.oauth.authcode;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

class AuthCodeResult {
    private String code;
    private String state;
    private AuthorizationStatus status;
    private String error;
    private String errorDescription;

    enum AuthorizationStatus {
        Success, ProtocolError, UnknownError;
    }

    static AuthCodeResult fromResponseBody(String responseBody) {
        if (isBlank(responseBody)) {
            return new AuthCodeResult(AuthorizationStatus.UnknownError, "Error",
                "The authorization server returned an invalid response: response is null or empty"
            );
        }

        Map<String, String> queryParameters = parseParameters(responseBody);
        if (queryParameters.containsKey("error")) {
            return new AuthCodeResult(AuthorizationStatus.ProtocolError, queryParameters.get("error"),
                !isBlank(queryParameters.get("error_description")) ? queryParameters.get("error_description") : null
            );
        }
        if (!queryParameters.containsKey("code")) {
            return new AuthCodeResult(AuthorizationStatus.UnknownError, "Error",
                "Authorization result response does not contain authorization code"
            );
        }

        AuthCodeResult result = new AuthCodeResult();
        result.code = queryParameters.get("code");
        result.status = AuthorizationStatus.Success;
        if (queryParameters.containsKey("state")) {
            result.state = queryParameters.get("state");
        }

        return result;
    }

    private AuthCodeResult() {
    }

    private AuthCodeResult(AuthorizationStatus status, String error, String errorDescription) {
        this.status = status;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    private static Map<String, String> parseParameters(String serverResponse) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        try {
            String[] pairs = serverResponse.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                query_pairs.put(key, value);
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Error parsing authorization result:  %s", ex.getMessage()));
        }

        return query_pairs;
    }

    String code() {
        return this.code;
    }

    String state() {
        return this.state;
    }

    AuthorizationStatus status() {
        return this.status;
    }

    String error() {
        return this.error;
    }

    String errorDescription() {
        return errorDescription;
    }

    AuthCodeResult code(final String code) {
        this.code = code;
        return this;
    }

    AuthCodeResult state(final String state) {
        this.state = state;
        return this;
    }

    AuthCodeResult status(final AuthorizationStatus status) {
        this.status = status;
        return this;
    }

    AuthCodeResult error(final String error) {
        this.error = error;
        return this;
    }

    AuthCodeResult errorDescription(final String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }
    private static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
