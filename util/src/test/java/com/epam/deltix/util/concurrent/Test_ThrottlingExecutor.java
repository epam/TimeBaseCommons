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
package com.epam.deltix.util.concurrent;


import java.util.Random;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_ThrottlingExecutor {
    private final Random                    random = new Random (2009);
    private long                            usedTime;
    
    class TestTask extends ThrottlingExecutor.Task {
        public boolean         run () {
            long                t1 = System.currentTimeMillis ();
            int                 t = random.nextInt (30) + 15;

            if (t < 20)
                t = 0;
            else
                try {
                    Thread.sleep (t);
                } catch (InterruptedException x) {
                    x.printStackTrace(System.out);
                    return false;
                }

            long                dt = System.currentTimeMillis () - t1;

            usedTime += dt;

            if (!Boolean.getBoolean ("quiet"))
                System.out.printf ("%,16d %16d %16d\n", t1, t, dt);
            
            return (true);
        }
    }

    @Test(timeout = 90000)
    public void             go () throws InterruptedException {

        boolean cruiseControlMode = Boolean.getBoolean("deltix.test.mode");
        double                  desiredRatio = cruiseControlMode ? 0.25 : 0.07;   // when executed together with other tests CPU usage much higher

        if (!Boolean.getBoolean ("quiet"))
            System.out.println ("Target: " + desiredRatio);

        Thread.sleep (100);

        ThrottlingExecutor      exe = new ThrottlingExecutor ("Test", desiredRatio);

        exe.start ();

        Thread.sleep (100);

        long            startTime = System.currentTimeMillis ();

        new TestTask().submit(exe);

        Thread.sleep (10000);

        exe.interrupt ();
        exe.join ();

        double          totalTime = System.currentTimeMillis () - startTime;
        double          actualRatio = usedTime / totalTime;
        double          dev = Math.abs (actualRatio - desiredRatio);
        
        if (!Boolean.getBoolean ("quiet"))
            System.out.println ("Actual: " + actualRatio + "; d=" + (dev * 100) + "%");

        assertTrue (
            "Target ratio: " + desiredRatio +
                " is too different from actual: " + actualRatio,
             dev < 0.02
        );
    }
}