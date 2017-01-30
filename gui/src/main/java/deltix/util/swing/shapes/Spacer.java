package deltix.util.swing.shapes;

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

    @Override
    protected void paintComponent (Graphics g) {
        if (isOpaque ()) {
            g.setColor (getBackground ());
            g.fillRect (0, 0, getWidth (), getHeight ());
        }
    }
    
    
}
