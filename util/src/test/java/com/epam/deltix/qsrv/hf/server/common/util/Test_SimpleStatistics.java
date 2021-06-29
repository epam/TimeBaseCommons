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
package com.epam.deltix.qsrv.hf.server.common.util;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class Test_SimpleStatistics {

    @Test
    public void testMovingAverage() {
        Random rnd = new Random();

        int movingAvgSize = 70;
        SimpleStatistics stat = new SimpleStatistics(movingAvgSize);

        double[] values = new double[10000];
        for (int i = 0; i < values.length; i++)
            values[i] = Math.pow(10, rnd.nextInt(5)) * rnd.nextDouble();

        for (int i = 0; i < values.length; i++) {
            stat.add(values[i]);

            double movingMax = Double.MIN_VALUE;
            double movingMin = Double.MAX_VALUE;
            double sum = 0d;
            int start = Math.max(0, i - movingAvgSize + 1);
            for (int j = start; j <= i; j++) {
                sum += values[j];
                movingMin = Math.min(movingMin, values[j]);
                movingMax = Math.max(movingMax, values[j]);
            }

            double[] minMax = stat.getMovingMinMax();
            double movingAvg = sum / Math.min(i + 1, movingAvgSize);

            Assert.assertEquals(movingMin, minMax[0], 0.0001);
            Assert.assertEquals(movingMax, minMax[1], 0.0001);
            Assert.assertEquals(movingAvg, stat.getMovingAvg(), 0.0001);
        }
    }
}