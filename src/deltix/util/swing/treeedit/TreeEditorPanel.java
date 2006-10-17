package deltix.util.swing.treeedit;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

import deltix.util.swing.*;

public class TreeEditorPanel extends JSplitPane {
    private final Action EDIT_ACTION =
        new SimpleAction (this, "edit");
    
    private final Action SAVE_ACTION =
        new SimpleAction (this, "save");
    
    private final Action CANCEL_ACTION =
        new SimpleAction (this, "cancel");
    
    public static final Border      HEADER_BORDER =
        BorderFactory.createEmptyBorder (3, 24, 3, 24);
    
    private static final Color  mDisabledColor = new Color (0xF8F8F8);
    private static final Color  mEnabledColor = new Color (0xFFFFFF);
    
    private DefaultTreeModel    mTreeModel;
    private JTree               mTree;
    private JLabel              mStockHeader = new JLabel (" ");
    private JPanel              mFormPanel = new JPanel (new BorderLayout ());
    private JPanel              mBottom = new JPanel (new FlowLayout ());
    private VerticalForm        mForm;
    private JComponent          mFormHeader = null;
    private TreeEditorNode      mSelectedNode;
    private TreeEditorNode      mEditedNode;
    private boolean             mCreationMode;
    private JButton             mEditBtn = new JButton (EDIT_ACTION);
    private JButton             mSaveBtn = new JButton (SAVE_ACTION);
    private JButton             mCancelBtn = new JButton (CANCEL_ACTION);
    private Collection <NodeChangeListener>     mNodeChangeListeners = 
        new HashSet <NodeChangeListener> ();
    
    public TreeEditorPanel (TreeEditorNode root) {
        super (HORIZONTAL_SPLIT);

        mTreeModel = new DefaultTreeModel (new NodeAdapter (this, root));
        mTree = new JTree (mTreeModel);
        mTree.setShowsRootHandles (true);
        mTree.setCellRenderer (new NodeRenderer ());
        mTree.addMouseListener (
            new MouseAdapter () {
                public void mousePressed (MouseEvent e) {
                    if (e.isPopupTrigger ()) {
                        popup (e.getX (), e.getY ());
                    }
                }
                
                public void mouseReleased (MouseEvent e) {
                    if (e.isPopupTrigger ()) {
                        popup (e.getX (), e.getY ());
                    }
                }
            }
        );
        
        mTree.addTreeSelectionListener (
            new TreeSelectionListener () {
                public void valueChanged (TreeSelectionEvent e) {
                    selectionChanged (e.getNewLeadSelectionPath ());
                }
            }
        );
        
        setLeftComponent (new JScrollPane (mTree));
        
        mForm = new VerticalForm ();
        JScrollPane     scroller = new JScrollPane (mForm);
        
        mFormPanel.add (scroller, BorderLayout.CENTER);
        mFormPanel.add (mBottom, BorderLayout.SOUTH);
        
        setRightComponent (mFormPanel);
        setEditing (null);
        EDIT_ACTION.setEnabled (false);
    }
    
    DefaultTreeModel    getTreeModel () {
        return (mTreeModel);
    }
    
    JTree               getTree () {
        return (mTree);
    }
    
    /**
     *  Controls the visibility of buttons such as "Edit", "Save" and
     *  "Cancel". Applications that use the tree editor as a slave
     *  might set this to false.
     */
    public void         setBottomButtonsVisible (boolean flag) {
        mBottom.setVisible (flag);
    }
    
    public void         save () {
        if (mEditedNode.acceptChanges ()) {
            TreeEditorNode  node = mEditedNode;
            
            setEditing (null);
            
            if (mCreationMode) {
                node.created ();
                node.select ();
            }
        
            setFormFromNode (mSelectedNode);
            
            fireNodeChanged (node);                        
        }
    }
    
