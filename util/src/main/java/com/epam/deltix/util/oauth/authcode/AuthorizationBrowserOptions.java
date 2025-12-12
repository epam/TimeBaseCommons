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

import java.net.URI;

public class AuthorizationBrowserOptions {

    private String successMessage;

    private String errorMessage;

    private URI successRedirectUri;

    private URI errorRedirectUri;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public URI getSuccessRedirectUri() {
        return successRedirectUri;
    }

    public void setSuccessRedirectUri(URI successRedirectUri) {
        this.successRedirectUri = successRedirectUri;
    }

    public URI getErrorRedirectUri() {
        return errorRedirectUri;
    }

    public void setErrorRedirectUri(URI errorRedirectUri) {
        this.errorRedirectUri = errorRedirectUri;
    }
}
