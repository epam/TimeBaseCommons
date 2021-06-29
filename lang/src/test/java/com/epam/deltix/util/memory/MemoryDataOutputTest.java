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
package com.epam.deltix.util.memory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Alexei Osipov
 */
public class MemoryDataOutputTest {
    @Test
    public void writeLongBytes() throws Exception {
        MemoryDataOutput mdo = new MemoryDataOutput();
        int result = mdo.writeLongBytes(0xFF_00_FFL);
        assertEquals(3, result);
        assertEquals(3, mdo.getSize());

        int result2 = mdo.writeLongBytes(0xCAFE_BABEL);
        assertEquals(4, result2);
        assertEquals(7, mdo.getSize());
    }

}