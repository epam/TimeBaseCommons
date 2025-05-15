package deltix.util.memory;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
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
@Warmup(iterations = 4, time = 3)
@Measurement(iterations = 10, time = 3)
public class DEU_WriteLong_Benchmark {
    byte[] bytes = new byte[1024];
    long value = 0x12_34_56_78_90_12_34_56L;

    //@Param({"0", "715", "512"})
    @Param({"715"})
    int offset;


    @Benchmark
    public byte writeLong() {
        DataExchangeUtils.writeLong(bytes, offset, value);
        return bytes[offset];
    }

/*
    @Benchmark
    public byte writeLong2() {
        DataExchangeUtils.writeLong2(bytes, offset, value);
        return bytes[offset];
    }
*/

    @Benchmark
    public long baseline() {
        return value;
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DEU_WriteLong_Benchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                //.result("DEU_ReadLong_Benchmark.json")
                //.resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
        //Main.main(args);
    }
}
