package deltix.util.swing.gridpanel;

import java.awt.*;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import javax.swing.*;

/**
 *
 */
public class TabDTL extends GridPanelDTL {
    public static void      install (Component tab) {
        new TabDTL (tab);
    }
    
    private Color           mSaveFGColor = null;
    
    private TabDTL (Component tab) {
        super (tab);
    }

    private JTabbedPane     getTabbedPane () {
        Component       c = target.getParent ();
        
        while (!(c instanceof JTabbedPane))
            c = c.getParent ();

        return ((JTabbedPane) c);
    }

    /**
     *  Override to change behavior. Default implementation
     *  sets target foreground to red.
     */
    public void             highlight () {
        mSaveFGColor = target.getForeground ();
        target.setForeground (Color.red);
    }
    
    /**
     *  Override to change behavior. Default implementation 
     *  restores target foreground.
     */
    public void             unhighlight () {
        if (mSaveFGColor != null) {
            target.setForeground (mSaveFGColor);
            mSaveFGColor = null;
        }
    }
    
    @Override
    public void             dragExit (DropTargetEvent e) {
        unhighlight ();
    }
    
    @Override
    public void             dragEnter (DropTargetDragEvent e) {
        if (getComponentIfLegal (e.getTransferable ()) != null) {
            JTabbedPane     tabbedPane = getTabbedPane ();
            int             tabIndex = tabbedPane.indexOfTabComponent (target);
            tabbedPane.setSelectedIndex (tabIndex);
            
            highlight ();
        }
    }
    
    @Override
    protected void          executeDrop (Component dragged, Point dropLocation) {
        unhighlight ();
        
        JTabbedPane     tabbedPane = getTabbedPane ();
        int             tabIndex = tabbedPane.indexOfTabComponent (target);
        GridPanel       gp = (GridPanel) tabbedPane.getComponentAt (tabIndex);

        GridPanel.removeChild (dragged);
        gp.addComponent (dragged, SwingConstants.TOP);
        tabbedPane.setSelectedComponent (gp);
    }
}
