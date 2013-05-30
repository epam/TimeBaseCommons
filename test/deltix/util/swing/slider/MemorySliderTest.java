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
        JPanel main = new JPanel(new GridBagLayout());

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
        SliderOptions options = new SliderOptions(
                new int[]{
                        0,
                        100,
                        (100+512)/2,
                        512,
                        memoryInMB * 16
                },
                new String[]{"Cache",
                        "Initial",
                        "Addl. Reserved",
                        "Unavailable"}
        );

        options.setTickColors(Arrays.copyOf(options.getColors(),options.getColors().length));
        //options.setTickColor(2, Color.BLACK);

        DrawnSlider slider1 = SliderControlBuilder.createSlider(options);
        main.add(new JLabel("Colored:"), layout.setPosition(0, 0).setInsets(5));
        main.add(SliderControlBuilder.createMemoryRangePanel(slider1), layout.setPosition(0, 1));

        //black and white slider
        options = new SliderOptions(
                new int[]{
                        0,
                        (int) (memoryInMB * 0.05),
                        (int) (memoryInMB * 0.14),
                        (int) (memoryInMB * 0.7),
                        memoryInMB
                },
                new String[]{"Cache",
                        "Initial",
                        "Addl. Reserved",
                        "Unavailable"},
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
        main.add(new JLabel("Black-and-White:"), layout.setPosition(0, 2));
        main.add(SliderControlBuilder.createMemoryRangePanel(slider2), layout.setPosition(0, 3));


        //read only slider
        options = options.copy();
        //options.setMinMaxUnderSlider(true);
        options.setReadOnly(true);
        DrawnSlider slider3 = SliderControlBuilder.createSlider(options);
        slider3.setEnabled(false);
        main.add(new JLabel("Read Only:"), layout.setPosition(0, 4));
        main.add(SliderControlBuilder.createMemoryRangePanel(slider3), layout.setPosition(0, 5));

        add(main);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                dispose();
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {

        MemorySliderTest frame = new MemorySliderTest();
        frame.setPreferredSize(new Dimension(600, 350));
        frame.pack();
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(windowSize.width/2 - frame.getWidth()/2, windowSize.height/2 - frame.getHeight()/2);

        frame.setVisible(true);

    }

}




