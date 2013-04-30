package deltix.util.swing.slider;

import com.jidesoft.swing.RangeSlider;
import deltix.util.lang.Util;
import deltix.util.os.MemoryUtils;
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

    public static Component createMemoryRangePanel(SliderOptions options) {
        // final JTextField currentValue = new JTextField();

        final GBC layoutManager = new GBC();
        final RangeSlider slider = new DrawnSlider(SwingUtilities.HORIZONTAL);

        slider.setUI(new DrawnSliderUI(slider));

        int memoryInMB;
        try {
            memoryInMB = (int) ((Long.valueOf(MemoryUtils.getTotalPhysicalMemory()) /
                    Math.pow(2, Util.IS_WINDOWS_OS ? 20 : 10)));
        } catch (Throwable t) {
            memoryInMB = 1024;
        }

        final DrawnSliderUI sliderUI = ((DrawnSliderUI) slider.getUI());
        if (true) {//hasLeftDefaultArea
            sliderUI.setBeforeMin((int) (memoryInMB * 0.1));
        }

        slider.setMinimum(0);
        slider.setMaximum(memoryInMB);
        slider.setLowValue((int) (memoryInMB * 0.4));
        slider.setHighValue(true/* hasRightDefaultArea*/ ? (int) (memoryInMB * 0.8) : memoryInMB);

        sliderUI.setOverrideThumb(options.isOverrideThumb());

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
                //currentValue.setText("" + slider.getValue());
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

        slider.setBorder(new StrokeBorder(new BasicStroke(SliderOptions.STROKE_WIDTH)));

/*        currentValue.setText("" + slider.getValue());
        currentValue.setPreferredSize(new Dimension(50, 20));
        currentValue.setEditable(false);*/

        panel.add(new JPanel(), layoutManager.setPosition(0, 0).
                setWeight(1, 0).
                setInsets(5));
        panel.add(slider,
                layoutManager.setPosition(0, 1).
                        setAnchor(GBC.WEST).
                        setSpan(1, 1).
                        setWeight(1, 0).
                        setFill(GBC.HORIZONTAL));

/*        panel.add(currentValue,
                layoutManager.setPosition(1, 1).
                        setWeight(0, 0).
                        setFill(GBC.NONE));*/

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

    private static void paintMemoryControlLabels(RangeSlider slider, Graphics g) {
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
        int beforeMin = sliderUI.getBeforeMin();

        ((Graphics2D) g).setStroke(new BasicStroke(SliderOptions.STROKE_WIDTH));

        //paint minumum
        paintTick (g,
                Color.BLACK,
                String.valueOf(min),
                new Point(
                        (int) sliderX,
                        (int) (sliderY + slider.getBounds().height + 15)
                )
        );
        //paint maximum
        paintTick (g,
                Color.BLACK,
                String.valueOf(max),
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(max)),
                        (int) (sliderY + slider.getBounds().height + 15)
                )
        );

        //if (beforeMin > 0 && beforeMin < value) {
        //int position = beforeMin == lowValue ? lowValue : beforeMin;
        paintThumb(g, SliderOptions.FIRST_AREA_COLOR, new Rectangle((int) (sliderX + sliderUI.getXLocation(beforeMin) - 5),
                (int) sliderY - 3,
                SliderOptions.DRAWN_THUMB_SIZE_PX,
                slider.getHeight() + SliderOptions.DRAWN_THUMB_SIZE_PX));

        //paint first tick
        String drawnString = String.format("%sM", beforeMin);
        int drawnStringWidth = g.getFontMetrics(defFont).stringWidth( drawnString);
        paintTick (g,
                SliderOptions.CACHE_AREA_COLOR,
                drawnString,
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(beforeMin)) - drawnStringWidth/2,
                        (int) (sliderY - 10)
                )
        );
        //}


        //if (value > 0 && value < high) {

        //paint second tick
        drawnString = String.format("%sM", lowValue);
        drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);
        paintTick (g,
                SliderOptions.PREALLOCATE_AREA_COLOR,
                drawnString,
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(lowValue)) - drawnStringWidth/2,
                        (int) (sliderY - 10)
                )
        );
        //paint second area thumb
        //position = lowValue == max ? max : lowValue == min ? min : lowValue;
        paintThumb(g, SliderOptions.SECOND_AREA_COLOR, new Rectangle((int) (sliderX + sliderUI.getXLocation(lowValue) - 3),
                (int) sliderY - 3,
                SliderOptions.DRAWN_THUMB_SIZE_PX,
                slider.getHeight() + SliderOptions.DRAWN_THUMB_SIZE_PX));

        //}

        //if (high > 0 && high < max){

        //paint third tick
        drawnString = String.format("%sM", high);
        drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);
        paintTick (g,
                Color.BLACK,
                drawnString,
                new Point(
                        (int) (sliderX + sliderUI.getXLocation(high)) - drawnStringWidth/2,
                        (int) (sliderY - 10)
                )
        );

        //paint third area thumb
        paintThumb(g, SliderOptions.THIRD_AREA_COLOR, new Rectangle((int) (sliderX + sliderUI.getXLocation(high)-1),
                (int) (sliderY - 3),
                SliderOptions.DRAWN_THUMB_SIZE_PX,
                slider.getHeight() + SliderOptions.DRAWN_THUMB_SIZE_PX));
        //}
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

    private static void paintTick (Graphics g, Color color, String str, Point point){
        g.setColor(color);
        g.drawString(str,
                point.x,
                point.y);
    }

}
