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
package com.epam.deltix.util.time;

import com.epam.deltix.qsrv.hf.pub.TimeSource;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic time source that executes System.currentTimeMillis() in the calling thread and
 * guarantied to return monotonously non-decreasing values.
 *
 * <p>Nanosecond precision is not provided ({@link #currentTimeNanos()} returns upscaled millisecond values).
 *
 * @author Alexei Osipov
 */
public class BasicTimeSource implements TimeSource {
    private static final long NANOS_IN_MS = 1_000_000;

    private static final AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);

    public static final BasicTimeSource INSTANCE = new BasicTimeSource();

    private BasicTimeSource() {
    }

    public static BasicTimeSource getInstance() {
        return INSTANCE;
    }

    @Override
    public long currentTimeMillis() {
        long currentTime = System.currentTimeMillis();
        while (true) {
            long prevVal = lastTime.get();
            if (prevVal >= currentTime) {
                // Shared value is already ahead (or same). So we can use it and do not need to update shared value.
                return prevVal;
            }
            // currentTime > prevVal
            if (lastTime.compareAndSet(prevVal, currentTime)) {
                return currentTime;
            }
        }
    }

    @Override
    public long currentTimeNanos() {
        return currentTimeMillis() * NANOS_IN_MS;
    }
}