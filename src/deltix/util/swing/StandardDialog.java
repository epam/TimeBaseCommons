package deltix.util.swing;

import deltix.util.lang.Util;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *  Dialog with a number of named buttons at the bottom. Non-modal by default,
 *  don't forget to call <code>setModal (true)</code> if desired.
 */
public class StandardDialog extends JDialog {
    private int                             mStatus;
    private ArrayList <Action>              mStdActions =
        new ArrayList <Action>  ();
    
    public final int                        getStatus () {
        return (mStatus);
    }
    
    protected final void                    setStatus (int status) {
        mStatus = status;
    }

    /**
     *  Child classes override for validation
     */
    public boolean                          acceptStdAction (int status) 
        throws Exception
    {
        return (true);
    }
    
    public void                             centerOnParent () {
        setLocationRelativeTo (getOwner ());
    }
    
    public StandardDialog (Component parent, final String ... buttonNames) {
        super (JOptionPane.getFrameForComponent (parent));

        Container       cp = getContentPane ();
        cp.setLayout (new BorderLayout ());
        
        JPanel          btns = new JPanel ();
        cp.add (btns, BorderLayout.SOUTH);
        
        for (int ii = 0; ii < buttonNames.length; ii++) {
            final int       status = ii;
            
            Action          action =
                new AbstractAction (buttonNames [ii]) {
                    public void actionPerformed (ActionEvent e) {
                        try {
                            if (acceptStdAction (status)) {
                                mStatus = status;
                                dispose ();
                            }
                        } catch (Throwable x) {
                            handle (x);
                        }
                    }
                };
                    
            JButton         btn = new JButton (action);
                
            mStdActions.add (action);
            
            btns.add (btn);
            
            if (ii == 0)
                getRootPane ().setDefaultButton (btn);
        }
    }
    
    protected void          setContentPaneCenter (Component c) {
        getContentPane ().add (c, BorderLayout.CENTER);
    }
    
    protected void          packWithMinimumSize (int minWidth, int minHeight) {
        pack ();
        
        Dimension   d = getPreferredSize ();
        
        if (d.width < minWidth)
            d.width = minWidth;
        
        if (d.height < minHeight)
            d.height = minHeight;
                
        setSize (d);
    }
    
    public final int        doModal () {
        assert !isVisible ();
        
        boolean     saveIsModal = isModal ();
        setModal (true);
        setVisible (true);
        setModal (saveIsModal);
        
        return (getStatus ());
    }
    
    public void		        handle (Throwable x) {
        handle (x, Level.SEVERE);
    }
    
    public void		        handle (
        Throwable                   x,
        Level                       logLevel        
    ) 
    {
        handle (x, Util.LOGGER, logLevel);
    }
    
    public void		        handle (
        Throwable                   x,
        Logger                      logger,
        Level                       logLevel
    ) 
    {
        SwingUtil.staticHandle (this, x, logger, logLevel);
    }
        
    protected Action        getAction (int idx) {
        return (mStdActions.get (idx));
    }
}
