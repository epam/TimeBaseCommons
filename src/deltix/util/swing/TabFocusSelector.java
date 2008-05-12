package deltix.util.swing;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTabbedPane;

/**
 *
 */
public class TabFocusSelector extends FocusAdapter {
    public static final TabFocusSelector    INSTANCE = new TabFocusSelector ();
    
    private TabFocusSelector () { }
    
    @Override
    public void     focusGained (FocusEvent e) {
        Component       tab = e.getComponent ();
        JTabbedPane     pane = SwingUtil.findParent (tab, JTabbedPane.class);
        pane.setSelectedIndex (pane.indexOfTabComponent (tab));
    }
}
