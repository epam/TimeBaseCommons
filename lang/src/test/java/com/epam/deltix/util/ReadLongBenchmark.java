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
package com.epam.deltix.util;

import com.epam.deltix.util.memory.DataExchangeUtils;
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
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 10, time = 3)
public class ReadLongBenchmark {
    byte[] bytes = new byte[1024];
    long value = 0x12_34_56_78_90_12_34_56L;

    @Param({"0", "715", "512"})
    int offset;

    @Setup
    public void setup() {
        DataExchangeUtils.writeLong(bytes, offset, value);
    }


    @Benchmark
    public long readLongOld() {
        return DataExchangeUtils.readLongOld(bytes, offset);
    }

    @Benchmark
    public long readLongNew() {
        return DataExchangeUtils.readLong(bytes, offset);
    }

    @Benchmark
    public long baseline() {
        return value;
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ReadLongBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                //.result("ReadLongBenchmark.json")
                //.resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
        //Main.main(args);
    }
}