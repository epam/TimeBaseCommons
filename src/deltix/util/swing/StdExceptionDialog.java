package deltix.util.swing;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.JTextArea;

/**
 *
 */
public class StdExceptionDialog extends StandardDialog {
    private Throwable       mException;
    private JLabel          mMessage;
    private JTextArea       mTrace;
    private JScrollPane     mTraceScroller;
    
    public StdExceptionDialog (Component parent, Throwable x) {
        super (parent, "Ok", "Details");
        setModal (true);
        
        mException = x;
        
        final String    xClassName = x.getClass ().getName ();
        
        setTitle (xClassName);
        
        String          msg = x.getLocalizedMessage ();

		if (msg == null || msg.length () == 0)
			msg = ("Exception: " + xClassName);

        if (msg.contains ("\n")) {
            msg = msg.replaceAll ("\n", "<br>");
            msg = "<html>" + msg + "</html>";
        }
		
		mMessage = new JLabel (msg);
        mMessage.setBorder (
            BorderFactory.createEmptyBorder (4, 4, 4, 4)
        );

        StringWriter    swr = new StringWriter ();
        PrintWriter     trace = new PrintWriter (swr);
        
        x.printStackTrace (trace);
        trace.close ();
        
        mTrace = new JTextArea (swr.toString ());
        mTrace.setEditable (false);
        
        mTraceScroller = new JScrollPane (mTrace);
        mTraceScroller.setVisible (false);
        
        Container       cp = getContentPane ();
        
        cp.add (mMessage, BorderLayout.NORTH);
        cp.add (mTraceScroller, BorderLayout.CENTER);
        
        repack ();
        
        centerOnParent ();
    }

    private void            repack () {
        pack ();
        
        Dimension       size = getSize ();
        
        size.width = 600;
        
        if (size.height > 600)
            size.height = 600;
        
        setSize (size);
    }
    
    @Override
    public boolean          acceptStdAction (int status) {
        if (status == 0)
            return (true);
        
        mTraceScroller.setVisible (!mTraceScroller.isVisible ());
        repack ();
        return (false);
    }
    
    public static void      main (String [] args) throws Exception {
        new StdExceptionDialog (null, new Throwable ("Blabidi blah")).setVisible (true);
    }
}
