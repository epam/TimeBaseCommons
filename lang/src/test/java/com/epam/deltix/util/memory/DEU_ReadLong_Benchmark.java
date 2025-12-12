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

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Simple benchmark to test performance of {@link DataExchangeUtils#readLong(byte[], int)}.
 *
 * @author Alexei Osipov
 */
@State(Scope.Thread)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 4, time = 3)
@Measurement(iterations = 10, time = 3)
public class DEU_ReadLong_Benchmark {
    private static final int COUNT = 1000;

    byte[] bytes = new byte[1024 + COUNT * Long.BYTES];
    long value = 0x12_34_56_78_90_12_34_56L;

    @Param({/*"0",*/ "715" /*, "512"*/})
    int offset;

    @Setup
    public void setup() {
        for (int i = 0; i < COUNT; i++) {
            DataExchangeUtils.writeLong(bytes, offset + i * Long.BYTES, value + i* 37L);
        }
    }


    @SuppressWarnings("removal")
    @Benchmark
    public long readLongOld() {
        long result = 0;
        for (int i = 0; i < COUNT; i++) {
            result ^= DataExchangeUtils.readLongOld(bytes, offset + i * Long.BYTES);
        }
        return result;
    }

    @Benchmark
    public long readLongNew() {
        long result = 0;
        for (int i = 0; i < COUNT; i++) {
            result ^= DataExchangeUtils.readLong(bytes, offset + i * Long.BYTES);
        }
        return result;
    }

    @Benchmark
    public long baseline() {
        return value;
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DEU_ReadLong_Benchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                //.result("DEU_ReadLong_Benchmark.json")
                //.resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
        //Main.main(args);
    }
}
