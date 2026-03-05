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
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DefaultTimeSourceProviderTest {

    @After
    public void after() {
        System.clearProperty(DefaultTimeSourceProvider.TIME_SOURCE_SYS_PROP);
        DefaultTimeSourceProvider.unconfigure();
    }

    @Test
    public void testDefault() {
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof KeeperTimeSource);
    }

    @Test
    public void testConfiguredByProperty() {
        System.setProperty(DefaultTimeSourceProvider.TIME_SOURCE_SYS_PROP, "MonotonicRealTimeSource");
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof MonotonicRealTimeSource);
    }

    @Test
    public void testConfiguredExplicitly() {
        DefaultTimeSourceProvider.configure("testConf", MonotonicRealTimeSource.getInstance());
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof MonotonicRealTimeSource);
    }

    @Test
    public void testExplicitConfigOverridesProperty() {
        DefaultTimeSourceProvider.configure("testConf", KeeperTimeSource.getInstance());
        System.setProperty(DefaultTimeSourceProvider.TIME_SOURCE_SYS_PROP, "MonotonicRealTimeSource");
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof KeeperTimeSource);
    }
}
