package deltix.util.swing;

import java.text.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static deltix.util.swing.SwingUtil.RB;

/**
 *  A combination of text field and a button. The button opens
 *  a file selector. User can see and type in the path into
 *  the text field without opening a file selector. This is suitable
 *  for forms where a form field has a file as its value.
 */
public class FileField extends JPanel {
    public static final int ACTION_FILE_DIALOG = 1;
    public static final int ACTION_TEXT_ENTER = 2;
    
    public static final int VALID_ANY_PATH = 0;
    public static final int VALID_EXISTING_DIR = 1;
    public static final int VALID_EXISTING_FILE = 2;
    public static final int VALID_EXISTING_OR_NEW_DIR = 3;
    public static final int VALID_EXISTING_OR_NEW_DIR_CC = 4;
    
    public static final int VALID_ALLOW_NULL = 0x100;
    
    private JFileChooser        mFileChooser = new JFileChooser ();
    private NonEmptyTextField   mPathField = new NonEmptyTextField (true);
    private JButton             mDialogButton = new JButton ("...");
    private int                 mValidationMode = VALID_ANY_PATH;
    private ArrayList <ActionListener> mActionListeners = new ArrayList <ActionListener> ();
        
    public FileField () {
        super (new BorderLayout ());
        
        add (mPathField, BorderLayout.CENTER);
        add (mDialogButton, BorderLayout.EAST);
        
        mDialogButton.addActionListener (
            new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    dialog ();                    
                }
            }
        );
        
        mPathField.addActionListener (
            new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    fireActionEvent (
                        new ActionEvent (
                            FileField.this, 
                            ACTION_TEXT_ENTER, 
                            mPathField.getText ()
                        )
                    );                  
                }
            }
        );
        
        mFileChooser.setApproveButtonText (RB.getString ("selectFile"));
        mDialogButton.setMargin(new Insets (0, 2, 0, 2));
    }
    
    public void         addActionListener (ActionListener lnr) {
        mActionListeners.add (lnr);
    }
    
    public void         removeActionListener (ActionListener lnr) {
        mActionListeners.remove (lnr);
    }
    
    protected void      fireActionEvent (ActionEvent e) {
        for (ActionListener lnr : mActionListeners)
            lnr.actionPerformed (e);
    }
    
    /**
     *  If user types in a directory path, and the directory does not exist,
     *  create this directory. Failure to create this directory causes 
     *  the control to fail validation.
     */
    public void         setValidationMode (int mode) {
        mValidationMode = mode;
        
        switch (mValidationMode & 0xFF) {
            case VALID_ANY_PATH:
                mFileChooser.setFileSelectionMode (JFileChooser.FILES_AND_DIRECTORIES);
                break;
                
            case VALID_EXISTING_DIR:
            case VALID_EXISTING_OR_NEW_DIR:   
            case VALID_EXISTING_OR_NEW_DIR_CC:
                mFileChooser.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
                break;
                
            case VALID_EXISTING_FILE:
                mFileChooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
                break;                
        }
    }
    
    public void         setEnabled (boolean flag) {
        super.setEnabled (flag);
        mPathField.setEnabled (flag);
        mDialogButton.setEnabled (flag);
    }
    
    public String       getPath () throws ParsingException {
        File    f = getFile ();
        
        if (f == null)
            return (null);
        
        return (f.getPath ());
    }
    
    private void        complain (ParsingException px) 
        throws ParsingException 
    {
        mPathField.complain (px);
        throw (px);
    }
    
    public File         getFile () throws ParsingException {
        if ((mValidationMode & VALID_ALLOW_NULL) != 0 &&
            mPathField.getText ().trim ().length () == 0)
            return (null);
        
        String      text = mPathField.getValue ();        
        File        f = new File (text);
        String      path = f.getPath ();
        
        if (!f.isAbsolute ()) 
            complain (new ParsingException ("relativePath", path));
               
        switch (mValidationMode & 0xFF) {
            case VALID_EXISTING_DIR:
                if (!f.isDirectory ()) 
                    complain (new ParsingException ("notDir", path));
                break;
            
            case VALID_EXISTING_OR_NEW_DIR_CC:
                if (!f.isDirectory ()) {
                    int	status =
                        JOptionPane.showOptionDialog (
                            this,
                            MessageFormat.format (
                                RB.getString ("confirmDirCreation"), 
                                new Object [] { path }
                            ),
                            RB.getString ("confirmation"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            null
                        );

                    if (status != JOptionPane.YES_OPTION) {                        
                        mPathField.highlight ();
                        throw new ParsingException ();
                    }

                    if (!f.mkdirs ()) 
                        complain (new ParsingException ("notDir", path));
                }
                break;
                
            case VALID_EXISTING_OR_NEW_DIR:                     
                break;
                
            case VALID_EXISTING_FILE:
                if (!f.isFile ()) 
                    complain (new ParsingException ("notFile", path));
                break;                
        }
        
        return (f);
    }
    
    public void         setPath (String value) {
        mPathField.setText (value == null ? "" : value);
    }
    
    public void         setFile (File value) {
        setPath (value == null ? "" : value.getPath ());
    }
    
    /**
     *  Exposes the embedded JFileChooser so caller can configure it. For
     *  example: <code>field.fileChooser ().setFileFilter (...)</code>.
     */
    public JFileChooser     fileChooser () {
        return (mFileChooser);
    }
    
	private void			dialog () {
        String              text = mPathField.getText ();
        
        if (text.length () != 0)
            mFileChooser.setSelectedFile (new File (text));
        
        int                 ret = mFileChooser.showOpenDialog (this);
        
        if (ret == JFileChooser.APPROVE_OPTION) {
            String      path = mFileChooser.getSelectedFile ().getPath ();
            
            setPath (path); 
            fireActionEvent (new ActionEvent (this, ACTION_FILE_DIALOG, path));
        }
    }
    
    public void             complain (String errorMsg) {
        mPathField.complain (errorMsg);
    }
}

