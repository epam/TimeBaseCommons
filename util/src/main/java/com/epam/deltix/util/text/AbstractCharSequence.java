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
package com.epam.deltix.util.text;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.collections.CharSubSequence;

/**
 *
 */
public abstract class AbstractCharSequence implements CharSequence {
    @Override
    public boolean          equals (Object obj) {
        return (Util.equals (this, (CharSequence) obj));
    }

    @Override
    public int              hashCode () {
        return (Util.hashCode (this));
    }

    @Override
    public String           toString () {
        return (Util.toString (this));
    }

    public CharSequence     subSequence (int start, int end) {
        return (new CharSubSequence (this, start, end));
    }
}