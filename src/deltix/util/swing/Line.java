package deltix.util.swing;

import javax.swing.*;
import java.awt.*;

/**
 *  Draws a line
 */
public class Line extends JComponent {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;
    
    private int             mOrientation;
    private Stroke          mStroke;
    
    public Line (int orientation) {
        this (orientation, new BasicStroke (1));
    }
    
    public Line (int orientation, Stroke stroke) {
       mOrientation = orientation;
       mStroke = stroke;        
    }
    
    public Line (int orientation, Stroke stroke, Color color) {
        this (orientation, stroke);
        setForeground (color);
    }
    
    /**
     *  Returns the <i>orientation</i> property.
     */
    public int                              getOrientation () {
        return (mOrientation);
    }

    /**
     *  Assigns the <i>orientation</i> property.
     */
    public void                             setOrientation (int value) {
        mOrientation = value;
        repaint ();
    }

    /**
     *  Returns the <i>stroke</i> property.
     */
    public Stroke                           getStroke () {
        return (mStroke);
    }

    /**
     *  Assigns the <i>stroke</i> property.
     */
    public void                             setStroke (Stroke value) {
        mStroke = value;
        repaint ();
    }
    
    public void     paint (Graphics g) {
        int             w = getWidth ();
        int             h = getHeight ();
        int             x0; 
        int             y0; 
        int             x1; 
        int             y1; 
        
        if (mOrientation == HORIZONTAL) {
            y0 = y1 = h / 2;
            x0 = 0;
            x1 = w;
        }
        else {
            x0 = x1 = w / 2;
            y0 = 0;
            y1 = h;
        }
            
        Graphics2D      g2 = (Graphics2D) g;
        
        g2.setColor (getForeground ());
        g2.setStroke (mStroke);
        
        g2.drawLine (x0, y0, x1, y1);
    }
}
