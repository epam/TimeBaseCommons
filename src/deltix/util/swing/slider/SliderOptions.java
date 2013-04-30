package deltix.util.swing.slider;

import java.awt.*;

/**
 * User: TurskiyS
 * Date: 4/26/13
 */
public class SliderOptions {

    public  static final Color    FIRST_AREA_COLOR            = new Color(237, 28, 36  );
    public  static final Color    SECOND_AREA_COLOR           = new Color( 34, 177, 76 );
    public  static final Color    THIRD_AREA_COLOR            = new Color(181, 230, 29 );
    public  static final Color    FOURTH_AREA_COLOR           = new Color(255, 255, 255);


    private              String[] labels                      = new String[4];
    private              Color[]  colors                      = new Color[]{
            FIRST_AREA_COLOR,
            SECOND_AREA_COLOR,
            THIRD_AREA_COLOR,
            FOURTH_AREA_COLOR
                              };

    private              int[]    positions                   = new int[]  {0, 0, 0, 0};

    public  static final Color    CACHE_AREA_COLOR            = FIRST_AREA_COLOR;
    public  static final Color    PREALLOCATE_AREA_COLOR      = SECOND_AREA_COLOR;
    public  static final Color    MAX_MEMORY_COLOR            = THIRD_AREA_COLOR;
    public  static final Color    TOTAL_PHYSICAL_MEMORY_COLOR = FOURTH_AREA_COLOR;

    public  static       float    STROKE_WIDTH                = 2.0f;

    public  static       int      DRAWN_THUMB_SIZE_PX         = 6;

    private              boolean  overrideThumb               = false;

    private              boolean  lowThumbEnabled             = true;
    private              boolean  highThumbEnabled            = true;

    public SliderOptions() {
    }

    public SliderOptions(int areaCount) {
        labels    = new String[areaCount];
        colors    = new Color[areaCount];
        positions = new int[areaCount];
    }

    public boolean isOverrideThumb() {
        return overrideThumb;
    }

    public void setOverrideThumb(boolean overrideThumb) {
        this.overrideThumb = overrideThumb;
    }

    private String getLable(int areaIndex) {
        if (areaIndex < labels.length) {
            return labels[areaIndex];
        }
        return null;
    }

    private void setLable(int areaIndex, String value) {
        if (areaIndex < labels.length) {
            labels[areaIndex] = value;
        }
    }

    private void setLables(String[] values) {
        if (values != null) {
            labels = values;
        }
    }

    private Color getColor(int areaIndex) {
        if (areaIndex < colors.length) {
            return colors[areaIndex];
        }
        return null;
    }


    private void setColor(int areaIndex, Color value) {
        if (areaIndex < colors.length) {
            colors[areaIndex] = value;
        }
    }


    private void setColors(Color[] values) {
        if (values != null) {
            colors = values;
        }
    }

    public int[] getPositions() {
        return positions;
    }

    private int getPosition(int areaIndex) {
        if (areaIndex < positions.length) {
            return positions[areaIndex];
        }
        return 0;
    }

    private void setPositiob(int areaIndex, int value) {
        if (areaIndex < positions.length) {
            positions[areaIndex] = value;
        }
    }

    private void setPositions(int[] values) {
        if (values != null) {
            positions = values;
        }
    }
}
