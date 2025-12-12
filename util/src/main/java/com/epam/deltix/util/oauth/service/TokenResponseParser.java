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

package com.epam.deltix.util.oauth.service;

import com.epam.deltix.util.lang.StringUtils;
import com.epam.deltix.util.oauth.AuthResult;
import io.github.green4j.jelly.JsonNumber;
import io.github.green4j.jelly.JsonParser;
import io.github.green4j.jelly.JsonParserListener;

class TokenResponseParser {

    private final JsonParser parser = new JsonParser();
    private final Listener listener = new Listener();

    public TokenResponseParser() {
        parser.setListener(listener);
    }

    synchronized AuthResult parse(String clientId, String response) {
        parser.parseAndEoj(response);
        if (listener.error != null) {
            throw new RuntimeException("Failed to parse response: " + listener.error);
        }

        return new AuthResult(clientId, listener.token(), listener.expiresInSec);
    }

    private static class Listener implements JsonParserListener {
        private final int STATE_UNKNOWN               = -1;
        private final int STATE_TOKEN                 = 0;
        private final int STATE_TOKEN_EXPIRATION      = 1;

        private int state = STATE_UNKNOWN;

        private String error;

        private String token;

        private long expiresInSec;

        public String token() {
            return token;
        }

        public long getExpiresInSec() {
            return expiresInSec;
        }

        @Override
        public void onJsonStarted() {
            this.state = STATE_UNKNOWN;
            this.error = null;
            this.token = null;
            this.expiresInSec = 0;
        }

        @Override
        public void onError(String error, int position) {
            this.error = error;
            this.state = STATE_UNKNOWN;
        }

        @Override
        public void onJsonEnded() {
            this.state = STATE_UNKNOWN;
        }

        @Override
        public boolean onObjectStarted() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onObjectMember(CharSequence name) {
            if (StringUtils.equals("access_token", name)) {
                this.state = STATE_TOKEN;
            } else if (StringUtils.equals("expires_in", name)) {
                this.state = STATE_TOKEN_EXPIRATION;
            } else {
                this.state = STATE_UNKNOWN;
            }

            return true;
        }

        @Override
        public boolean onObjectEnded() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onArrayStarted() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onArrayEnded() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onStringValue(CharSequence data) {
            if (state == STATE_TOKEN) {
                token = data.toString();
            }

            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onNumberValue(JsonNumber number) {
            if (state == STATE_TOKEN_EXPIRATION) {
                expiresInSec = number.mantissa();
            }

            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onTrueValue() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onFalseValue() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onNullValue() {
            this.state = STATE_UNKNOWN;
            return true;
        }
    }
}
