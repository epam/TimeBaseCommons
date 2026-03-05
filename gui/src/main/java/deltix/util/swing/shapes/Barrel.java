package deltix.util.swing.shapes;

import deltix.util.awt.*;
import java.awt.*;
import javax.swing.JComponent;

/**
 *  3D barrel component with optional text label.
 */
public class Barrel extends JComponent {
    private Stroke                          stroke = new BasicStroke (1);
    private Color                           lineColor = Color.black;
    private String                          text = null;
    private int                             ovalDepth = 15;

    public Barrel () {
    }

    public int              getOvalDepth () {
        return ovalDepth;
    }

    public void             setOvalDepth (int ovalDepth) {
        this.ovalDepth = ovalDepth;
        repaint ();
    }

    public String           getText () {
        return text;
    }

    public void             setText (String text) {
        this.text = text;
        repaint ();
    }

    public Stroke           getStroke () {
        return stroke;
    }

    public void             setStroke (Stroke stroke) {
        this.stroke = stroke;
        repaint ();
    }

    public Color            getLineColor () {
        return lineColor;
    }

    public void             setLineColor (Color lineColor) {
        this.lineColor = lineColor;
        repaint ();
    }

    @Override
    protected void          paintComponent (Graphics g) {
        super.paintComponent (g);

        Graphics2D  g2 = (Graphics2D) g;

        Stroke      saveStroke = g2.getStroke ();
        Color       saveColor = g2.getColor ();
        

        g2.setStroke (stroke);

        AwtUtil.draw3DBarrel (
            g2,
            0, 0, getWidth (), getHeight (), ovalDepth,
            getBackground (), lineColor
        );
                
        if (text != null) {
            Font        saveFont = g2.getFont ();

            g2.setFont (getFont ());
            g2.setColor (getForeground ());
            TextDisplay.centerText (g, text, getWidth () / 2, getHeight () / 2 + ovalDepth / 4);
            g2.setFont (saveFont);
        }

        g2.setStroke (saveStroke);
        g2.setColor (saveColor);
        
    }
}
