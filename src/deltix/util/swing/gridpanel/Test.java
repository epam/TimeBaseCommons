package deltix.util.swing.gridpanel;

import deltix.util.swing.AbstractApp;
import deltix.util.swing.SwingUtil;
import java.awt.*;
import javax.swing.*;

/**
 *
 */
public class Test {
    public static void main (String [] args) throws Exception {
        SwingUtil.setWindowsLookAndFeel ();
        
        AbstractApp     app = new AbstractApp ();
        
        final JTabbedPane     tabs = new JTabbedPane ();
        
        for (int jj = 0; jj < 3; jj++) {
            GridPanel       gp = new GridPanel ();

            for (int ii = 0; ii < 4; ii++) {
                JTextArea    ta = new JTextArea ();

                ta.setText ("Control #" + ii);

                gp.addComponent (ta, SwingConstants.BOTTOM);
            }
            
            tabs.addTab ("" + jj, gp);
            
            JLabel      tab = new JLabel ("Tab #" + jj);
            tab.setForeground (Color.green);
            tabs.setTabComponentAt (jj, tab); 
            
            TabDTL.install (tab);           
        }
                
        app.setContentPane (tabs);
        
        app.setSize (1024, 800);
        app.setVisible (true);
    }
}