    public void         cancel () {
        if (mCreationMode)
            mEditedNode.creationCanceled ();
       
        mForm.removeAll ();
        mEditedNode.configureForm (mForm);
        mForm.revalidate ();
        setEditing (null); 
        
        if (mCreationMode)
            setFormFromNode (mSelectedNode);
    }
    
    public void         edit () {
        mCreationMode = false;
        setEditing (mSelectedNode);
        mSelectedNode.beginEdit (mForm);
    }
    
    public void         editInCreationMode (TreeEditorNode node) {
        mCreationMode = true;
        setFormFromNode (node);
        setEditing (node);
        node.beginEdit (mForm);
    }
    
    private void        setEditing (TreeEditorNode node) {
        mEditedNode = node;
        mTree.setBackground (node == null ? mEnabledColor : mDisabledColor);
        mTree.setEnabled (node == null);
        mForm.setEnabled (node != null);
        
        mBottom.removeAll ();
        
        if (node == null) 
            mBottom.add (mEditBtn);
        else {
            /**
             *  Enable save/cancel by default, node can reset.
             */
            SAVE_ACTION.setEnabled (true);
            CANCEL_ACTION.setEnabled (true);
            
            mBottom.add (mSaveBtn);
            mBottom.add (mCancelBtn);        
        }
                            
        mBottom.revalidate ();
        mBottom.repaint ();
    }
    
    public void         updateFormHeaderFromEditedNode () {
        if (mEditedNode != null)
            setFormHeader (mEditedNode);
    }
    
    private void        setFormHeader (TreeEditorNode userNode) {
        JComponent          c = userNode.render (true, true, mStockHeader);
        
        if (mFormHeader == c)
            return;
        
        if (mFormHeader != null)
            mFormPanel.remove (mFormHeader);
        
        mFormHeader = c;
        
        if (mFormHeader != null) {
            mFormHeader.setBorder (HEADER_BORDER);        
            mFormPanel.add (mFormHeader, BorderLayout.NORTH);
        }
    }
    
    private void        setFormFromNode (TreeEditorNode userNode) {
        mForm.removeAll ();
        
        if (userNode != null) {
            setFormHeader (userNode);
            userNode.configureForm (mForm);
        }
        
        mFormPanel.revalidate ();
        mFormPanel.repaint ();
    }
    
    private void        selectionChanged (TreePath newPath) {
        if (newPath == null) {
            mSelectedNode = null;
            EDIT_ACTION.setEnabled (false);
        }
        else {        
            mSelectedNode = ((NodeAdapter) newPath.getLastPathComponent ()).getUserNode ();
            EDIT_ACTION.setEnabled (mSelectedNode.isEditable ());
        }
        
        setFormFromNode (mSelectedNode);
    }
    
    private void        popup (int x, int y) {
        if (!mTree.isEnabled ())
            return;
            
        TreePath    selPath = mTree.getPathForLocation (x, y); 
        
        if (selPath == null)
            return;
        
        NodeAdapter                 node = 
            (NodeAdapter) selPath.getLastPathComponent ();
        
        JPopupMenu                  menu = node.getUserNode ().getMenu ();
        
        if (menu != null)
            menu.show (mTree, x, y);        
    }
    
    public void         expandEntireTree () {
        SwingUtil.expandEntireTree (mTree);
    }
    
    final void           setSaveEnabled (boolean flag) {
        SAVE_ACTION.setEnabled (flag);
    }
    
    final void           setCancelEnabled (boolean flag) {
        CANCEL_ACTION.setEnabled (flag);
    }        
    
    public void         addNodeChangeListener (NodeChangeListener listener) {
        mNodeChangeListeners.add (listener);
    }
    
    public void         removeNodeChangeListener (NodeChangeListener listener) {
        mNodeChangeListeners.remove (listener);
    }
    
    void                fireNodeChanged (TreeEditorNode node) {
        for (NodeChangeListener listener : mNodeChangeListeners) 
            listener.nodeChanged (node);
    }
}
