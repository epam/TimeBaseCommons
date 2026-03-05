package deltix.util.swing.gridpanel;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.IOException;
import javax.swing.*;
import static javax.swing.SwingConstants.*;

public abstract class GridPanelDTL extends DropTargetAdapter {
    protected final JComponent       target;
    
    GridPanelDTL (JComponent inTarget) {
        target = inTarget;
        new DropTarget (inTarget, DnDConstants.ACTION_MOVE, this);
    }

    protected JComponent     getComponentIfLegal (Transferable trf) {
        JComponent             c;
        
        try {
            c = (JComponent) trf.getTransferData (GridPanel.COMP_MOVE_FLAVOR);
        } catch (IOException iox) {
            throw new RuntimeException (iox);
        } catch (UnsupportedFlavorException x) {
            return (null);
        }
        
        if (c == target)
            return (null);
                
        return (c);
    }
    
    @Override
    public void             dragEnter (DropTargetDragEvent e) {
        JComponent   c = getComponentIfLegal (e.getTransferable ());
        
        if (c == null) {
            e.rejectDrag ();
            return;
        }
        
        e.acceptDrag (DnDConstants.ACTION_MOVE);                               
    }

    @Override
    public void             dragExit (DropTargetEvent e) {
        
    }

    protected abstract void executeDrop (
        JComponent               dragged,
        Point                   dropLocation
    );
    
    public void             drop (DropTargetDropEvent e) {
        final JComponent   c = getComponentIfLegal (e.getTransferable ());
        
        if (c == null) {
            e.rejectDrop ();
            return;
        }
        
        e.acceptDrop (DnDConstants.ACTION_MOVE);
        e.dropComplete (true);
        
        final Point       p = e.getLocation ();
        
        SwingUtilities.invokeLater (
            new Runnable () {
                public void run () {
                    executeDrop (c, p);
                }
            }
        );
    }
}
