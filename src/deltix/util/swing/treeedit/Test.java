package deltix.util.swing.treeedit;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import deltix.util.swing.*;

/**
 *
 */
public class Test extends TreeEditorNode {
    private String      mLabel;
    private int         mLevel;
    private ArrayList <Test>   mChildren = new ArrayList <Test> ();
    
    public Test (int level) {
        mLevel = level;
        mLabel = mLevel + "-" + hashCode ();
        
        if (mLevel < 4)
            mChildren.add (new Test (mLevel + 1));
    }
        
    public int                 getNumChildren () {
        return (mChildren == null ? LEAF_NODE : mChildren.size ());
    }
    
    public TreeEditorNode       getChild (int idx) {
        return ((TreeEditorNode) mChildren.get (idx));
    }
    
    public String               getLabelText () {
        return (mLabel);
    }
    
    /**
     *  Return the color for the label to display, or null.
     */
    public Color                getColor () {
        int     k = (mLevel * 16) % 256;
        
        return (new Color (k, 255 - k, 255 - k));
    }
        
    public JPopupMenu           getMenu () {
        JPopupMenu                  menu = new JPopupMenu ();
        
        menu.add (
            new JMenuItem (
                new AbstractAction ("Add Child") { 
                    public void     actionPerformed (ActionEvent e) {
                        addChild ();
                    }
                }
            )
        );
        
        return (menu);
    }
    
    private IntegerTextField    mLevelField;
    private JTextField          mLabelField;
    
    public void                 configureForm (VerticalForm form) {  
        mLevelField = new IntegerTextField (mLevel, 5);
        mLabelField = new JTextField (getLabelText (), 16);
        
        form.addField ("Level", mLevelField);
        form.addField ("Label", mLabelField);
    }
    
    public boolean              acceptChanges () {
        try {
            String      lbl = mLabelField.getText ();
            int         lvl = mLevelField.getIntegerValue ();
            
            if (!super.acceptChanges ())
                return (false);
            
            mLabel = lbl;
            mLevel = lvl;
            reload ();
            return (true);
        } catch (ParsingException x) {
            return (false);
        }        
    }
 
    private void                addChild () {
        mChildren.add (new Test (mLevel + 1));
        reload ();
    }
    
    public static void main (String [] args) throws Exception {
        JFrame  f = new JFrame ("test");
        TreeEditorPanel ted = new TreeEditorPanel (new Test (0));
        f.getContentPane ().add (ted);
        f.setDefaultCloseOperation (f.EXIT_ON_CLOSE);
        f.setSize (800, 600);
        f.setVisible (true);
        
        ted.setDividerLocation (0.5);
    }    
}
