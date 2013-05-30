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

/**
 * User: TurskiyS
 * Date: 4/29/13
 */
public class SliderControlBuilder {

    public static JComponent createMemoryRangePanel(final DrawnSlider slider) {
        final GBC layoutManager = new GBC();
        final JPanel panel = new JPanel(new GridBagLayout()) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                try {
                    paintMemoryControlLabels(slider, g);
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

    public static DrawnSlider createSlider(final SliderOptions options) {
        final DrawnSlider slider = new DrawnSlider(options);

        slider.setMinimum(options.getTick(0));
        slider.setMaximum(options.getTick(4));
        slider.setThirdValue(SliderScale.getValueInScale(options.getTick(1), options.getTick(4)));
        slider.setLowValue(SliderScale.getValueInScale(options.getTick(2), options.getTick(4)));
        slider.setHighValue(SliderScale.getValueInScale(options.getTick(3), options.getTick(4)));

        slider.setUI(new DrawnSliderUI(slider));

        slider.setBorder(new StrokeBorder(new BasicStroke(SliderOptions.STROKE_WIDTH)));

        return slider;
    }

    private static void paintMemoryControlLabels(DrawnSlider slider, Graphics g) {
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
                String.valueOf(max),
                new Point(
                        getLabelXPosition (slider, max, g, customFont),
                        slider.getOptions().isMinMaxUnderSlider() ?
                                (int) (sliderY + slider.getBounds().height + 15) :
                                getLabelYPosition (slider, max)

                )
        );

        //if (beforeMin > 0 && beforeMin < value) {
        if (!readOnly){
            paintThumb(g, slider.getOptions().getColor(0), new Rectangle((int) (sliderX + sliderUI.getXLocation(beforeMin) - 5),
                    (int) sliderY - SliderOptions.THUMB_OVER_BODER_PX,
                    SliderOptions.DRAWN_THUMB_SIZE_PX,
                    slider.getHeight() + SliderOptions.THUMB_OVER_BODER_PX * 2));
        }

        //paint first tick
        String drawnString = String.format("%sM", SliderScale.getVisibleValue(beforeMin, max));
        paintTick(g,
                slider.getOptions().getTickColor(0),
                drawnString,
                new Point(
                        getLabelXPosition (slider, beforeMin, g, customFont),
                        getLabelYPosition (slider, beforeMin)
                )
        );

        //paint second tick
        drawnString = String.format("%sM", SliderScale.getVisibleValue(lowValue, max));
        paintTick(g,
                slider.getOptions().getTickColor(1),
                drawnString,
                new Point(
                        getLabelXPosition (slider, lowValue, g, customFont),
                        getLabelYPosition (slider, lowValue)
                )
        );
        //paint second area thumb
        if (!readOnly) {
            paintThumb(
                    g,
                    slider.getOptions().getColor(1),
                    new Rectangle((int) (sliderX + sliderUI.getXLocation(lowValue) - 3),
                            (int) sliderY - 4 * SliderOptions.THUMB_OVER_BODER_PX,
                            SliderOptions.DRAWN_THUMB_SIZE_PX,
                            slider.getHeight() + SliderOptions.THUMB_OVER_BODER_PX * 5));
        }

        //paint third tick
        drawnString = String.format("%sM", SliderScale.getVisibleValue(high, max));
        paintTick(g,
                slider.getOptions().getTickColor(2),
                drawnString,
                new Point(
                        getLabelXPosition (slider, high, g, customFont),
                        getLabelYPosition (slider, high)
                )
        );

        //paint third area thumb
        if (!readOnly){
            paintThumb(g, slider.getOptions().getColor(2), new Rectangle((int) (sliderX + sliderUI.getXLocation(high) - 1),
                    (int) (sliderY - SliderOptions.THUMB_OVER_BODER_PX),
                    SliderOptions.DRAWN_THUMB_SIZE_PX,
                    slider.getHeight() + SliderOptions.THUMB_OVER_BODER_PX * 2));
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

        String drawnString = "";
        int drawnStringWidth = 0;

        if (value == max) {
            drawnString = String.valueOf(SliderScale.getVisibleValue(max, max));
            drawnStringWidth = g.getFontMetrics(customFont).stringWidth(drawnString);
            return (int) (sliderX + sliderUI.getXLocation(max) - (drawnStringWidth * 0.9));

        } else if (value == min) {
            return (int) sliderX;
        } else if (value == beforeMin) {
            String drawnStringMin = String.format("%sM", SliderScale.getVisibleValue(beforeMin, max));
            int drawnStringMinWidth = g.getFontMetrics(customFont).stringWidth(drawnStringMin);
            return (int) (Math.max(
                    sliderX + sliderUI.getXLocation(beforeMin) - drawnStringMinWidth + (readOnly ? drawnStringMinWidth / 2 : 0),
                    sliderX
                    )
            );
        } else if (value == lowValue) {
            drawnString = String.format("%sM", SliderScale.getVisibleValue(lowValue, max));
            drawnStringWidth = g.getFontMetrics(customFont).stringWidth(drawnString);
            return (int) (sliderX + sliderUI.getXLocation(lowValue) - drawnStringWidth / 2);
        } else if (value == high) {
            String drawnStringHigh = String.format("%sM", SliderScale.getVisibleValue(high, max));
            int drawnStringWidthHigh = g.getFontMetrics(customFont).stringWidth(drawnStringHigh);
            String drawnStringMax = String.format("%sM", SliderScale.getVisibleValue(max, max));
            int drawnStringWidthMax = g.getFontMetrics(customFont).stringWidth(drawnStringMax);
            return (int) (
                    Math.min(
                            sliderX + sliderUI.getXLocation(high) - (readOnly ? drawnStringWidthHigh / 2 : 0),
                            sliderX + sliderUI.getXLocation(max) - (drawnStringWidthMax * 0.9)
                    )
            );
        }

        return (int) sliderX;
    }

    private static int getLabelYPosition(DrawnSlider slider, int value) {
        boolean  readOnly = slider.getOptions().isReadOnly();
        final int lowValue = slider.getLowValue();
        final int high = slider.getHighValue();
        final int max = slider.getMaximum();
        final int min = slider.getMinimum();
        final int beforeMin = slider.getThirdValue();

        double sliderY = slider.getLocation().getY();
        int numberYPosition1 = (int) (sliderY - 5);
        int numberYPosition2 = (int) (sliderY - 15);

        if (value == max) {
            return slider.getOptions().isMinMaxUnderSlider() ?
                    (int) (sliderY + slider.getBounds().height + 15) :
                    numberYPosition2;

        } else if (value == min) {
            return slider.getOptions().isMinMaxUnderSlider() ?
                    (int) (sliderY + slider.getBounds().height + 15) :
                    numberYPosition2;

        } else if (value == beforeMin) {
            return numberYPosition1;
        } else if (value == high) {
            return numberYPosition1;

        } else if (value == lowValue) {
            return numberYPosition2;
        }

        return (int) sliderY;
    }

}
