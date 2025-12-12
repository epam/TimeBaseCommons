package com.epam.deltix.util.memory;

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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
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
public class DEU_ReadLong48_Benchmark {

    private static final VarHandle BE_INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.BIG_ENDIAN);
    private static final VarHandle BE_SHORT_HANDLE = MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.BIG_ENDIAN);


    private static final int COUNT = 1000;

    byte[] bytes = new byte[1024 + COUNT * 6];
    long value = 0x12_34_56_78_90_12_34_56L;

    //@Param({"0", "715", "512"})
    @Param({"715"})
    int offset;

    @Setup
    public void setup() {
        for (int i = 0; i < COUNT; i++) {
            DataExchangeUtils.writeLong48(bytes, offset + i * 6, value + i* 37L);
        }
    }


    @Benchmark
    public long writeLong48() {
        long result = 0;
        for (int i = 0; i < COUNT; i++) {
            result ^= DataExchangeUtils.readLong48(bytes, offset + i * 6);
        }
        return result;
    }
//
//    @Benchmark
//    public long writeLong48_new() {
//        long result = 0;
//        for (int i = 0; i < COUNT; i++) {
//            result ^= readLong48_new(bytes, offset + i * 6);
//        }
//        return result;
//    }
//
//    // Has same performance as readLong48
//    public static long readLong48_new(byte[] bytes, int offset) {
//        int highBits = (int) BE_INT_HANDLE.get(bytes, offset);
//        short lowBits = (short) BE_SHORT_HANDLE.get(bytes, offset + Integer.BYTES);
//        long b1 = (long) highBits << Short.SIZE;
//        return b1 | (lowBits & 0xFFFF);
//    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DEU_ReadLong48_Benchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                //.result("DEU_ReadLong_Benchmark.json")
                //.resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
        //Main.main(args);
    }
}
