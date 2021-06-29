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

import com.epam.deltix.containers.interfaces.MutableString;

/**
 * Base class for poolable messages that could be written in Timebase.
 */
public interface MessagePoolable extends IdentityKey, MessageInfo, RecordInterface {
    /**
     * Sets this message to represent a point in time that is
     * <code>value</code> milliseconds after January 1, 1970 00:00:00 GMT.
     *
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp
     * in which case TimeBase server stores message using current server time.
     *
     * @param   value   the number of milliseconds.
     */
    void setTimeStampMs(long value);

    /**
     * Sets time to the 'undefined' value {link TIMESTAMP_UNKNOWN}
     */
    void nullifyTimeStampMs();

    /**
     * Sets this message to represent a point in time that is
     * <code>value</code> milliseconds after January 1, 1970 00:00:00 GMT.
     *
     * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp
     * in which case TimeBase server stores message using current server time.
     *
     * @param   value   the number of milliseconds.
     */
    void setNanoTime(long value);

    /**
     * Sets time to the 'undefined' value {link TIMESTAMP_UNKNOWN}
     */
    void nullifyNanoTime();

    /**
     * Use this name.
     */
    MutableString useSymbol();

    /**
     * Instrument name.
     * @return Symbol
     */
    MutableString getSymbol();

    /**
     * Instrument name.
     */
    void nullifySymbol();

    /**
     * Deep copies content from src instance to this.
     * @param src source for copy.
     */
    MessagePoolable copyFrom(RecordInfo src);

    /**
     * Reset all instance field to their default states.
     * @return this.
     */
    MessagePoolable reset();

    /**
     * Set null to all fields of this instance.
     * @return this.
     */
    MessagePoolable nullify();
}