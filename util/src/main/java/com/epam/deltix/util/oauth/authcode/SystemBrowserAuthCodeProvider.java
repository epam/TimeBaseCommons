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

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class SystemBrowserAuthCodeProvider implements AuthCodeProvider {

    private final int redirectPort;
    private final int authorizationTimeoutSec;

    private final HttpListener httpListener = new HttpListener();
    private final LinkedBlockingQueue<AuthCodeResult> resultQueue;

    SystemBrowserAuthCodeProvider(int redirectPort) {
        this(redirectPort, 60);
    }

    SystemBrowserAuthCodeProvider(int redirectPort, int authorizationTimeoutSec) {
        this.redirectPort = redirectPort;
        this.authorizationTimeoutSec = authorizationTimeoutSec;
        this.resultQueue = startHttpListener(httpListener);
    }

    @Override
    public AuthCodeResult requestCode(String authorizationUrl) {
        openDefaultSystemBrowser(authorizationUrl);
        return getAuthorizationResultFromHttpListener(resultQueue);
    }

    @Override
    public int redirectPort() {
        return httpListener.port();
    }

    @Override
    public void close() {
        httpListener.stopListener();
    }

    private LinkedBlockingQueue<AuthCodeResult> startHttpListener(HttpListener httpListener) {
        LinkedBlockingQueue<AuthCodeResult> authCodeResultQueue = new LinkedBlockingQueue<>();
        AuthCodeResponseHandler authCodeResponseHandler =
            new AuthCodeResponseHandler(authCodeResultQueue, null);

        int port = redirectPort == -1 ? 0 : redirectPort;
        httpListener.startListener(port, authCodeResponseHandler);
        return authCodeResultQueue;
    }

    private void openDefaultSystemBrowser(String authorizationUrl) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URL(authorizationUrl).toURI());
            } else {
                throw new RuntimeException("Unable to open default system browser.");
            }
        } catch (URISyntaxException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private AuthCodeResult getAuthorizationResultFromHttpListener(LinkedBlockingQueue<AuthCodeResult> authCodeResultQueue) {
        AuthCodeResult result = null;
        try {
            long expirationTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + authorizationTimeoutSec;
            while (result == null && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) < expirationTime) {
                result = authCodeResultQueue.poll(100, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (result == null || result.code() == null || result.code().isEmpty()) {
            throw new RuntimeException("No Authorization code was returned from the server");
        }
        return result;
    }
}
