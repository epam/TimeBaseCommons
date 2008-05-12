package deltix.util.swing.gridpanel;

import deltix.util.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.util.WeakHashMap;
import javax.swing.*;
import static javax.swing.SwingConstants.*;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

/**
 *
 */
public class GridPanel extends JPanel {
    static final DataFlavor     COMP_MOVE_FLAVOR =
        LocalObjectTransferable.getLocalObjectFlavor (JComponent.class);
    
    static final DragSource     DS = new DragSource ();
    static final WeakHashMap <JComponent, JComponent> DRAGGABLES =
        new WeakHashMap <JComponent, JComponent> ();
    
    private static final MoveDGL       mDGL = new MoveDGL ();
    
    public GridPanel () { 
        super (new BorderLayout ());
    }
    
    public static boolean       isLocated (JComponent c, JComponent ref, int side) {
        Container   parent = c.getParent ();
        
        if (parent != ref.getParent ())
            return (false);
      
        assert parent instanceof JSplitPane : "Only JSplitPane is handled.";
        
        JSplitPane  split = (JSplitPane) parent;
        int         orient = split.getOrientation ();
        boolean     refIsFirst = ref == split.getTopComponent ();
        
        switch (side) {
            case LEFT:      return (orient == HORIZONTAL_SPLIT && !refIsFirst);
            case RIGHT:     return (orient == HORIZONTAL_SPLIT && refIsFirst);
            case TOP:       return (orient == VERTICAL_SPLIT && !refIsFirst);
            case BOTTOM:    return (orient == VERTICAL_SPLIT && refIsFirst);
            default:    throw new IllegalArgumentException ("Side: " + side);
        }
    }
    
    public static JSplitPane   addComponent (JComponent c, JComponent ref, int side) {
        JSplitPane                  ret;
        
        setupDraggable (c);
        
        JComponent                   parent = (JComponent) ref.getParent ();
                
        if (parent instanceof GridPanel) {
            parent.remove (ref); 
            ret = createSplit (ref, c, side);
            parent.add (ret, BorderLayout.CENTER);
            parent.revalidate ();
            parent.repaint ();
        }
        else {
            JSplitPane      split = (JSplitPane) parent;
            
            int             loc = split.getDividerLocation ();
            
            if (ref == split.getTopComponent ()) {
                split.remove (ref);
                ret = createSplit (ref, c, side);
                split.setTopComponent (ret);
            }
            else {
                split.remove (ref);
                ret = createSplit (ref, c, side);
                split.setBottomComponent (ret);
            }
            
            split.setDividerLocation (loc);
            split.revalidate ();
            split.repaint ();
        }
        
        return (ret);
    }
    
    public static void     replace (JComponent ref, JComponent c) {
        JComponent               parent = (JComponent) ref.getParent ();
                
        if (parent instanceof GridPanel) {
            parent.remove (ref);        
            parent.add (c, BorderLayout.CENTER);
            parent.revalidate ();
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
            split.revalidate ();
            split.repaint ();
        }
    }
    
    public static void      removeChild (JComponent c) {
        Container               parent = c.getParent ();
        
        if (parent == null)
            return;
                        
        if (parent instanceof GridPanel) {        
            parent.remove (c);
            parent.repaint ();
        }
        else {
            JSplitPane      split = (JSplitPane) parent;
            
            JComponent       other = (JComponent) split.getTopComponent ();
            
            if (other == c)
                other = (JComponent) split.getBottomComponent ();
            
            split.removeAll ();
            replace (split, other);
        }
    }
    
    private static void     swap (JSplitPane split) {
        int         loc = split.getDividerLocation ();        
        int         range = 
            split.getOrientation () == VERTICAL_SPLIT ?
                split.getHeight () :
                split.getWidth ();
        
        JComponent   a = (JComponent) split.getTopComponent ();
        JComponent   b = (JComponent) split.getBottomComponent ();
        
        split.remove (a);
        split.remove (b);
        split.setTopComponent (b);
        split.setBottomComponent (a);
        split.setDividerLocation (range - split.getDividerSize () - loc);
        split.repaint ();
    }
    
    private static int      curSide (JComponent c) {
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
    
    public static void      move (JComponent c, JComponent ref, int side) {
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
    
    private static JSplitPane createSplit (JComponent ref, JComponent c, int side) {
        switch (side) {
            case LEFT:      return (new JSplitPane (HORIZONTAL_SPLIT, c, ref));
            case RIGHT:     return (new JSplitPane (HORIZONTAL_SPLIT, ref, c));
            case TOP:       return (new JSplitPane (VERTICAL_SPLIT, c, ref));
            case BOTTOM:    return (new JSplitPane (VERTICAL_SPLIT, ref, c));
            default:    throw new IllegalArgumentException ("Side: " + side);
        }
    }
    
    private static void     setupDraggable (JComponent c) {
        if (DRAGGABLES.put (c, c) == null) {        
            MoveDTL.install (c);
            DS.createDefaultDragGestureRecognizer (c, DnDConstants.ACTION_MOVE, mDGL);
        }
    }
    
    public void             addComponent (JComponent c, int side) {
           setupDraggable (c);
        
        if (getComponentCount () == 0) 
            add (c, BorderLayout.CENTER);            
        else {            
            JComponent       child = (JComponent) getComponent (0);                        
            removeAll ();
            add (createSplit (child, c, side), BorderLayout.CENTER);
        }
        
        repaint ();
    }
}
