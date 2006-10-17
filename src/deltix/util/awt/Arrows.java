package deltix.util.awt;

import java.awt.*;

public class Arrows {
    public static final int     STYLE_OPEN = 1;
    public static final int     STYLE_CLOSED = 2;
    public static final int     STYLE_FILLED = 3;
    
    public static final int     POSITION_MIDDLE = 1;
    public static final int     POSITION_END = 2;
    
    /**
     *  Draws a line between (x0, y0) and (x1, y1), with a directional arrow 
     *  in the middle. The arrowhead is 2*a pixels long and 2*b pixels wide,
     *  total.
     *
     */
    public static void drawArrow (Graphics g, int x0, int y0, int x1, int y1, int a, int b) {
        drawArrow (g, x0, y0, x1, y1, a, b, POSITION_MIDDLE, STYLE_OPEN);
    }
    
    /**
     *  Draws a line between (x0, y0) and (x1, y1), with a directional arrow 
     *  in the middle. The arrowhead is 2*a pixels long and 2*b pixels wide,
     *  total.
     *
     */
    public static void drawArrow (
        Graphics        g, 
        int             x0, 
        int             y0, 
        int             x1, 
        int             y1, 
        int             a, 
        int             b,
        int             pos,
        int             style
    ) 
    {
        int []  xs = new int [3];
        int []  ys = new int [3];
        
        getArrowHead (x0, y0, x1, y1, a, b, pos, xs, ys);
                
        g.drawLine (x0, y0, x1, y1);
        
        if (style == STYLE_FILLED) 
            g.fillPolygon (xs, ys, 3);
        else {     
            g.drawLine (xs [0], ys [0], xs [1], ys [1]);
            g.drawLine (xs [1], ys [1], xs [2], ys [2]);
            
            if (style == STYLE_CLOSED)
                g.drawLine (xs [0], ys [0], xs [2], ys [2]);
        }
    }
    
    /**
     *  Returns arrowhead coordinates. The arrowhead is 2*a pixels long and 2*b pixels wide,
     *  total.
     *
     */
    public static void getArrowHead (
        int             x0, 
        int             y0, 
        int             x1, 
        int             y1, 
        int             a, 
        int             b,
        int             pos,
        int []          retXs,
        int []          retYs
    ) 
    {
        double          dx = x1 - x0;
        double          dy = y1 - y0;
        double          l = Math.sqrt (dx * dx + dy * dy);
        double          ka = a / l;
        double          dxa = ka * dx;
        double          dya = ka * dy;
        double          kb = b / l;
        double          dxb = kb * dx;
        double          dyb = kb * dy;
        int             px, py;
        
        if (pos == POSITION_MIDDLE) {
            double          mx = (x1 + x0) * 0.5;
            double          my = (y1 + y0) * 0.5;

            px = (int) (mx + dxa);
            py = (int) (my + dya);
        }
        else if (pos == POSITION_END) {
            px = x1;
            py = y1;
        }
        else
            throw new IllegalArgumentException ("pos == " + pos);
            
        double          tx = px - dxa - dxa;
        double          ty = py - dya - dya;
        int             lx = (int) (tx + dyb);
        int             ly = (int) (ty - dxb);
        int             rx = (int) (tx - dyb);
        int             ry = (int) (ty + dxb);
                
        retXs [0] = lx;
        retXs [1] = px;
        retXs [2] = rx;
        retYs [0] = ly;
        retYs [1] = py;
        retYs [2] = ry;
    }
}
