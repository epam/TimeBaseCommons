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
package com.epam.deltix.util.currency;

import com.epam.deltix.util.collections.generated.ObjectToObjectHashMap;
import com.epam.deltix.util.collections.hash.HashCodeComputer;
import com.epam.deltix.util.collections.hash.StringHashCodeComputer;
import com.epam.deltix.util.lang.Util;


class ThreeLetterToObjectMapQuick <T> extends ObjectToObjectHashMap<CharSequence, T> {

    ThreeLetterToObjectMapQuick (int initialCapacity) {
        super (initialCapacity, StringHashCodeComputer.INSTANCE);
    }

    ThreeLetterToObjectMapQuick () {
        super (StringHashCodeComputer.INSTANCE);
    }

    @Override
    protected void          putKey (int pos, CharSequence key) {
        super.putKey (pos, key.toString ());
    }

    @Override
    protected boolean       keyEquals (CharSequence a, CharSequence b) {
        return (Util.equals (a, b));
    }

    @SuppressWarnings("unchecked")
    public final T          get (CharSequence key, int start, int end, T notFoundValue) {
        int         pos = find (key, start, end);

        return (pos == NULL ? notFoundValue : (T)values [pos]);
    }

    protected int           find (CharSequence key, int start, int end) {
        int hidx = HashCodeComputer.computeModHashCode (hashCode (key, start, end), hashIndex.length);
        return (find (hidx, key, start, end));
    }

    protected int           find (int hidx, CharSequence key, int start, int end) {
        for (int chain = hashIndex [hidx]; chain != NULL; chain = next [chain]) {
            assert hashIndex ((CharSequence)keys [chain]) == hidx;

            if (keyEquals (key, start, end, (CharSequence)keys [chain]))
                return (chain);
        }

        return (NULL);
    }


    /**
     *  Replicates the String.hashCode () logic for arbitrary CharSequence instances.
     *  Returns 0 for null.
     */
    private static int hashCode (CharSequence cs, int start, int end) {
        if (cs == null)
            return (0);

        int         hc = 0;

        for (int i = start; i < end; i++)
            hc = 31 * hc + cs.charAt (i);

        return (hc);
    }

    private static boolean keyEquals(
            CharSequence s1,
            int start, int end,
            CharSequence s2
    ) {
        if (s1 == null) {
            return (s2 == null);
        } else {
            if (s2 == null)
                return false;
        }
        int len = s2.length();

        if (len != end - start)
            return false;

        for (int ii = 0; ii < len; ii++) {
            int cdiff = s1.charAt(ii + start) - s2.charAt(ii);

            if (cdiff != 0)
                return false;
        }

        return true;
    }

}