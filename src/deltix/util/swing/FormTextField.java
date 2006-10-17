package deltix.util.swing;

import javax.swing.*;
import java.util.*;

import deltix.util.*;

/** 
 *  Additional functionality to JTextField.
 */
public class FormTextField extends JTextField {
    public FormTextField (String text, int columns) {
        super (text, columns);
    }
    
    public FormTextField (String text) {
        super (text);
    }
    
    public FormTextField (int columns) {
        super (columns);
    }
    
    public FormTextField () {
        super ();
    }
    
    public void         highlight () {
        requestFocus ();
        select (0, getText ().length ());        
    }
    
    public void         complain (String errorMessage) {
        JOptionPane.showMessageDialog (
            this, 
            errorMessage, 
            SwingUtil.ERROR_TITLE, 
            JOptionPane.ERROR_MESSAGE
        );

        highlight ();
    }
    
    public void         complain (Throwable t) {
        complain (t.getLocalizedMessage ());
    }
    
}
