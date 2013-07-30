package deltix.util.swing.slider;

import deltix.util.lang.Util;
import deltix.util.os.MemoryUtils;
import deltix.util.swing.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;


/**
 * User: TurskiyS
 * Date: 4/8/13
 */
public class MemorySliderTest extends JFrame {

    public MemorySliderTest() {
        super("Memory Slider UI Test");
        JPanel container = new JPanel(new GridBagLayout());

        int memoryInMB;
        try {
            memoryInMB = (int) ((Long.valueOf(MemoryUtils.getTotalPhysicalMemory()) /
                    Math.pow(2, Util.IS_WINDOWS_OS ? 20 : 10)));
        } catch (Throwable t) {
            memoryInMB = 1024;
        }

        GBC layout = new GBC().setFill(GBC.HORIZONTAL).
                setWeight(1, 0).setAnchor(GBC.WEST);

        //colored slider
        drawColoredSliderWithThreeThumbs (memoryInMB, container, layout);

        //black and white slider
        drawBWSliderWithThreeThumbs (memoryInMB, container, layout);

        //read only slider
        drawROSliderWithThreeThumbs (memoryInMB, container, layout);

        //slider with one thumb
        drawColoredSliderWithOneThumb(memoryInMB, container, layout);

        //slider with one thumb
        drawColoredSliderWithTwoThumbs(memoryInMB, container, layout);

        add(container);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                dispose();
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {

        MemorySliderTest frame = new MemorySliderTest();
        frame.setPreferredSize(new Dimension(400, 550));
        frame.pack();
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(windowSize.width/2 - frame.getWidth()/2, windowSize.height/2 - frame.getHeight()/2);

        frame.setVisible(true);

    }

    private void drawColoredSliderWithThreeThumbs(int memoryInMB, JPanel container,  GBC layout){
        SliderOptions options = new SliderOptions(
                new int[]{
                        0,
                        (int) (memoryInMB * 8 * 0.05),
                        (int) (memoryInMB * 8  * 0.1),
                        (int) (memoryInMB * 8  * 0.15),
                        memoryInMB * 8
                },
                new String[]{"Cache",
                        "Initial",
                        "Max",
                        "Physical"}
        );

        options.setTickColors(Arrays.copyOf(options.getColors(),options.getColors().length));
        //options.setTickColor(2, Color.BLACK);

        DrawnSlider slider1 = SliderControlBuilder.createSlider(options);
        container.add(new JLabel("Colored 3 thumbs:"), layout.setPosition(0, 0).setInsets(5));
        container.add(SliderControlBuilder.createMemoryRangePanel(slider1), layout.setPosition(0, 1));
    }

    private void drawBWSliderWithThreeThumbs(int memoryInMB, JPanel container,  GBC layout){
        SliderOptions options = new SliderOptions(
                new int[]{
                        0,
                        (int) (memoryInMB * 8 * 0.05),
                        (int) (memoryInMB * 8 * 0.1),
                        (int) (memoryInMB * 8 * 0.15),
                        memoryInMB*8
                },
                new String[]{"Cache",
                        "Initial",
                        "Max",
                        "Physical"},
                new Color[]{
                        new Color(96, 96, 96),
                        new Color(128, 128, 128),
                        new Color(192, 192, 192),
                        SliderOptions.FOURTH_AREA_COLOR,
                }
        );


        Color[] textColors = new Color[4];
        Arrays.fill(textColors, Color.WHITE);
        textColors[3] = Color.BLACK;
        options.setTextColors(textColors);
        DrawnSlider slider2 = SliderControlBuilder.createSlider(options);
        container.add(new JLabel("Black-and-White:"), layout.setPosition(0, 2));
        container.add(SliderControlBuilder.createMemoryRangePanel(slider2), layout.setPosition(0, 3));
    }

    private void drawROSliderWithThreeThumbs(int memoryInMB, JPanel container,  GBC layout){
        SliderOptions options = new SliderOptions(
                new int[]{
                        0,
                        (int) (memoryInMB * 8 * 0.05),
                        (int) (memoryInMB * 8 * 0.1),
                        (int) (memoryInMB * 8 * 0.15),
                        memoryInMB*8
                },
                new String[]{"Cache",
                        "Initial",
                        "Max",
                        "Physical"},
                new Color[]{
                        new Color(96, 96, 96),
                        new Color(128, 128, 128),
                        new Color(192, 192, 192),
                        SliderOptions.FOURTH_AREA_COLOR,
                }
        );

        //options.setMinMaxUnderSlider(true);
        options.setReadOnly(true);
        DrawnSlider slider3 = SliderControlBuilder.createSlider(options);
        slider3.setEnabled(false);
        container.add(new JLabel("Read Only:"), layout.setPosition(0, 4));
        container.add(SliderControlBuilder.createMemoryRangePanel(slider3), layout.setPosition(0, 5));
    }

    private void drawColoredSliderWithOneThumb(int memoryInMB, JPanel container,  GBC layout){
        SliderOptions options = new SliderOptions(
                new int[]{
                        0,
                        (int) (memoryInMB * 8 * 0.1),
                        memoryInMB * 8
                },
                new String[]{"Max",
                        "Physical"}
        );


        options.setColors(new Color[]{
                new Color(0x6E93DC),
                new Color(0x8BF5B7)
                //new Color(0xFFFFFF)
        });

        options.setTickColors(Arrays.copyOf(options.getColors(),options.getColors().length));

        DrawnSlider slider1 = SliderControlBuilder.createSlider(options);
        container.add(new JLabel("Colored one thumb:"), layout.setPosition(0, 6));
        container.add(SliderControlBuilder.createMemoryRangePanel(slider1), layout.setPosition(0, 7));

    }

    private void drawColoredSliderWithTwoThumbs(int memoryInMB, JPanel container, GBC layout){
        SliderOptions options = new SliderOptions(
                new int[]{
                        0,
                        (int) (memoryInMB * 8  * 0.1),
                        (int) (memoryInMB * 8 * 0.15),
                        memoryInMB * 8
                },
                new String[]{"Initial",
                             "Max",
                             "Physical"}
        );

        options.setColors(new Color[]{
                new Color(0x8BF5B7),
                new Color(0x6E93DC),
                new Color(0xFFFFFF)
        });
        options.setTickColors(Arrays.copyOf(options.getColors(),options.getColors().length));


        DrawnSlider slider1 = SliderControlBuilder.createSlider(options);
        container.add(new JLabel("Colored 2 thumbs:"), layout.setPosition(0, 8));
        container.add(SliderControlBuilder.createMemoryRangePanel(slider1), layout.setPosition(0, 9));

    }

}




