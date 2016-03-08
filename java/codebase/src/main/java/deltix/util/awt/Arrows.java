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

    public static void      makeFilledHorizontalArrow (
        int                     left,
        int                     right,
        int                     y,
        int                     thickness,
        int                     headLength,
        int                     headWidth,
        boolean                 leftArrow,
        boolean                 rightArrow,
        Polygon                 p
    )
    {
        p.reset ();

        int                     t2 = thickness / 2;
        int                     hw2 = headWidth / 2;
        int                     bottomIn = y - t2;
        int                     topIn = y + t2;
        int                     bottomOut = y - hw2;
        int                     topOut = y + hw2;
        
        if (leftArrow) {
            int                 x = left + headLength;

            p.addPoint (x, bottomIn);
            p.addPoint (x, bottomOut);
            p.addPoint (left, y);
            p.addPoint (x, topOut);
            p.addPoint (x, topIn);
        }
        else {
            p.addPoint (left, bottomIn);
            p.addPoint (left, topIn);
        }

        if (rightArrow) {
            int                 x = right - headLength;

            p.addPoint (x, topIn);
            p.addPoint (x, topOut);
            p.addPoint (right, y);
            p.addPoint (x, bottomOut);
            p.addPoint (x, bottomIn);
        }
        else {
            p.addPoint (right, topIn);
            p.addPoint (right, bottomIn);
        }
    }

    public static void      makeFilledVerticalArrow (
        int                     x,
        int                     top,
        int                     bottom,
        int                     thickness,
        int                     headLength,
        int                     headWidth,
        boolean                 topArrow,
        boolean                 bottomArrow,
        Polygon                 p
    )
    {
        p.reset ();

        int                     t2 = thickness / 2;
        int                     hw2 = headWidth / 2;
        int                     leftIn = x - t2;
        int                     rightIn = x + t2;
        int                     leftOut = x - hw2;
        int                     rightOut = x + hw2;

        if (topArrow) {
            int                 y = top + headLength;

            p.addPoint (leftIn, y);
            p.addPoint (leftOut, y);
            p.addPoint (x, top);
            p.addPoint (rightOut, y);
            p.addPoint (rightIn, y);
        }
        else {
            p.addPoint (leftIn, top);
            p.addPoint (rightIn, top);
        }

        if (bottomArrow) {
            int                 y = bottom - headLength;

            p.addPoint (rightIn, y);
            p.addPoint (rightOut, y);
            p.addPoint (x, bottom);
            p.addPoint (leftOut, y);
            p.addPoint (leftIn, y);
        }
        else {
            p.addPoint (rightIn, bottom);
            p.addPoint (leftIn, bottom);
        }
    }
}
