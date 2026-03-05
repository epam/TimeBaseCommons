package deltix.util.awt;

import java.awt.*;

public class AwtUtil {
    
 	public static Point centerComponent (Rectangle rExt, Rectangle rInt) {
	    int x = rExt.x + (rExt.width - rInt.width)/2;
	    int y = rExt.y + (rExt.height - rInt.height)/2;
	    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	    if (x > screenDim.width)
	        x = screenDim.width - rInt.width;
	    if (y > screenDim.height)
	        x = screenDim.height - rInt.height;
	    if (x < 0)
	        x = 0;
	    if (y < 0)
	        y = 0;
	    return new Point (x, y);
	}
   
    public static void  union (Dimension out, Dimension add) {
        if (add.width > out.width)
            out.width = add.width;
        
        if (add.height > out.height)
            out.height = add.height;
    }

    public static void  inflate (Rectangle out, int margin) {
        inflate (out, margin, margin, margin, margin);
    }

    public static void  inflate (Rectangle out, int top, int left, int bottom, int right) {
        out.x -= left;
        out.y -= top;
        out.width += left + right;
        out.height += top + bottom;
    }

    public static Color brighter (Color c, double factor) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int)(1.0/(1.0-factor));
        if ( r == 0 && g == 0 && b == 0) {
           return new Color(i, i, i);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/factor), 255),
                         Math.min((int)(g/factor), 255),
                         Math.min((int)(b/factor), 255));
    }

    /**
     *  Draw a 3d barrel ("database symbol") using specified colors and
     *  current stroke. Supply null for color if line or background fill is not
     *  desired. This method does not restore current color in the graphics
     *  context.
     */
    public static void  draw3DBarrel (
        Graphics2D          g2,
        int                 left,
        int                 top,
        int                 width,
        int                 height,
        int                 ovalHeight,
        Color               bgColor,
        Color               lineColor        
    )
    {
        int         w1 = width - 1;
        int         h1 = height - 1;
        int         bottom1 = top + h1;
        int         halfDepth = ovalHeight / 2;
        int         h1a2 = h1 - halfDepth;

        if (bgColor != null) {
            g2.setColor (bgColor);
            g2.fillOval (left, top, w1, ovalHeight);
            g2.fillOval (left, bottom1 - ovalHeight, w1, ovalHeight);
            g2.fillRect (left, top + halfDepth, width, height - ovalHeight);
        }

        if (lineColor != null) {
            g2.setColor (lineColor);
            g2.drawLine (left, top + halfDepth, 0, h1a2);
            g2.drawLine (left + w1, top + halfDepth, w1, h1a2);
            g2.drawOval (left, top, w1, ovalHeight);
            g2.drawArc (left, bottom1 - ovalHeight, w1, ovalHeight, 0, -180);
        }
    }

    public static void  draw3DBox (
        Graphics2D          g2,
        int                 left,
        int                 top,
        int                 width,
        int                 height,
        int                 dx,
        int                 dy,
        Color               bgColor,
        Color               lineColor
    )
    {
        int         bx = width - dx;
        int         bx1 = bx - 1;
        int         h1 = height - 1;
        int         w1 = width - 1;
        int         lw1 = left + w1;
        int         lbx1 = left + bx1;
        int         toph1 = top + h1;
        int         topdy = top + dy;
        int []      xs = { left, left + dx, lw1, lw1, lbx1, left };
        int []      ys = { topdy, top, top, toph1 - dy, toph1, toph1 };

        if (bgColor != null) {
            g2.setColor (bgColor);
            g2.fillPolygon (xs, ys, 6);
        }

        if (lineColor != null) {
            g2.setColor (lineColor);
            g2.drawPolygon (xs, ys, 6);
            g2.drawLine (lbx1, topdy, lw1, top);
            g2.drawLine (lbx1, topdy, left, top + dy);
            g2.drawLine (lbx1, topdy, lbx1, toph1);
        }
    }
}
