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

public class IncreasingDelayRetryStrategy implements RetryStrategy {

    private static final long DEFAULT_RETRY_DELAY_MS = 5 * 1000;
    private static final long MAX_RETRY_DELAY_MS = 5 * 60 * 1000; // 5 min

    private long retryDelay = DEFAULT_RETRY_DELAY_MS;

    private int retriesMade;

    @Override
    public synchronized void refreshRetryDelay() {
        retryDelay = DEFAULT_RETRY_DELAY_MS;
        retriesMade = 0;
    }

    @Override
    public synchronized long nextRetryDelay() {
        ++retriesMade;

        long currentRetryDelay = retryDelay;
        retryDelay *= 2;
        if (retryDelay > MAX_RETRY_DELAY_MS) {
            retryDelay = MAX_RETRY_DELAY_MS;
        }

        return currentRetryDelay;
    }

    @Override
    public int retriesMade() {
        return retriesMade;
    }

}
