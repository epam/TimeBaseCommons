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
package com.epam.deltix.util.lang;

import java.util.Set;

/**
 * Attribute key/value holder.
 */
public interface AttributeProvider<T> {
    /**
     * Returns set of attribute keys, never <code>null</code>.
     * @return attribute keys.
     */
    Set<String> getAttributeKeys();

    /**
     * Returns target attribute by key or <code>null</code> if key does not exist.
     * @param key attribute key.
     * @return target attribute by key or <code>null</code> if key does not exist.
     */
    T getAttribute(String key);
}