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

import com.epam.deltix.util.collections.CharSubSequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@State(Scope.Thread)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 4, time = 3)
@Measurement(iterations = 10, time = 3)
public class MDO_WriteString_Benchmark {
    public static final int OFFSET = 17;
    private static final int COUNT = 1000;

    final MemoryDataOutput out = new MemoryDataOutput();

    final StringBuilder[] strings = new StringBuilder[COUNT];

    @Param({"3", "7", "20", "100"})
    //@Param({"7"})
    int strLength;

    @Param({"1", "2", "3"})
    int bytePerChar;

    @Param({"false", "true"})
    boolean pollute;


    @Setup
    public void setup() {
        // Trigger buffer growth at least once
        for (int i = 0; i < 1000; i++) {
            out.writeLong(Long.MAX_VALUE);
        }

        if (pollute) {
            // "Pollute" the method by calling it with different types of arguments
            List<Consumer<CharSequence>> methods = List.of(out::writeString, out::writeString);

            for (Consumer<CharSequence> method : methods) {
                method.accept(""); // String
                method.accept("123");
                method.accept(new StringBuilder("abc")); // String builder
                method.accept(new CharSubSequence("aBCDe", 1, 4)); // 3-rd different type
                method.accept(new StringBuilder().append((char) 0x0081)); // 2 byte char
                method.accept(new StringBuilder().append((char) 0x0801)); // 3 byte char
                method.accept(new StringBuilder("раз два три")); // 2 byte chars
            }
        }


        // Generate strings (1 byte per char)
        for (int i = 0; i < COUNT; i++) {
            StringBuilder sb = new StringBuilder();
            switch (bytePerChar) {
                case 1: {
                    for (int j = 0; j < strLength; j++) {
                        sb.append((char) ('a' + ((i + j) % 26)));
                    }
                    break;
                }
                case 2: {
                    for (int j = 0; j < strLength; j++) {
                        sb.append((char) (0x0080 + ((i + j) % 0x80)));
                    }
                    break;
                }
                case 3: {
                    for (int j = 0; j < strLength; j++) {
                        sb.append((char) (0x0800 + ((i + j) % 0x800)));
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unsupported bytePerChar: " + bytePerChar);
            }
            strings[i] = sb;
        }
    }


    @Benchmark
    @OperationsPerInvocation(COUNT)
    public void writeStringNonNull() {
        out.seek(OFFSET);
        for (int i = 0; i < COUNT; i++) {
            CharSequence str = strings[i];
            out.writeStringNonNullOld(str, 0, str.length());
        }
    }

    @Benchmark
    @OperationsPerInvocation(COUNT)
    public void writeStringNonNullNew() {
        out.seek(OFFSET);
        for (int i = 0; i < COUNT; i++) {
            CharSequence str = strings[i];
            out.writeStringNonNull(str, 0, str.length());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MDO_WriteString_Benchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .result("MDO_WriteString_Benchmark.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
        //Main.main(args);
    }

}
