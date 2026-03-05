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

import com.epam.deltix.util.memory.MemoryDataOutput;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Simple benchmark to test performance of {@link MemoryDataOutput#writeLongBytes}.
 *
 * @author Alexei Osipov
 */
@State(Scope.Thread)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 10, time = 3)
public class WriteLongBytesBenchmark {
    MemoryDataOutput out = new MemoryDataOutput();
    long value = 0;

    @Benchmark
    public int writeLongBytes() {
        out.seek(0);
        return out.writeLongBytes(value++);
    }
}