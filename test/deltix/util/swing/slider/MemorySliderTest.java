package deltix.util.swing.slider;

import deltix.util.swing.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * User: TurskiyS
 * Date: 4/8/13
 */
public class MemorySliderTest extends JFrame {

    public MemorySliderTest() {
        super("Memory Slider UI Test");
        JPanel main = new JPanel(new GridBagLayout());

        GBC layout = new GBC().setFill(GBC.HORIZONTAL).
                setWeight(1, 0).setAnchor(GBC.WEST);
        SliderOptions options = new SliderOptions();
        options.setOverrideThumb(false);
        main.add(SliderControlBuilder.createMemoryRangePanel(options), layout.setPosition(0, 0));

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
        frame.pack();
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(windowSize.width/2 - frame.getWidth()/2, windowSize.height/2 - frame.getHeight()/2);
        frame.setVisible(true);

    }

}




