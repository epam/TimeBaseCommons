package deltix.util.swing.gridpanel;

import deltix.util.swing.shapes.Spacer;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import static javax.swing.SwingConstants.*;

class MoveDTL extends GridPanelDTL {
    public static void      install (JComponent target) {
        new MoveDTL (target);
    }
    
    private static final Color  IND_COLOR =
        new Color (255, 200, 128);
    
    private JComponent      mIndicator = null;
    private Rectangle       mRect = new Rectangle ();
    
    private MoveDTL (JComponent target) {
        super (target);
    }

    @Override
    public void             dragEnter (DropTargetDragEvent e) {
        final JComponent         dragged = getComponentIfLegal (e.getTransferable ());
        
        if (dragged == null) {
            e.rejectDrag ();
            return;
        }
 
        mIndicator = new Spacer (1, 1);
        mIndicator.setOpaque (true);
        mIndicator.setBackground (IND_COLOR);
        
        target.add (mIndicator);       
    }

    private void            removeIndicator () {
        if (mIndicator != null) {
            target.remove (mIndicator);
            target.repaint ();
        }
    }
    
    @Override
    public void             dragExit (DropTargetEvent e) {
        removeIndicator ();
    }

    
    @Override
    public void             dragOver (DropTargetDragEvent e) {
        final JComponent         dragged = getComponentIfLegal (e.getTransferable ());
        
        if (dragged == null) {
            e.rejectDrag ();
            return;
        }
        
        e.acceptDrag (DnDConstants.ACTION_MOVE);
        
        int         side = getSide (e.getLocation ());        
        
        mRect.width = target.getWidth ();
        mRect.height = target.getHeight ();
        mRect.x = mRect.y = 0;
        
        switch (side) {
            case TOP:       
                mRect.height /= 2;
                break;
                
            case BOTTOM:    
                mRect.y += mRect.height / 2; 
                break;
                
            case LEFT:      
                mRect.width /= 2; 
                break;
                
            case RIGHT:    
                mRect.x += mRect.width / 2; 
                break;
        }
        
        mIndicator.setBounds (mRect);
        mIndicator.repaint ();
    }

    private int             getSide (Point p) {
        Dimension   size = target.getSize ();
        
        double      k = ((double) size.height) / size.width;
            
        boolean     leftbottom = p.y > p.x * k;        
        boolean     lefttop = p.y < size.height - p.x * k;
        final int   side;
        
        return (
            leftbottom ? 
                lefttop ? LEFT : BOTTOM :
                lefttop ? TOP : RIGHT
        );
    }
    
    protected void          executeDrop (
        JComponent              dragged,
        Point                   p
    )
    {
        removeIndicator ();
        
        int         side = getSide (p);
        
        if (target instanceof GridPanel)
            ((GridPanel) target).addComponent (dragged, side);
        else
            GridPanel.move (dragged, target, side);               
    }
}
