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
package com.epam.deltix.util.id;

/** Thread-safe generator of unique integer numbers */
public interface IdentifierGenerator {
	/** @return next valid order id. This method is thread safe */
    long next();

    /**
     * Special method to support UHF Failover. Method re-establishes next unique order ID from original media.
     * @throws UnsupportedOperationException if reset/failover is not supported
     * */
    void reset()
        throws UnsupportedOperationException;
}