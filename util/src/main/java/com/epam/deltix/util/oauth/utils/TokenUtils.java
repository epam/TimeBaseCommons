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
package com.epam.deltix.util.oauth.utils;

import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

public class TokenUtils {

    public static String extractUserName(String token, String usernameClaim) {
        Object userNameObj = extractClaim(token, usernameClaim);
        if (userNameObj instanceof CharSequence) {
            return ((CharSequence) userNameObj).toString();
        }

        throw new RuntimeException("Can not extract username from token.");
    }

    public static Object extractClaim(String token, String claim) {
        try {
            return JWTParser.parse(token).getJWTClaimsSet().getClaim(claim);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to decode token", e);
        }
    }
}
