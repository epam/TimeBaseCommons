package deltix.util.swing.slider;

import com.jidesoft.swing.RangeSlider;

import javax.swing.*;

/**
 * User: TurskiyS
 * Date: 4/30/13
 */
public class DrawnSlider extends RangeSlider {

    private SliderOptions options;
    private int thirdValue;

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
}
