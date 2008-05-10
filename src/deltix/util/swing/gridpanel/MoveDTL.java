package deltix.util.swing.gridpanel;

import deltix.qsrv.hf.pub.md.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import static javax.swing.SwingConstants.*;

class MoveDTL extends GridPanelDTL {
    public static void      install (Component target) {
        new MoveDTL (target);
    }
    
    private MoveDTL (Component target) {
        super (target);
    }

    protected void          executeDrop (
        Component               dragged,
        Point                   p
    )
    {
        Dimension   size = target.getSize ();
        
        double      k = ((double) size.height) / size.width;
            
        boolean     leftbottom = p.y > p.x * k;        
        boolean     lefttop = p.y < size.height - p.x * k;
        final int   side;
        
        if (leftbottom)
            if (lefttop)
                side = LEFT;
            else
                side = BOTTOM;
        else
            if (lefttop)
                side = TOP;
            else
                side = RIGHT;
        
        
        GridPanel.move (dragged, target, side);               
    }
}
