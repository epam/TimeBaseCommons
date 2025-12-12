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

import deltix.clock.Clocks;
import com.epam.deltix.qsrv.hf.pub.TimeSource;
import com.epam.deltix.util.annotations.TimestampMs;
import com.epam.deltix.util.annotations.TimestampNs;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared non-decreasing realtime time source with up to nanosecond resolution (if available).
 *
 * <p>Time source that:
 * <ul>
 *     <li>Uses {@link deltix.clock.Clocks#REALTIME} as base time source</li>
 *     <li>Guarantied to return monotonously non-decreasing values</li>
 *     <li>Guaranties consistent time across multiple threads (instance users)</li>
 *     <li>Provided "millis" version is in sync with "nanos" value (rounded down) but is not very efficient</li>
 * </ul>
 *
 * <p>WARNING: This implementation is monotonic in the sense of non-decreasing returned values.
 * However, it is not monotonic in the same sense as Linux CLOCK_MONOTONIC (or {@link Clocks#MONOTONIC})
 * that assumes linear growth with time flow.
 */
@ThreadSafe
public class MonotonicRealTimeSource implements TimeSource {

    // Shared value
    private static final AtomicLong lastTimeNs = new AtomicLong(Long.MIN_VALUE);

    private static final MonotonicRealTimeSource INSTANCE = new MonotonicRealTimeSource();

    private MonotonicRealTimeSource() {
    }

    public static MonotonicRealTimeSource getInstance() {
        return INSTANCE;
    }

    @Override
    @TimestampMs
    public long currentTimeMillis() {
        // TODO: Consider using System.currentTimeMillis() directly Clocks.REALTIME is not available
        //  to avoid extra multiplication and division steps
        return currentTimeNanos() / 1_000_000L;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    @TimestampNs
    public long currentTimeNanos() {
        long currentTimeNanos = Clocks.REALTIME.time();
        while (true) {
            long prevVal = lastTimeNs.get();
            if (prevVal >= currentTimeNanos) {
                // Shared value is already ahead (or same). So we can use it and do not need to update shared value.
                return prevVal;
            }
            // currentTimeNanos > prevVal
            if (lastTimeNs.compareAndSet(prevVal, currentTimeNanos)) {
                return currentTimeNanos;
            }
        }
    }
}
