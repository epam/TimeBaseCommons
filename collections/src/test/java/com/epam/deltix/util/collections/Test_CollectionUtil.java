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
package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.LongArrayList;
import com.epam.deltix.util.collections.generated.LongList;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;


public class Test_CollectionUtil {

    @Test
    public void testHashCode() {
        for (int i = 0; i < 1000; i++) {
            final long v1 = randomLong();
            final long v2 = randomLong();

            Assert.assertEquals(CollectionUtil.hashCode(list(v1, v2)), CollectionUtil.hashCode(list(v1, v2)));
        }

        Assert.assertNotEquals(CollectionUtil.hashCode(list(1)), CollectionUtil.hashCode(list(2)));
    }

    @Test
    public void testEquals() {
        for (int i = 0; i < 1000; i++) {
            final long v1 = randomLong();
            final long v2 = randomLong();

            Assert.assertTrue(CollectionUtil.equals(list(v1, v2), list(v1, v2)));
            Assert.assertFalse(CollectionUtil.equals(list(v1), list(v1, v2)));
        }
    }

    @Test
    public void testCopy() {
        for (int i = 0; i < 1000; i++) {
            final LongArrayList list = new LongArrayList(i);

            for (int j = 0; j < i; j++) {
                list.add(randomLong());
            }

            final LongArrayList copy = CollectionUtil.copy(list);
            Assert.assertTrue(CollectionUtil.equals(list, copy));
        }
    }

    private static LongList list(long... values) {
        return new LongArrayList(values);
    }

    private static long randomLong() {
        return ThreadLocalRandom.current().nextLong();
    }

}