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
package com.epam.deltix.util.oauth;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;

public class Oauth2CodeClientConfig {

    private String issuer;

    private String clientId;

    private int redirectPort = 4278;

    private String scope;

    private String usernameClaim = "preferred_username";

    private String authorizationEndpoint;

    private String tokenEndpoint;

    private boolean withPkce = false;

    private boolean validateState = true;

    private final Map<String, String> additionalParams = new HashMap<>();

    private int connectTimeoutMs = 10000;

    private int readTimeoutMs = 10000;

    private double expirationMultiplier = 0.8;

    private double refreshRetriesCount = 5;

    private Timer timer;

    private ScheduledExecutorService executor;

    public static Builder builder() {
        return new Oauth2CodeClientConfig().new Builder();
    }

    public String getIssuer() {
        return issuer;
    }

    public String getClientId() {
        return clientId;
    }

    public String getScope() {
        return scope;
    }

    public int getRedirectPort() {
        return redirectPort;
    }

    public String getUsernameClaim() {
        return usernameClaim;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public boolean isWithPkce() {
        return withPkce;
    }

    public boolean isValidateState() {
        return validateState;
    }

    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    public double getExpirationMultiplier() {
        return expirationMultiplier;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public double getRefreshRetriesCount() {
        return refreshRetriesCount;
    }

    public Timer getTimer() {
        return timer;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }


    public class Builder {
        private Builder() {
        }

        public Builder withIssuer(String issuer) {
            Oauth2CodeClientConfig.this.issuer = issuer;
            return this;
        }

        public Builder withClientId(String clientId) {
            Oauth2CodeClientConfig.this.clientId = clientId;
            return this;
        }

        public Builder withScope(String scope) {
            Oauth2CodeClientConfig.this.scope = scope;
            return this;
        }

        public Builder withRedirectPort(int redirectPort) {
            Oauth2CodeClientConfig.this.redirectPort = redirectPort;
            return this;
        }

        public Builder withUsernameClaim(String usernameClaim) {
            Oauth2CodeClientConfig.this.usernameClaim = usernameClaim;
            return this;
        }

        public Builder withAuthorizationEndpoint(String authorizationEndpoint) {
            Oauth2CodeClientConfig.this.authorizationEndpoint = authorizationEndpoint;
            return this;
        }

        public Builder withTokenEndpoint(String tokenEndpoint) {
            Oauth2CodeClientConfig.this.tokenEndpoint = tokenEndpoint;
            return this;
        }

        public Builder withPkce(boolean withPkce) {
            Oauth2CodeClientConfig.this.withPkce = withPkce;
            return this;
        }

        public Builder withValidateState(boolean validateState) {
            Oauth2CodeClientConfig.this.validateState = validateState;
            return this;
        }

        public Builder withAdditionalParam(String key, String value) {
            Oauth2CodeClientConfig.this.additionalParams.put(key, value);
            return this;
        }

        public Builder withExpirationMultiplier(double multiplier) {
            Oauth2CodeClientConfig.this.expirationMultiplier = multiplier;
            return this;
        }

        public Builder withRefreshRetriesCount(int count) {
            Oauth2CodeClientConfig.this.refreshRetriesCount = count;
            return this;
        }

        public Builder withTimer(Timer timer) {
            Oauth2CodeClientConfig.this.timer = timer;
            return this;
        }

        public Builder withExecutor(ScheduledExecutorService executor) {
            Oauth2CodeClientConfig.this.executor = executor;
            return this;
        }

        public Oauth2CodeClientConfig build() {
            return Oauth2CodeClientConfig.this;
        }

    }

}
