package deltix.util.swing.gridpanel;

import deltix.qsrv.hf.pub.md.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.IOException;
import javax.swing.SwingUtilities;
import static javax.swing.SwingConstants.*;

class MoveDTL extends DropTargetAdapter {
    private final GridPanel         mPanel;
    private final Component         mTarget;
    
    MoveDTL (GridPanel panel, Component target) {
        mTarget = target;
        mPanel = panel;
    }

    private Component     getComponentIfLegal (Transferable trf) {
        Component             c;
        
        try {
            c = (Component) trf.getTransferData (GridPanel.COMP_MOVE_FLAVOR);
        } catch (IOException iox) {
            throw new RuntimeException (iox);
        } catch (UnsupportedFlavorException x) {
            return (null);
        }
        
        if (c == mTarget)
            return (null);
                
        return (c);
    }
    
    @Override
    public void             dragEnter (DropTargetDragEvent e) {
        Component   c = getComponentIfLegal (e.getTransferable ());
        
        if (c == null) {
            e.rejectDrag ();
            return;
        }
        
        e.acceptDrag (DnDConstants.ACTION_MOVE);                               
    }

    @Override
    public void             dragExit (DropTargetEvent e) {
        
    }

    public void             drop (DropTargetDropEvent e) {
        final Component   c = getComponentIfLegal (e.getTransferable ());
        
        if (c == null) {
            e.rejectDrop ();
            return;
        }
        
        e.acceptDrop (DnDConstants.ACTION_MOVE);
        e.dropComplete (true);
        
        Point       p = e.getLocation ();
        Dimension   size = mTarget.getSize ();
        
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
        
        SwingUtilities.invokeLater (
            new Runnable () {
                public void run () {
                    GridPanel.move (c, mTarget, side);
                }
            }
        );
    }
}
