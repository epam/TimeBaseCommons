package deltix.util.swing.shapes;

import javax.swing.*;
import java.awt.*;

import deltix.util.awt.*;

/**
 *  Draws an arrow.
 *
 *  @see deltix.util.awt.Arrows
 */
public class Arrow extends JComponent {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;
    
    private int             mOrientation;
    private int             mPosition;
    private int             mStyle;
    private Stroke          mStroke;
    private int             mArrowHeadWidth;
    private int             mArrowHeadLength;
    
    public Arrow (
        int                 orientation,
        int                 width,
        int                 length
    )
    {
        this (orientation, Arrows.POSITION_END, Arrows.STYLE_FILLED, width, length);
    }
    
    public Arrow (
        int                 orientation,
        int                 position,
        int                 style,
        int                 width,
        int                 length
    )
    {
        this (
            orientation, 
            position, 
            style, 
            width, 
            length, 
            Color.black, 
            new BasicStroke (1)
        );
    }
    
    public Arrow (
        int                 orientation,
        int                 position,
        int                 style,
        int                 width,
        int                 length,
        Color               color,
        Stroke              stroke
    )
    {
        mOrientation = orientation;
        mPosition = position;
        mStyle = style;
        mArrowHeadWidth = width;
        mArrowHeadLength = length;
        mStroke = stroke;
        setForeground (color);
        
        Dimension   prefSize = new Dimension ();
        Dimension   minSize = new Dimension ();
        
        if (mOrientation == VERTICAL) {
            minSize.width = prefSize.width = mArrowHeadWidth;
            minSize.height = mArrowHeadLength + 1;
            prefSize.height = mArrowHeadLength * 2;
        }
        else {
            minSize.height = prefSize.height = mArrowHeadWidth;
            prefSize.width = mArrowHeadLength * 2; 
            minSize.width = mArrowHeadLength + 1;
        }
            
        setPreferredSize (prefSize);
        setMinimumSize (minSize);
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
    }

    /**
     *  Returns the <i>position</i> property.
     */
    public int                              getPosition () {
        return (mPosition);
    }

    /**
     *  Assigns the <i>position</i> property.
     */
    public void                             setPosition (int value) {
        mPosition = value;
    }

    /**
     *  Returns the <i>style</i> property.
     */
    public int                              getStyle () {
        return (mStyle);
    }

    /**
     *  Assigns the <i>style</i> property.
     */
    public void                             setStyle (int value) {
        mStyle = value;
    }

    /**
     *  Returns the <i>arrowHeadLength</i> property.
     */
    public int                              getArrowHeadLength () {
        return (mArrowHeadLength);
    }

    /**
     *  Assigns the <i>arrowHeadLength</i> property.
     */
    public void                             setArrowHeadLength (int value) {
        mArrowHeadLength = value;
    }

    /**
     *  Returns the <i>arrowHeadWidth</i> property.
     */
    public int                              getArrowHeadWidth () {
        return (mArrowHeadWidth);
    }

    /**
     *  Assigns the <i>arrowHeadWidth</i> property.
     */
    public void                             setArrowHeadWidth (int value) {
        mArrowHeadWidth = value;
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
    }
    
    @Override
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
        
        Arrows.drawArrow (
            g, x0, y0, x1, y1, 
            mArrowHeadLength / 2, 
            mArrowHeadWidth / 2,
            mPosition,
            mStyle
        );
    }
}
