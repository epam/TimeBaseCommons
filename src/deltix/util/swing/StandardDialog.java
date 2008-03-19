package deltix.util.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  Dialog with a number of named buttons at the bottom. Non-modal by default,
 *  don't forget to call <code>setModal (true)</code> if desired.
 */
public class StandardDialog extends JDialog {
    private int                             mStatus;
    
    public final int                        getStatus () {
        return (mStatus);
    }
    
    /**
     *  Child classes override for validation
     */
    public boolean                          acceptStdAction (int status) {
        return (true);
    }
    
    public StandardDialog (Component parent, final String [] buttonNames) {
        super (JOptionPane.getFrameForComponent (parent));

        Container       cp = getContentPane ();
        cp.setLayout (new BorderLayout ());
        
        JPanel          btns = new JPanel ();
        cp.add (btns, BorderLayout.SOUTH);
        
        for (int ii = 0; ii < buttonNames.length; ii++) {
            final int       status = ii;
            
            JButton         btn = 
                new JButton (
                    new AbstractAction (buttonNames [ii]) {
                        public void actionPerformed (ActionEvent e) {
                            if (acceptStdAction (status)) {
                                mStatus = status;
                                dispose ();
                            }
                        }
                    }
                );
                
            btns.add (btn);
            
            if (ii == 0)
                getRootPane ().setDefaultButton (btn);
        }
    }
    
    public final int        doModal () {
        assert !isVisible ();
        
        boolean     saveIsModal = isModal ();
        setModal (true);
        setVisible (true);
        setModal (saveIsModal);
        
        return (getStatus ());
    }
}
