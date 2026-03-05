package deltix.util.swing;

import java.awt.*;
import java.awt.image.*;

public class Java2DIconFactory {

    public static BufferedImage createErrorIcon () {
        return createErrorIcon (7,
                                8);
    }

    public static BufferedImage createErrorIcon (final int width,
                                                 final int height) {
        final BufferedImage icon = new BufferedImage (width,
                                                      height,
                                                      BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = (Graphics2D) icon.getGraphics ();
        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint (RenderingHints.KEY_STROKE_CONTROL,
                             RenderingHints.VALUE_STROKE_PURE);
        g2.setColor (Color.RED);
        g2.fillRect (0,
                     0,
                     width,
                     height);
        g2.setColor (Color.WHITE);
        g2.drawLine (0,
                     0,
                     width,
                     height);
        g2.drawLine (0,
                     height,
                     width,
                     0);
        g2.dispose ();
        return icon;
    }

}