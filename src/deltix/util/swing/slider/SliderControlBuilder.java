package deltix.util.swing.slider;

import deltix.util.swing.GBC;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: TurskiyS
 * Date: 4/29/13
 */
public class SliderControlBuilder {

    private static final String MEMORY_TEXT_FORMAT = "%sM";

    public static JComponent createMemoryRangePanel(final DrawnSlider slider) {
        final GBC layoutManager = new GBC();
        final JPanel panel = new JPanel(new GridBagLayout()) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                try {
                    paintSliderLabelsAndThumbs(slider, g);
                } catch (Throwable t) {
                    super.paint(g);
                }
            }

        };

        panel.setBorder(BorderFactory.createTitledBorder(""));

        //Slider Change Listener
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                panel.repaint();
            }
        });

        //Slider focus lost listener
        slider.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                panel.repaint();
            }

            @Override
            public void focusGained(FocusEvent e) {
                panel.repaint();
            }
        });


        //build layout
        panel.add(new JPanel(), layoutManager.setPosition(0, 0).
                setFill(GBC.HORIZONTAL).
                setInsets(5).
                setWeight(1, 0));

        panel.add(slider,
                layoutManager.setPosition(0, 1).
                        setAnchor(GBC.WEST).
                        setSpan(1, 1).
                        setWeight(1, 0).
                        setFill(GBC.HORIZONTAL));

        if (slider.getOptions().isMinMaxUnderSlider()) {
            panel.add(new JPanel(), layoutManager.setPosition(0, 2).
                    setFill(GBC.NONE).
                    setInsets(5).
                    setWeight(1, 0));
        }

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.repaint();
            }
        });


        return panel;
    }

    public static Component createMemoryRangePanel(final SliderOptions options) {
        final DrawnSlider slider = createSlider(options);

        return createMemoryRangePanel(slider);
    }

    public static DrawnSlider createSlider(final SliderOptions options) throws RuntimeException {
        if (options.getTicks() != null){
           switch (options.getTicks().length){
               case 3:
                    return createSliderOneThumb(options);
               case 4:
                    return createSliderTwoThumbs(options);
               case 5:
                    return createSliderThreeThumbs(options);
               default:
                   throw new RuntimeException("Initialization error: bad slider options");
           }
        }
        throw new RuntimeException("Initialization error: bad slider options");
    }

    public static DrawnSlider createSliderThreeThumbs(final SliderOptions options) {
        final DrawnSlider slider = new DrawnSlider(options);

        slider.setMinimum(options.getTick(0));
        slider.setMaximum(options.getTick(4));

        SliderScale scale = SliderScale.getInstance(options.getTick(4));
        slider.setThirdValue(scale.getValueInScale(options.getTick(1)));
        slider.setLowValue(scale.getValueInScale(options.getTick(2)));
        slider.setHighValue(scale.getValueInScale(options.getTick(3)));

        slider.setUI(new DrawnSliderUI(slider));

        slider.setBorder(new StrokeBorder(new BasicStroke(SliderOptions.STROKE_WIDTH)));

        return slider;
    }

    public static DrawnSlider createSliderTwoThumbs(final SliderOptions options) {
        options.setVisibilities(0x011);

        ArrayList<Color> colorList = getColorListTwoThumbs(options.getColors());
        options.setColors(colorList.toArray(new Color[colorList.size()]));

        colorList = getColorListTwoThumbs(options.getTickColors());
        options.setTickColors(colorList.toArray(new Color[colorList.size()]));

        colorList = getColorListTwoThumbs(options.getTextColors());
        options.setTextColors(colorList.toArray(new Color[colorList.size()]));

        ArrayList<String> labelList = new ArrayList<>();
        labelList.add(null);

        if (options.getLabels() != null){
            labelList.addAll(Arrays.asList(options.getLabels()));
        }else{
            labelList.addAll(Arrays.asList((String)null, (String)null, (String)null));
        }
        options.setLabels(labelList.toArray(new String[labelList.size()]));


        final DrawnSlider slider = new DrawnSlider(options);

        slider.setMinimum(options.getTick(0));
        slider.setMaximum(options.getTick(3));

        SliderScale scale = SliderScale.getInstance(options.getTick(3));
        slider.setThirdValue(scale.getValueInScale(options.getTick(0)));
        slider.setLowValue(scale.getValueInScale(options.getTick(1)));
        slider.setHighValue(scale.getValueInScale(options.getTick(2)));

        slider.setUI(new DrawnSliderUI(slider));

        slider.setBorder(new StrokeBorder(new BasicStroke(SliderOptions.STROKE_WIDTH)));

        return slider;
    }

    private static ArrayList<Color> getColorListOneThumb(Color[] colors){
        ArrayList<Color> colorList = new ArrayList<>();
        colorList.add(null);
        if (colors != null){
            colorList.addAll(Arrays.asList(colors));
            colorList.add(null);
        }else{
            colorList.addAll(Arrays.asList(
                    SliderOptions.SECOND_AREA_COLOR,
                    SliderOptions.THIRD_AREA_COLOR,
                    null));
        }
        return colorList;
    }

    private static ArrayList<Color> getColorListTwoThumbs(Color[] colors){
        ArrayList<Color> colorList = new ArrayList<>();
        colorList.add(null);
        if (colors != null){
            colorList.addAll(Arrays.asList(colors));
        }else{
            colorList.addAll(Arrays.asList(
                    SliderOptions.SECOND_AREA_COLOR,
                    SliderOptions.THIRD_AREA_COLOR,
                    SliderOptions.FOURTH_AREA_COLOR));
        }
        return colorList;
    }

    public static DrawnSlider createSliderOneThumb(final SliderOptions options) {
        options.setVisibilities(0x010);

        ArrayList<Color> colorList = getColorListOneThumb(options.getColors());
        options.setColors(colorList.toArray(new Color[colorList.size()]));

        colorList = getColorListOneThumb(options.getTickColors());
        options.setTickColors(colorList.toArray(new Color[colorList.size()]));

        colorList = getColorListOneThumb(options.getTextColors());
        options.setTextColors(colorList.toArray(new Color[colorList.size()]));

        ArrayList<String> labelList = new ArrayList<>();
        labelList.add(null);

        if (options.getLabels() != null){
            labelList.addAll(Arrays.asList(options.getLabels()));
            labelList.add(null);
        }else{
            labelList.addAll(Arrays.asList((String)null, (String)null, (String)null));
        }
        options.setLabels(labelList.toArray(new String[labelList.size()]));

        final DrawnSlider slider = new DrawnSlider(options);

        slider.setMinimum(options.getTick(0));
        slider.setMaximum(options.getTick(2));

        SliderScale scale = SliderScale.getInstance(options.getTick(2));
        slider.setThirdValue(scale.getValueInScale(options.getTick(0)));
        slider.setLowValue(scale.getValueInScale(options.getTick(1)));
        slider.setHighValue(scale.getValueInScale(options.getTick(2)));

        slider.setUI(new DrawnSliderUI(slider));

        slider.setBorder(new StrokeBorder(new BasicStroke(SliderOptions.STROKE_WIDTH)));

        return slider;
    }

    private static void paintSliderLabelsAndThumbs(DrawnSlider slider, Graphics g) {
        final DrawnSliderUI sliderUI = ((DrawnSliderUI) slider.getUI());
        boolean readOnly = slider.getOptions().isReadOnly();
        double sliderX = slider.getLocation().getX();
        double sliderY = slider.getLocation().getY();
        Font defFont = g.getFont();
        Font customFont = new Font(defFont.getFontName(), Font.BOLD, defFont.getSize());
        g.setFont(customFont);
        int lowValue = slider.getLowValue();
        int high = slider.getHighValue();
        int max = slider.getMaximum();
        int min = slider.getMinimum();
        int beforeMin = slider.getThirdValue();

        ((Graphics2D) g).setStroke(new BasicStroke(SliderOptions.STROKE_WIDTH));

       //paint minimum
        paintTick(g,
                Color.BLACK,
                String.valueOf(min),
                new Point(
                        getLabelXPosition (slider, min, g, customFont),
                        getLabelYPosition (slider, min)
                )
        );
        //paint maximum
        paintTick(g,
                Color.BLACK,
                String.format(MEMORY_TEXT_FORMAT, max),
                new Point(
                        getLabelXPosition (slider, max, g, customFont),
                        slider.getOptions().isMinMaxUnderSlider() ?
                                (int) (sliderY + slider.getBounds().height + 15) :
                                getLabelYPosition (slider, max)

                )
        );

        String drawnString;
        SliderScale  scale = SliderScale.getInstance(max);

        //if (beforeMin > 0 && beforeMin < value) {
        if (slider.getOptions().isVisibile(0)) {
            if (!readOnly) {
                paintThumb(g, slider.getOptions().getColor(0), new Rectangle((int) (sliderX + sliderUI.getXLocation(beforeMin) - 5),
                        (int) sliderY - SliderOptions.THUMB_OVER_BODER_PX,
                        SliderOptions.DRAWN_THUMB_SIZE_PX,
                        slider.getHeight() + SliderOptions.THUMB_OVER_BODER_PX * 2));
            }

            //paint first tick
            drawnString = String.format(MEMORY_TEXT_FORMAT, scale.getVisibleValue(beforeMin));
            paintTick(g,
                    slider.getOptions().getTickColor(0),
                    drawnString,
                    new Point(
                            getLabelXPosition(slider, beforeMin, g, customFont),
                            getLabelYPosition(slider, beforeMin)
                    )
            );
        }

        //paint second tick
        if (slider.getOptions().isVisibile(1)) {

            drawnString = String.format(MEMORY_TEXT_FORMAT, scale.getVisibleValue(lowValue));
            paintTick(g,
                    slider.getOptions().getTickColor(1),
                    drawnString,
                    new Point(
                            getLabelXPosition(slider, lowValue, g, customFont),
                            getLabelYPosition(slider, lowValue)
                    )
            );
            //paint second area thumb
            if (!readOnly) {
                boolean threeThumbs = (slider.getOptions().getVisibilities() ^ 0x111) == 0;
                paintThumb(
                        g,
                        slider.getOptions().getColor(1),
                        new Rectangle((int) (sliderX + sliderUI.getXLocation(lowValue) - 3),
                                (int) sliderY - SliderOptions.THUMB_OVER_BODER_PX * (threeThumbs ? 4 : 1),
                                SliderOptions.DRAWN_THUMB_SIZE_PX,
                                slider.getHeight() + SliderOptions.THUMB_OVER_BODER_PX * (threeThumbs ? 5 : 2)));
            }
        }

        //paint third tick
        if (slider.getOptions().isVisibile(2)) {
            drawnString = String.format(MEMORY_TEXT_FORMAT, scale.getVisibleValue(high));
            paintTick(g,
                    slider.getOptions().getTickColor(2),
                    drawnString,
                    new Point(
                            getLabelXPosition(slider, high, g, customFont),
                            getLabelYPosition(slider, high)
                    )
            );

            //paint third area thumb
            if (!readOnly) {
                paintThumb(g, slider.getOptions().getColor(2), new Rectangle((int) (sliderX + sliderUI.getXLocation(high) - 1),
                        (int) (sliderY - SliderOptions.THUMB_OVER_BODER_PX),
                        SliderOptions.DRAWN_THUMB_SIZE_PX,
                        slider.getHeight() + SliderOptions.THUMB_OVER_BODER_PX * 2));
            }
        }
    }

    private static void paintThumb(Graphics g, Color thumbColor, Rectangle rect) {
        g.setColor(thumbColor);
        g.fillRoundRect(
                (int) rect.getX(),
                (int) rect.getY(),
                (int) rect.getWidth(),
                (int) rect.getHeight(),
                2,
                2
        );

        g.setColor(Color.BLACK);
        ((Graphics2D) g).setStroke(new BasicStroke(SliderOptions.STROKE_WIDTH));
        g.drawRoundRect(
                (int) rect.getX(),
                (int) rect.getY(),
                (int) rect.getWidth(),
                (int) rect.getHeight(),
                2,
                2
        );
    }

    private static void paintTick(Graphics g, Color color, String str, Point point) {
        g.setColor(color);
        g.drawString(str,
                point.x,
                point.y);
    }

    private static int getLabelXPosition(DrawnSlider slider, int value, Graphics g, Font customFont) {
        final DrawnSliderUI sliderUI = ((DrawnSliderUI) slider.getUI());
        boolean  readOnly = slider.getOptions().isReadOnly();
        final int lowValue = slider.getLowValue();
        final int high = slider.getHighValue();
        final int max = slider.getMaximum();
        final int min = slider.getMinimum();
        final int beforeMin = slider.getThirdValue();

        double sliderX = slider.getLocation().getX();

        SliderScale scale = SliderScale.getInstance(max);

        String drawnStringMax = String.valueOf(scale.getVisibleValue(max));
        int drawnStringMaxWidth = g.getFontMetrics(customFont).stringWidth(drawnStringMax);
        int maxLabelPosition = (int) (sliderX + sliderUI.getXLocation(max) - (drawnStringMaxWidth * 0.9));

        String drawnStringBeforeMin = String.format(MEMORY_TEXT_FORMAT, scale.getVisibleValue(beforeMin));
        int drawnStringBeforeMinWidth = g.getFontMetrics(customFont).stringWidth(drawnStringBeforeMin);

        String drawnStringLow = String.format(MEMORY_TEXT_FORMAT, scale.getVisibleValue(lowValue));
        int drawnStringLowWidth = g.getFontMetrics(customFont).stringWidth(drawnStringLow);

        String drawnStringHigh = String.format(MEMORY_TEXT_FORMAT, scale.getVisibleValue(high));
        int drawnStringWidthHigh = g.getFontMetrics(customFont).stringWidth(drawnStringHigh);

        if (value == max) {
            return maxLabelPosition;
        } else if (value == min) {
            return (int) sliderX;
        } else if (value == beforeMin) {
            return (int) (Math.max(
                    sliderX + sliderUI.getXLocation(beforeMin) -
                            drawnStringBeforeMinWidth +
                            (readOnly || beforeMin == lowValue ? drawnStringBeforeMinWidth / 2 : 0),
                    sliderX
            )
            );
        } else if (value == lowValue) {
            return (int) (Math.max(
                            Math.min(sliderX + sliderUI.getXLocation(lowValue) -
                                ((slider.getOptions().getVisibilities() ^ 0x011) == 0 ?  drawnStringLowWidth : drawnStringLowWidth / 4),
                                (slider.getOptions().getVisibilities() ^ 0x010) == 0 ? maxLabelPosition: maxLabelPosition - drawnStringLowWidth ),
                            sliderX)
            );
        } else if (value == high) {
            return (int) (
                    Math.max(
                            Math.min(
                                    sliderX + sliderUI.getXLocation(high),// - (readOnly ? drawnStringWidthHigh / 2 : 0),
                                    maxLabelPosition
                            ),
                            sliderX + Math.max(drawnStringLowWidth, drawnStringBeforeMinWidth))
            );
        }

        return (int) sliderX;
    }

    private static int getLabelYPosition(DrawnSlider slider, int value) {
        final int lowValue = slider.getLowValue();
        final int high = slider.getHighValue();
        final int max = slider.getMaximum();
        final int min = slider.getMinimum();
        final int beforeMin = slider.getThirdValue();

        double sliderY = slider.getLocation().getY();
        int numberYPosition1 = (int) (sliderY - 5);
        int numberYPosition2 = (int) (sliderY - 15);

        if (value == beforeMin && slider.getOptions().isVisibile(0)) {
            return beforeMin == lowValue && (slider.getOptions().getVisibilities() ^ 0x111) == 0  ? numberYPosition2 : numberYPosition1;
        } else if (value == min) {
            return slider.getOptions().isMinMaxUnderSlider() ?
                    (int) (sliderY + slider.getBounds().height + 15) :
                    value == lowValue ? numberYPosition1 :  numberYPosition2;

        } else if (value == high && slider.getOptions().isVisibile(2)) {
            return high == lowValue && (slider.getOptions().getVisibilities() ^ 0x111) == 0  ? numberYPosition2 : numberYPosition1;

        } else if (value == lowValue) {
            return (slider.getOptions().getVisibilities() ^ 0x111) == 0 ?  numberYPosition2 : numberYPosition1;
        } else if (value == max) {
            return slider.getOptions().isMinMaxUnderSlider() ?
                    (int) (sliderY + slider.getBounds().height + 15) :
                    numberYPosition2;
        }


        return (int) sliderY;
    }

}
