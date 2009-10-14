package deltix.util.swing.shapes;

import deltix.util.awt.*;
import java.awt.*;
import javax.swing.JComponent;

/**
 *  2D rounded box component with optional text label.
 */
public class RoundedBox extends JComponent {
    private Stroke                          stroke = new BasicStroke (1);
    private Color                           lineColor = Color.black;
    private String                          text = null;
    private int                             arcRadius = 15;

    public RoundedBox () {
    }

    public int              getArcRadius () {
        return arcRadius;
    }

    public void             setArcRadius (int arcRadius) {
        this.arcRadius = arcRadius;
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
        Color       saveColor = g2.getColor ();

        int         h1 = getHeight () - 1;
        int         w1 = getWidth () - 1;

        if (getBackground () != null) {
            g2.setColor (getBackground ());
            g2.fillRoundRect (0, 0, w1, h1, arcRadius, arcRadius);
        }

        if (lineColor != null) {
            Stroke      saveStroke = g2.getStroke ();

            g2.setColor (lineColor);
            g2.setStroke (stroke);
            g2.drawRoundRect (0, 0, w1, h1, arcRadius, arcRadius);
            g2.setStroke (saveStroke);
        }

        if (text != null) {
            Font        saveFont = g2.getFont ();

            g2.setFont (getFont ());
            g2.setColor (getForeground ());
            TextDisplay.centerText (g, text, getWidth () / 2, getHeight () / 2);
            g2.setFont (saveFont);
        }
        
        g2.setColor (saveColor);        
    }
}
