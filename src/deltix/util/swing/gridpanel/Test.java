package deltix.util.swing.gridpanel;

import deltix.util.swing.AbstractApp;
import deltix.util.swing.SwingUtil;
import javax.swing.*;

/**
 *
 */
public class Test {
    public static void main (String [] args) throws Exception {
        SwingUtil.setWindowsLookAndFeel ();
        
        AbstractApp     app = new AbstractApp ();
        
        GridPanel       gp = new GridPanel ();
        
        for (int ii = 0; ii < 4; ii++) {
            JTextArea    ta = new JTextArea ();
            
            ta.setText("Label #" + ii);
            
            gp.addComponent (ta, SwingConstants.BOTTOM);
        }
        
        app.setContentPane (gp);
        
        app.setSize (1024, 800);
        app.setVisible (true);
    }
}
