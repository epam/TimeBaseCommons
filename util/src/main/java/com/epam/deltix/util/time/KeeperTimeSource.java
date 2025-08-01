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

/**
 * {@link TimeSource} implementation that uses {@link TimeKeeper} as time source.
 *
 * <p>Guarantied to return monotonously non-decreasing values.
 *
 * <p>
 *
 * @author Alexei Osipov
 */
public class KeeperTimeSource implements TimeSource {
    public static final KeeperTimeSource INSTANCE = new KeeperTimeSource();

    private KeeperTimeSource() {
    }

    public static KeeperTimeSource getInstance() {
        return INSTANCE;
    }

    @Override
    public long currentTimeMillis() {
        return TimeKeeper.currentTime;
    }

    @Override
    public long currentTimeNanos() {
        return TimeKeeper.currentTimeNanos;
    }
}