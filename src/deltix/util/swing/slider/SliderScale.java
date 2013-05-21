package deltix.util.swing.slider;

/**
 * User: TurskiyS
 * Date: 5/21/13
 */
public class SliderScale {

    private static final int MIN_TICK = 50;
    private static final int MIDDLE_OF_SLIDER = 1000;

    public static int getVisibleValue(int value, int sliderMax){
        int newValue = value;

        if (newValue < sliderMax/2 ){
            newValue = newValue / ((sliderMax/2 / MIDDLE_OF_SLIDER));
        }else{
            int gauge = sliderMax/2 / ((sliderMax - MIDDLE_OF_SLIDER)/MIN_TICK);
            newValue = MIDDLE_OF_SLIDER + (newValue - sliderMax/2)/gauge * MIN_TICK;
        }

        int residue = newValue % MIN_TICK;
        if (residue == 0){
            return newValue >= sliderMax ? sliderMax : newValue;
        }else{
            if (residue < MIN_TICK /2){
                newValue =   newValue - residue;
            }
            else{
                newValue =   newValue + (MIN_TICK - residue);
            }
            return newValue >= sliderMax ? sliderMax : newValue;
        }
    }

    public static int getValueInScale(int value, int sliderMax) {
        if (value < MIDDLE_OF_SLIDER) {
            return value * ((sliderMax / 2 / MIDDLE_OF_SLIDER));
        } else{
            if (value == sliderMax){
                return sliderMax;
            }
            int gauge = sliderMax/2 / ((sliderMax - MIDDLE_OF_SLIDER)/MIN_TICK);
            value = gauge * (value - MIDDLE_OF_SLIDER)/MIN_TICK + sliderMax/2;
        }

        return value;

    }

}
