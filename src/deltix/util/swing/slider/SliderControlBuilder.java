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

    public static Component createMemoryRangePanel(final DrawnSlider slider) {
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
                setWeight(1, 0).
                setInsets(5));
        panel.add(slider,
                layoutManager.setPosition(0, 1).
                        setAnchor(GBC.WEST).
                        setSpan(1, 1).
                        setWeight(1, 0).
                        setFill(GBC.HORIZONTAL));

        panel.add(new JLabel("       "),
                layoutManager.setPosition(1, 1).
                        setWeight(0, 0).
                        setFill(GBC.NONE));
        panel.add(new JPanel(), layoutManager.setPosition(0, 2).
                setWeight(1, 0));

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
        slider.setThirdValue(options.getTick(1));
        slider.setLowValue(options.getTick(2));
        slider.setHighValue(options.getTick(3));

        slider.setUI(new DrawnSliderUI(slider));

        slider.setBorder(new StrokeBorder(new BasicStroke(SliderOptions.STROKE_WIDTH)));

        return slider;
    }

    private static void paintMemoryControlLabels(DrawnSlider slider, Graphics g) {
        final DrawnSliderUI sliderUI = ((DrawnSliderUI) slider.getUI());
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
                        (int) sliderX,
                        (int) (sliderY + slider.getBounds().height + 15)
                )
        );
        //paint maximum
        paintTick(g,
                Color.BLACK,
                String.valueOf(max),
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(max)),
                        (int) (sliderY + slider.getBounds().height + 15)
                )
        );

        //if (beforeMin > 0 && beforeMin < value) {
        paintThumb(g, slider.getOptions().getColor(0), new Rectangle((int) (sliderX + sliderUI.getXLocation(beforeMin) - 5),
                (int) sliderY - 3,
                SliderOptions.DRAWN_THUMB_SIZE_PX,
                slider.getHeight() + SliderOptions.DRAWN_THUMB_SIZE_PX));

        //paint first tick
        String drawnString = String.format("%sM", beforeMin);
        int drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);
        paintTick(g,
                slider.getOptions().getColor(0),
                drawnString,
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(beforeMin)) - drawnStringWidth / 2,
                        (int) (sliderY - 10)
                )
        );

        //paint second tick
        drawnString = String.format("%sM", lowValue);
        drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);
        paintTick(g,
                slider.getOptions().getColor(1),
                drawnString,
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(lowValue)) - drawnStringWidth / 2,
                        (int) (sliderY - 10)
                )
        );
        //paint second area thumb
        paintThumb(
                g,
                slider.getOptions().getColor(1),
                new Rectangle((int) (sliderX + sliderUI.getXLocation(lowValue) - 3),
                        (int) sliderY - 3,
                        SliderOptions.DRAWN_THUMB_SIZE_PX,
                        slider.getHeight() + SliderOptions.DRAWN_THUMB_SIZE_PX));

        //paint third tick
        drawnString = String.format("%sM", high);
        drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);
        paintTick(g,
                slider.getOptions().getColor(2),
                drawnString,
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(high)) - drawnStringWidth / 2,
                        (int) (sliderY - 10)
                )
        );

        //paint third area thumb
        paintThumb(g, slider.getOptions().getColor(2), new Rectangle((int) (sliderX + sliderUI.getXLocation(high) - 1),
                (int) (sliderY - 3),
                SliderOptions.DRAWN_THUMB_SIZE_PX,
                slider.getHeight() + SliderOptions.DRAWN_THUMB_SIZE_PX));
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

}
