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
