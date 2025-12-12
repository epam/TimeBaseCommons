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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorRefreshScheduler implements RefreshTokenScheduler {
    private final ScheduledExecutorService executor;

    private ScheduledFuture<?> currentTask;

    public ExecutorRefreshScheduler(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public synchronized void schedule(long timestampMs, Runnable task) {
        currentTask = executor.schedule(task, timestampMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void close() {
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
    }
}
