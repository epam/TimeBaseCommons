package deltix.util.swing.gridpanel;

import deltix.util.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import javax.swing.*;
import static javax.swing.SwingConstants.*;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

/**
 *
 */
public class GridPanel extends JPanel {
    static final DataFlavor     COMP_MOVE_FLAVOR =
        LocalObjectTransferable.getLocalObjectFlavor (Component.class);
    
    static final DragSource     DS = DragSource.getDefaultDragSource ();
    
    private final MoveDGL       mDGL = new MoveDGL ();
    
    public GridPanel () { 
        super (new BorderLayout ());
    }
    
    public static void         addComponent (Component c, Component ref, int side) {
        Container                   parent = ref.getParent ();
                
        if (parent instanceof GridPanel) {
            parent.remove (ref);        
            parent.add (createSplit (ref, c, side), BorderLayout.CENTER);
            parent.repaint ();
        }
        else {
            JSplitPane      split = (JSplitPane) parent;
            
            int             loc = split.getDividerLocation ();
            
            if (ref == split.getTopComponent ()) {
                split.remove (ref);
                split.setTopComponent (createSplit (ref, c, side));
            }
            else {
                split.remove (ref);
                split.setBottomComponent (createSplit (ref, c, side));
            }
            
            split.setDividerLocation (loc);
            split.repaint ();
        }
    }
    
    private static void     replace (Component ref, Component c) {
        Container               parent = ref.getParent ();
                
        if (parent instanceof GridPanel) {
            parent.remove (ref);        
            parent.add (c, BorderLayout.CENTER);
            parent.repaint ();
        }
        else {
            JSplitPane      split = (JSplitPane) parent;
            
            int             loc = split.getDividerLocation ();
            
            if (ref == split.getTopComponent ()) {
                split.remove (ref);
                split.setTopComponent (c);
            }
            else {
                split.remove (ref);
                split.setBottomComponent (c);
            }
            
            split.setDividerLocation (loc);
            split.repaint ();
        }
    }
    
    public static void      removeChild (Component c) {
        Container               parent = c.getParent ();
        
        if (parent instanceof GridPanel) {
            parent.remove (c);
            parent.repaint ();
        }
        else {
            JSplitPane      split = (JSplitPane) parent;
            
            Component       other = split.getTopComponent ();
            
            if (other == c)
                other = split.getBottomComponent ();
            
            split.remove (other);
            replace (split, other);
        }
    }
    
    private static void     swap (JSplitPane split) {
        int         loc = split.getDividerLocation ();        
        int         range = 
            split.getOrientation () == VERTICAL_SPLIT ?
                split.getHeight () :
                split.getWidth ();
        
        Component   a = split.getTopComponent ();
        Component   b = split.getBottomComponent ();
        
        split.remove (a);
        split.remove (b);
        split.setTopComponent (b);
        split.setBottomComponent (a);
        split.setDividerLocation (range - split.getDividerSize () - loc);
        split.repaint ();
    }
    
    private static int      curSide (Component c) {
        JSplitPane      split = (JSplitPane) c.getParent ();
        int             orient = split.getOrientation ();
        
        if (c == split.getTopComponent ())
            return (orient == VERTICAL_SPLIT ? TOP : LEFT);
        else
            return (orient == VERTICAL_SPLIT ? BOTTOM : RIGHT);
    }
    
    private static boolean  sameAxis (int a, int b) {
        return ((a == LEFT || a == RIGHT) == (b == LEFT || b == RIGHT));
    }
    
    public static void      move (Component c, Component ref, int side) {
        if (c.getParent () == ref.getParent ()) {
            int             curSide = curSide (c);
            
            if (side == curSide)
                return;
            
            if (sameAxis (side, curSide)) {            
                swap ((JSplitPane) c.getParent ());
                return;
            }
        }
        
        removeChild (c);
        addComponent (c, ref, side);        
    }
    
    private static JSplitPane createSplit (Component ref, Component c, int side) {
        switch (side) {
            case LEFT:      return (new JSplitPane (HORIZONTAL_SPLIT, c, ref));
            case RIGHT:     return (new JSplitPane (HORIZONTAL_SPLIT, ref, c));
            case TOP:       return (new JSplitPane (VERTICAL_SPLIT, c, ref));
            case BOTTOM:    return (new JSplitPane (VERTICAL_SPLIT, ref, c));
            default:    throw new IllegalArgumentException ("Side: " + side);
        }
    }
    
    private void            setupNewChild (Component c) {
        new DropTarget (c, DnDConstants.ACTION_MOVE, new MoveDTL (this, c));
        DS.createDefaultDragGestureRecognizer (c, DnDConstants.ACTION_MOVE, mDGL);
    }
    
    public void             addComponent (Component c, int side) {
        setupNewChild (c);
        
        if (getComponentCount () == 0) 
            add (c, BorderLayout.CENTER);            
        else {            
            Component       child = getComponent (0);                        
            removeAll ();
            add (createSplit (child, c, side), BorderLayout.CENTER);
        }
        
        repaint ();
    }
}
