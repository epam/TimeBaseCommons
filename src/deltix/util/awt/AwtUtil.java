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
    
    
}
