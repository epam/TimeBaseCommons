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
