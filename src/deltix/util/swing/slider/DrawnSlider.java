package deltix.util.swing.slider;

import com.jidesoft.swing.RangeSlider;

import javax.swing.*;

/**
 * User: TurskiyS
 * Date: 4/30/13
 */
public class DrawnSlider extends RangeSlider {

    private static int           MB         = 1024 * 1024;

    private        SliderOptions options;
    private        int           thirdValue;

    public DrawnSlider(int orientation) {
        super(orientation);
    }

    public DrawnSlider(SliderOptions options) {
        super(SwingUtilities.HORIZONTAL);
        this.options = options;
    }

    public SliderOptions getOptions() {
        return options;
    }

    public void setOptions(SliderOptions options) {
        this.options = options;
    }

    public int getThirdValue() {
        return thirdValue;
    }

    public void setThirdValue(int thirdValue) {
        this.thirdValue = thirdValue;
    }

    public Object getValues() {
        SliderScale scale = SliderScale.getInstance(getMaximum());
        Long[] values = new Long[]{
                (long) scale.getVisibleValue(getThirdValue()) * MB,
                (long) scale.getVisibleValue(getLowValue()) * MB,
                (long) scale.getVisibleValue(getHighValue()) * MB
        };

        int visibilities = options.getVisibilities();

        if ((visibilities ^ 0x111) == 0) {
            return values;
        } else if ((visibilities ^ 0x011) == 0) {
            return new Long[]{
                    values[1],
                    values[2]
            };
        } else if ((visibilities ^ 0x010) == 0) {
            return values[1];
        }

        return null;

    }

    public void setValues(Object value) {
        if (value != null) {
            SliderScale scale = SliderScale.getInstance(getMaximum());
            if (value instanceof Long[]) {
                Long[] lv = (Long[]) value;
                if (lv.length == 3) {
                    setThirdValue(scale.getValueInScale((int) (lv[0] / MB)));
                    setLowValue(scale.getValueInScale((int) (lv[1] / MB)));
                    setHighValue(scale.getValueInScale((int) (lv[2] / MB)));
                } else {
                    setThirdValue(scale.getValueInScale(0));
                    setLowValue(scale.getValueInScale((int) (lv[1] / MB)));
                    setHighValue(scale.getValueInScale((int) (lv[2] / MB)));
                }
            }else if(value instanceof Long){
                setThirdValue(0);
                setLowValue(scale.getValueInScale((int) ((Long)value / MB)));
                setHighValue(getMaximum());

            }
        }
    }
}
