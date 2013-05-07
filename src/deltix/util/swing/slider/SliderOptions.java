package deltix.util.swing.slider;

import com.jidesoft.swing.RangeSlider;

import java.awt.*;
import java.util.Arrays;

/**
 * User: TurskiyS
 * Date: 4/26/13
 */
public class SliderOptions{

    public  static final Color       FIRST_AREA_COLOR    = new Color(237, 28, 36  );
    public  static final Color       SECOND_AREA_COLOR   = new Color( 34, 177, 76 );
    public  static final Color       THIRD_AREA_COLOR    = new Color(181, 230, 29 );
    public  static final Color       FOURTH_AREA_COLOR   = new Color(255, 255, 255);

    private              String[]    labels              = null;
    private              Color[]     colors              = null;
    private              Color[]     textColors          = null;
    private              Color[]     tickColors          = null;

    private              int[]       ticks               = null;

    public  static       float       STROKE_WIDTH        = 2.0f;
    public  static       int         Y_START_COORDINATE  = 2;

    public  static       int         DRAWN_THUMB_SIZE_PX = 6;

    private              boolean     overrideThumb       = false;

    private              boolean     readOnly            = false;

    private              boolean     minMaxUnderSlider   = false;


    public SliderOptions(int[] ticks) {
        this.ticks = ticks;

        labels = new String[]{
                "",
                "",
                "",
                ""
        };

        colors = new Color[]{
                FIRST_AREA_COLOR,
                SECOND_AREA_COLOR,
                THIRD_AREA_COLOR,
                FOURTH_AREA_COLOR
        };
        textColors = new Color[4];
        Arrays.fill(textColors, Color.BLACK);

    }

    public SliderOptions(int[] ticks, String[] labels) {
        this.ticks = ticks;
        this.labels = labels;
        colors = new Color[]{
                FIRST_AREA_COLOR,
                SECOND_AREA_COLOR,
                THIRD_AREA_COLOR,
                FOURTH_AREA_COLOR
        };
        textColors = new Color[4];
        Arrays.fill(textColors, Color.BLACK);
    }

    public SliderOptions(int[] ticks, String[] labels, Color[] colors) {
        this.ticks = ticks;
        this.labels = labels;
        this.colors = colors;
        textColors = new Color[4];
        Arrays.fill(textColors, Color.BLACK);
    }

    public boolean isOverrideThumb() {
        return overrideThumb;
    }

    public void setOverrideThumb(boolean overrideThumb) {
        this.overrideThumb = overrideThumb;
    }

    public String getLabel(int areaIndex) {
        if (labels != null && areaIndex < labels.length) {
            return labels[areaIndex];
        }
        return null;
    }

    public String getLabel(int areaIndex, int allowedWidth, FontMetrics metrics) {
        String label = getLabel(areaIndex);
        if (label != null && label.length() > 0){
            String result = "";
            for (int i=1; i<=label.length(); i++){
                String drawnString = label.substring(0, i);
                if (i < label.length()){
                    drawnString += "...";
                }
                int drawnStringWidth = metrics.stringWidth(drawnString);
                if (drawnStringWidth <= allowedWidth){
                    result = drawnString;
                }else {
                    return result;
                }
            }
            return result;
        }

        return null;
    }

    private void setLabel(int areaIndex, String value) {
        if (labels != null && areaIndex < labels.length) {
            labels[areaIndex] = value;
        }
    }

    public void setLabels(String[] values) {
        if (values != null) {
            labels = values;
        }
    }

    public Color getColor(int areaIndex) {
        if (colors != null && areaIndex < colors.length) {
            return colors[areaIndex];
        }
        return null;
    }


    public void setColor(int areaIndex, Color value) {
        if (colors != null && areaIndex < colors.length) {
            colors[areaIndex] = value;
        }
    }

    public void setColors(Color[] values) {
        if (values != null) {
            colors = values;
        }
    }

    public Color[] getColors() {
        return colors;
    }

    public void setTextColors(Color[] values) {
        if (values != null) {
            textColors = values;
        }
    }

    public Color getTextColor(int areaIndex) {
        if (textColors != null && areaIndex < textColors.length) {
            return textColors[areaIndex];
        }
        return Color.BLACK;
    }

    public void setTickColors(Color[] values) {
        if (values != null) {
            tickColors = values;
        }
    }

    public Color getTickColor(int areaIndex) {
        if (tickColors != null && areaIndex < tickColors.length) {
            return tickColors[areaIndex];
        }
        return getColor(areaIndex);
    }

    public void setTickColor(int areaIndex, Color value) {
        if (tickColors != null && areaIndex < tickColors.length) {
            tickColors[areaIndex] = value;
        }
    }


    public int[] getTicks() {
        return ticks;
    }

    public void setTicks(int[] values) {
        if (values != null) {
            ticks = values;
        }
    }

    public int getTick(int areaIndex) {
        if (ticks != null && areaIndex < ticks.length) {
            return ticks[areaIndex];
        }
        return 0;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isMinMaxUnderSlider() {
        return minMaxUnderSlider;
    }

    public void setMinMaxUnderSlider(boolean minMaxUnderSlider) {
        this.minMaxUnderSlider = minMaxUnderSlider;
    }

    protected SliderOptions copy(){
        SliderOptions options = new SliderOptions(ticks,
                                                  labels,
                                                  colors );
        options.setTickColors(tickColors);
        options.setTextColors(textColors);
        options.setOverrideThumb(overrideThumb);
        options.setMinMaxUnderSlider(minMaxUnderSlider);
        options.setReadOnly(readOnly);

        return options;
    }
}
