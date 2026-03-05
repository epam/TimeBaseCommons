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

import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Andy
 *         Date: 6/4/12 11:15 AM
 */
public class Test_Util {

    @Test
    public void testArrayAddObjectArgument () {
        String[] input = {"a", "b", "c"};
        String[] output = Util.arrayadd(input, "d");
        assertEquals("[a, b, c, d]", Arrays.toString(output));
    }
}