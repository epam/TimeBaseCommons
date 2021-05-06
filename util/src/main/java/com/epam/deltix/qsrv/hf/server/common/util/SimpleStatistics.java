package com.epam.deltix.qsrv.hf.server.common.util;

public class SimpleStatistics {
    private final int movingFrameSize;

    private double max;
    private double min;
    private double sum;
    private long total;

    private double[] movingValues;
    private int movingIndex;
    private double movingSum;

    public SimpleStatistics(int movingFrameSize) {
        this.movingFrameSize = movingFrameSize;
        movingValues = new double[movingFrameSize];
        reset();
    }

    public void reset() {
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
        sum = 0d;
        total = 0;

        movingIndex = 0;
        movingSum = 0d;
    }

    public void add(double value) {
        total++;
        max = Math.max(value, max);
        min = Math.min(value, min);
        sum += value;

        // moving data processing
        movingIndex = (int) ((total - 1) % movingFrameSize);
        // subtract first non-frame value
        if (total > movingFrameSize)
            movingSum -= movingValues[movingIndex];
        // remember current value and summing it up
        movingValues[movingIndex] = value;
        movingSum += value;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getAvg() {
        return sum / (double) total;
    }

    public long getTotal() {
        return total;
    }

    public double getMovingAvg() {
        return movingSum / (double) Math.min(total, movingFrameSize);
    }

    public double[] getMovingMinMax() {
        double movingMax = Double.MIN_VALUE;
        double movingMin = Double.MAX_VALUE;

        int lastIndex = total >= movingFrameSize ? movingFrameSize - 1 : movingIndex;
        for (int i = 0; i <= lastIndex; i++) {
            movingMin = Math.min(movingMin, movingValues[i]);
            movingMax = Math.max(movingMax, movingValues[i]);
        }
        return new double[] {movingMin, movingMax};
    }

    public int getMovingFrameSize() {
        return movingFrameSize;
    }
}
