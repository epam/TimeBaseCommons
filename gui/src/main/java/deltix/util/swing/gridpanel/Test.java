package deltix.util.swing.gridpanel;

import deltix.util.swing.SwingAbstractApp;
import deltix.util.swing.SwingUtil;
import deltix.util.swing.TabFocusSelector;
import java.awt.*;
import javax.swing.*;

/**
 *
 */
public class Test {
    public static void main (String [] args) throws Exception {
        SwingUtil.setWindowsLookAndFeel ();
        
        SwingAbstractApp     app = new SwingAbstractApp ();
        
        final JTabbedPane     tabs = new JTabbedPane ();
        
        for (int jj = 0; jj < 3; jj++) {
            GridPanel       gp = new GridPanel ();

            for (int ii = 0; ii < 4; ii++) {
                JTextArea    ta = new JTextArea ();

                ta.setText ("Control #" + ii);
                ta.setPreferredSize(new Dimension (100, 100));
                gp.addComponent (ta, SwingConstants.BOTTOM);
            }
            
            tabs.addTab ("" + jj, gp);
            
            JTextField      tab = new JTextField ("Tab #" + jj);
            tab.setOpaque (false);
            tab.setBorder (null);
            tab.addFocusListener (TabFocusSelector.INSTANCE);
            
            tabs.setTabComponentAt (jj, tab); 
            
            TabDTL.install (tab);           
        }
                
        app.setContentPane (tabs);
        
        app.setSize (1024, 800);
        app.setVisible (true);
    }
}
