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
package com.epam.deltix.timebase.messages;

/**
 *
 */
public interface TimeStampedMessage {

    public final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
    public final long    INT64_NULL = Long.MIN_VALUE;

    /**
     * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    public long             getTimeStampMs();

    /**
     * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
     * @return the number of nanoseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    public long             getNanoTime();
}