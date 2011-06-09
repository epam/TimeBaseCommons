package deltix.util.swing.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.tree.TreePath;

/**
 *  Helper class for invoking node-dependent pop-up menus on trees.
 */
public abstract class TreePopupInvoker extends MouseAdapter {    
    @Override
    public void         mousePressed (MouseEvent e) {
        maybeShowPopup (e);
    }

    @Override
    public void         mouseReleased (MouseEvent e) {
        maybeShowPopup (e);
    }

    protected abstract JPopupMenu   getMenuForPath (JTree tree, TreePath path);
    
    private void        maybeShowPopup (MouseEvent e) {
        if (e.isPopupTrigger ()) {
            JTree       tree = (JTree) e.getComponent ();            
            TreePath    path = tree.getPathForLocation (e.getX (), e.getY ());            
            JPopupMenu  menu = getMenuForPath (tree, path);
            
            if (menu != null)
                menu.show (tree, e.getX (), e.getY ());
        }                
    }
}
