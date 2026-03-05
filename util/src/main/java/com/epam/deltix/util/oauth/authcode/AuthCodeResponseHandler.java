/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.epam.deltix.util.oauth.authcode;

import com.epam.deltix.util.oauth.utils.Utils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

class AuthCodeResponseHandler implements HttpHandler {

    private static final String DEFAULT_SUCCESS_MESSAGE = "<html><head><title>Authentication Complete</title></head><body>Authentication complete. You can close the browser and return to the application.</body></html>";
    private static final String DEFAULT_SUCCESS_RESOURCE = "/deltix/util/oauth/success-login.html";

    private static final String DEFAULT_FAILURE_MESSAGE = "<html><head><title>Authentication Failed</title></head><body>Authentication failed. You can return to the application. Feel free to close this browser tab.</br></br></br></br> Error details: error {0} error_description: {1}</body></html>";
    private static final String DEFAULT_FAILURE_RESOURCE = "/deltix/util/oauth/failure-login.html";

    private final BlockingQueue<AuthCodeResult> authCodeResultQueue;

    private final AuthorizationBrowserOptions browserOptions;

    AuthCodeResponseHandler(BlockingQueue<AuthCodeResult> authCodeResultQueue,
                            AuthorizationBrowserOptions browserOptions) {

        this.authCodeResultQueue = authCodeResultQueue;
        this.browserOptions = browserOptions;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (!httpExchange.getRequestURI().getPath().equalsIgnoreCase("/")) {
                httpExchange.sendResponseHeaders(200, 0);
                return;
            }

            AuthCodeResult result = AuthCodeResult.fromResponseBody(
                extractResponseBody(httpExchange.getRequestURI())
            );
            sendResponse(httpExchange, result);
            authCodeResultQueue.put(result);
        } catch (Exception t) {
            throw new RuntimeException(t);
        } finally {
            httpExchange.close();
        }
    }

    private String extractResponseBody(URI uri) {
        String requestUri = uri.toString();
        int startParams = requestUri.indexOf("?");
        if (startParams >= 0) {
            return requestUri.substring(startParams + 1);
        }

        return requestUri;
    }

    private void sendResponse(HttpExchange httpExchange, AuthCodeResult result) throws IOException {
        switch (result.status()) {
            case Success:
                sendSuccessResponse(httpExchange, getSuccessfulResponseMessage());
                break;
            case ProtocolError:
            case UnknownError:
                sendErrorResponse(httpExchange, getErrorResponseMessage(result));
                break;
        }
    }

    private void sendSuccessResponse(HttpExchange httpExchange, String response) throws IOException {
        if (browserOptions == null || browserOptions.getSuccessRedirectUri() == null) {
            send200Response(httpExchange, response);
        } else {
            send302Response(httpExchange, browserOptions.getSuccessRedirectUri().toString());
        }
    }

    private void sendErrorResponse(HttpExchange httpExchange, String response) throws IOException {
        if (browserOptions == null || browserOptions.getErrorRedirectUri() == null) {
            send200Response(httpExchange, response);
        } else {
            send302Response(httpExchange, browserOptions.getErrorRedirectUri().toString());
        }
    }

    private void send302Response(HttpExchange httpExchange, String redirectUri) throws IOException {
        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Location", redirectUri);
        httpExchange.sendResponseHeaders(302, 0);
    }

    private void send200Response(HttpExchange httpExchange, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String getSuccessfulResponseMessage() {
        if (browserOptions == null || browserOptions.getSuccessMessage() == null) {
            return getDefaultSuccessMessage();
        }

        return browserOptions.getSuccessMessage();
    }

    private String getDefaultSuccessMessage() {
        try {
            return Utils.getResourceFileAsString(DEFAULT_SUCCESS_RESOURCE);
        } catch (Exception e) {
            return DEFAULT_SUCCESS_MESSAGE;
        }
    }

    private String getErrorResponseMessage(AuthCodeResult result) {
        if (browserOptions == null || browserOptions.getErrorMessage() == null) {
            return formatErrorMessage(getDefaultFailureMessage(), result);
        }

        return formatErrorMessage(browserOptions.getErrorMessage(), result);
    }

    private String getDefaultFailureMessage() {
        try {
            return Utils.getResourceFileAsString(DEFAULT_FAILURE_RESOURCE);
        } catch (Exception e) {
            return DEFAULT_FAILURE_MESSAGE;
        }
    }

    private String formatErrorMessage(String message, AuthCodeResult result) {
        try {
            return message
                .replace("{{result-error}}", result.error())
                .replace("{{result-error-description}}", result.errorDescription());
        } catch (Exception t) {
            return browserOptions.getErrorMessage();
        }
    }

    public BlockingQueue<AuthCodeResult> authorizationResultQueue() {
        return this.authCodeResultQueue;
    }

}
