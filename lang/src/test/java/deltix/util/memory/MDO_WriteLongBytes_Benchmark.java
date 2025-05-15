package deltix.util.memory;

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
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
public class MDO_WriteLongBytes_Benchmark {
    private final int MIN_LENGTH = 0;
    private final int MAX_LENGTH = 8;
    private final long[] values = new long[MAX_LENGTH + 1];

    MemoryDataOutput out = new MemoryDataOutput();

    @Setup
    public void setup() {
        // Trigger buffer growth at least once
        for (int i = 0; i < 1000; i++) {
            out.writeLong(Long.MAX_VALUE);
        }

        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            values[i] = 1L << ((i-1) * 8);
        }
        values[0] = 0;
    }

    @Benchmark
    public int writeLongBytes() {
        out.seek(1);
        int r = 0;
        for (int i = MIN_LENGTH; i <= MAX_LENGTH; i++) {
            r += out.writeLongBytes(values[i]);
        }
        return r;
    }
}
