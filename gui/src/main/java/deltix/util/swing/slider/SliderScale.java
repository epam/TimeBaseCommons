package deltix.util.swing.slider;

/**
 * User: TurskiyS
 * Date: 5/21/13
 */
public abstract class SliderScale {

    protected int sliderMax;

    protected SliderScale() {
    }

    public abstract int getVisibleValue(int value);

    public abstract int getValueInScale(int value);

    public int multiplyValue(int value, float multiplier) {
        int visibleValue = getVisibleValue(value);
        return getValueInScale((int) (visibleValue * multiplier));
    }

    protected int roundToTick(int value, int tickSize) {
        int residue = value % tickSize;
        if (residue != 0) {
            if (residue < tickSize / 2) {
                value = value - residue;
            } else {
                value = value + (tickSize - residue);
            }
        }
        return value >= sliderMax ? sliderMax : value;
    }

    public static SliderScale getInstance(int sliderMax) {
        return new SliderScale2(sliderMax);
    }

    public static class SliderScale1 extends SliderScale {

        private static final int MIN_TICK = 50;
        private static final int MIDDLE_OF_SLIDER = 1000;

        private int gauge;

        public SliderScale1(int sliderMax) {
            this.sliderMax = sliderMax;
            gauge = (int) ((double) sliderMax / 2 / ((sliderMax - MIDDLE_OF_SLIDER) / MIN_TICK));
        }

        public int getVisibleValue(int value) {
            int newValue = value;

            if (newValue < sliderMax / 2) {
                newValue = (int) (newValue / (double) ((sliderMax / 2 / MIDDLE_OF_SLIDER)));
            } else {
                newValue = (int) (MIDDLE_OF_SLIDER + (double) (newValue - sliderMax / 2) / gauge * MIN_TICK);
            }

            return roundToTick(newValue, MIN_TICK);
        }

        public int getValueInScale(int value) {
            int newValue = value;

            if (newValue < MIDDLE_OF_SLIDER) {
                newValue = (int) (newValue * ((double) (sliderMax / 2 / MIDDLE_OF_SLIDER)));
            } else {
                if (newValue == sliderMax) {
                    return sliderMax;
                }
                newValue = (int) (gauge * (double) (newValue - MIDDLE_OF_SLIDER) / MIN_TICK + sliderMax / 2);
            }

            return newValue;

        }

    }

    public static class SliderScale2 extends SliderScale {

        private static final int MIN_TICK = 32;
        private double valueExponense;
        private final int functionBorderValue;

        public SliderScale2(int sliderMax) {
            this.sliderMax = sliderMax;
            functionBorderValue = (int)((double)3 / 4 * sliderMax);
            valueExponense = Math.log((double) sliderMax / 2) / Math.log(functionBorderValue);
        }


        public int getVisibleValue(int value) {
            int newValue = value;


            if (value < functionBorderValue) {
                newValue = (int) Math.pow(newValue, valueExponense);
            } else {
                newValue = 2 * newValue - sliderMax;
            }

            return roundToTick (newValue, MIN_TICK);
        }

        public int getValueInScale(int value) {
            int newValue = value;

            if (value < sliderMax / 2) {
                newValue = (int) Math.pow(newValue, 1 / valueExponense);
            } else {
                if (newValue == sliderMax) {
                    return sliderMax;
                }
                newValue = (value + sliderMax)/2;

            }

            return newValue;

        }
    }

}


