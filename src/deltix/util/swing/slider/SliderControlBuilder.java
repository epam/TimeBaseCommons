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

    public static Component  createMemoryRangePanel (SliderOptions options){
        final JTextField currentValue = new JTextField();

        final GBC layoutManager = new GBC();
        final RangeSlider slider = new RangeSlider(SwingUtilities.HORIZONTAL);

        slider.setUI(new DrawnSliderUI(slider));

        int memoryInMB;
        try {
            memoryInMB = (int) ((Long.valueOf(MemoryUtils.getTotalPhysicalMemory()) /
                    Math.pow(2, Util.IS_WINDOWS_OS ? 20 : 10)));
        } catch (Throwable t) {
            memoryInMB = 1024;
        }

        final DrawnSliderUI sliderUI = ((DrawnSliderUI) slider.getUI());
        if (true){//hasLeftDefaultArea
            sliderUI.setLeftDefaultArea((int) (memoryInMB * 0.1));
        }

        slider.setMinimum(0);
        slider.setMaximum(memoryInMB);
        slider.setLowValue((int) (memoryInMB * 0.2));
        slider.setHighValue(true /*hasRightDefaultArea*/ ? (int) (memoryInMB * 0.9) : memoryInMB);

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
                currentValue.setText("" + slider.getValue());
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

        currentValue.setText("" + slider.getValue());
        currentValue.setPreferredSize(new Dimension(50, 20));
        currentValue.setEditable(false);

        panel.add(new JPanel(), layoutManager.setPosition(0, 0).
                setWeight(1, 0).
                setInsets(5));
        panel.add(slider,
                layoutManager.setPosition(0, 1).
                        setAnchor(GBC.WEST).
                        setSpan(1, 1).
                        setWeight(1, 0).
                        setFill(GBC.HORIZONTAL));

        panel.add(currentValue,
                layoutManager.setPosition(1, 1).
                        setWeight(0, 0).
                        setFill(GBC.NONE));

        panel.add(new JLabel("MB"),
                layoutManager.setPosition(2, 1));
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

    private static void paintMemoryControlLabels(RangeSlider slider, Graphics g){
        final DrawnSliderUI sliderUI = ((DrawnSliderUI) slider.getUI());
        double sliderX = slider.getLocation().getX();
        double sliderY = slider.getLocation().getY();
        Font defFont = g.getFont();
        Font customFont = new Font(defFont.getFontName(), Font.BOLD, defFont.getSize());
        g.setFont(customFont);
        int value = slider.getLowValue();
        int high = slider.getHighValue();
        int max = slider.getMaximum();
        int min = slider.getMinimum();
        int beforeMin = sliderUI.getLeftDefaultArea();

        ((Graphics2D) g).setStroke(new BasicStroke(2.0f));

        //paint minumum
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(min),
                (int) sliderX,
                (int) (slider.getLocation().getY() - 5));
        //paint maximum
        g.drawString(String.valueOf(max),
                (int) (sliderX + sliderUI.getXLocation(max)),
                (int) (sliderY + slider.getBounds().height + 15));

        if (beforeMin> 0 && beforeMin < value){
            g.setColor(Color.BLACK);
            int position = beforeMin == value ? value : beforeMin;
            g.drawRoundRect(
                    (int) (sliderX + sliderUI.getXLocation(position)-5),
                    (int) sliderY,
                    6,
                    slider.getHeight(),
                    2,
                    2);

            //paint first static area label
            String drawnString = String.format("Data Cache:%sM", sliderUI.getLeftDefaultArea());
            g.setColor(SliderOptions.CACHE_AREA_COLOR);
            g.drawString(drawnString,
                    (int) (sliderX +  5),
                    (int) (sliderY + slider.getBounds().height + 15));
        }


        if (value > 0 && value < high) {

            //paint changing area label
            String drawnString = String.format("-Xms%sM", slider.getValue());
            int drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);

            g.setColor(SliderOptions.PREALLOCATE_AREA_COLOR);
            g.drawString(drawnString,
                    (int) (sliderX +
                            sliderUI.getXLocation(sliderUI.getLeftDefaultArea()) + ((sliderUI.getXLocation(slider.getLowValue() - sliderUI.getLeftDefaultArea())) - drawnStringWidth) / 2),
                    (int) (sliderY - 5));

        }

        //paint changing area thumb
        g.setColor(Color.BLACK);
        int position = value == max ? max : value == min ? min : value;
        g.drawRoundRect(
                (int) (sliderX + sliderUI.getXLocation(position)-5),
                (int) sliderY,
                6,
                slider.getHeight(),
                2,
                2);

        if (high > 0 && high < max){
            //paint static label from the right side (third area)
            g.setColor(Color.BLACK);
            String drawnString = String.format("Total:%sM", high);
            int drawnStringWidth = g.getFontMetrics(defFont).stringWidth(drawnString);
            g.drawString(drawnString,
                    (int) (sliderX + sliderUI.getXLocation(high) - drawnStringWidth/2),
                    (int) (slider.getLocation().getY() - 5));


            //paint finish border of third area
            g.setColor(SliderOptions.MAX_MEMORY_COLOR);
            g.fillRoundRect((int) (sliderX + sliderUI.getXLocation(high) - 3),
                    (int) (sliderY - 3),
                    6,
                    slider.getHeight() + 6,
                    2,
                    2);
            g.setColor(Color.BLACK);
            ((Graphics2D)g).setStroke(new BasicStroke(2.0f));
            g.drawRoundRect((int) (sliderX + sliderUI.getXLocation(high) - 3),
                    (int) (sliderY - 3),
                    6,
                    slider.getHeight() + 6,
                    2,
                    2);
        }
    }

}
