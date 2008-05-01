package deltix.util.swing;

import javax.swing.*;
import java.awt.*;

/**
 *  Empty component
 */
public class Spacer extends JComponent {
    public Spacer (int width, int height) {
        this (new Dimension (width, height));
    }
    
    public Spacer (Dimension size) {
        setPreferredSize (size);
        setSize (size);
    }    
}
