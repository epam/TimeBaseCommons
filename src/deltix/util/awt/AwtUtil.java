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
   
    public static void  inflate (Dimension out, Dimension add) {
        if (add.width > out.width)
            out.width = add.width;
        
        if (add.height > out.height)
            out.height = add.height;
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
    
}
