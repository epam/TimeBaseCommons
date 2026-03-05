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

import com.epam.deltix.util.lang.Disposable;
import com.epam.deltix.util.oauth.service.Oauth2ClientImpl;

public interface Oauth2Client extends Disposable {

    static Oauth2Client create(Oauth2ClientConfig config) {
        return Oauth2ClientImpl.of(config);
    }

    AuthResult login();

    default String clientId() {
        return login().userName();
    }

    default String token() {
        return login().accessToken();
    }

}
