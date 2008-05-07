package deltix.util.swing.treeedit;

import deltix.util.swing.DefaultDragGestureRecognizer;
import deltix.util.swing.SimpleAction;
import deltix.util.swing.SwingUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;

public class TreeEditorPanel extends JSplitPane {
    protected final Action EDIT_ACTION =
        new SimpleAction (this, "edit");
    
    protected final Action SAVE_ACTION =
        new SimpleAction (this, "save");
    
    protected final Action CANCEL_ACTION =
        new SimpleAction (this, "cancel");
    
    public static final Border      HEADER_BORDER =
        BorderFactory.createCompoundBorder (
            BorderFactory.createEtchedBorder (),
            BorderFactory.createEmptyBorder (3, 24, 3, 24)
        );
    
    protected static final Color  mDisabledColor = new Color (0xF8F8F8);
    protected static final Color  mEnabledColor = new Color (0xFFFFFF);
    
    protected DefaultTreeModel    mTreeModel;
    protected JTree               mTree;
    protected JLabel              mStockHeader = new JLabel (" ");
    protected JPanel              mFormPanel = new JPanel (new BorderLayout ());
    protected JPanel              mBottom = new JPanel (new FlowLayout ());
    protected JComponent          mCurrentForm = null;
    protected JComponent          mFormHeader = null;
    protected TreeEditorNode      mSelectedNode;
    protected TreeEditorNode      mEditedNode;
    protected boolean             mCreationMode;
    protected JButton             mEditBtn = new JButton (EDIT_ACTION);
    protected JButton             mSaveBtn = new JButton (SAVE_ACTION);
    protected JButton             mCancelBtn = new JButton (CANCEL_ACTION);
    protected Collection <NodeChangeListener>     mNodeChangeListeners =
        new HashSet <NodeChangeListener> ();
    protected Collection <EnablingChangeListener> mEnablingChangeListeners =
        new HashSet <EnablingChangeListener> ();
    
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
        
        mFormPanel.add (mBottom, BorderLayout.SOUTH);
        
        setRightComponent (mFormPanel);
        setEditing (null);
        setEditEnabled (false);

        DefaultDragGestureRecognizer dgRecognizer = new DefaultDragGestureRecognizer();
        mTree.addMouseListener(dgRecognizer);
        mTree.addMouseMotionListener(dgRecognizer);
        mTree.setDragEnabled(true);
        mTree.setTransferHandler(new TreeEditorTransferHandler());
        //Enable tool tips.
        ToolTipManager.sharedInstance().registerComponent(mTree);
    }
    
    private void            setEditEnabled (boolean flag) {
        mEditBtn.setVisible (flag);
        EDIT_ACTION.setEnabled (flag);
    }
    
    protected void        setFormComponent (JComponent form) {
        if (mCurrentForm != null)
            mFormPanel.remove (mCurrentForm);
        
        mCurrentForm = form;
        
        if (mCurrentForm != null)
            mFormPanel.add (form, BorderLayout.CENTER);
    }
    
    DefaultTreeModel    getTreeModel () {
        return (mTreeModel);
    }
    
    public JTree               getTree () {
        return (mTree);
    }

    protected JComponent getCurrentForm() {
        return mCurrentForm;
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
            else
                node.updated ();
            
            setFormFromNode (mSelectedNode);
            
            fireNodeChanged (node);                        
        }
    }
    
    public void         cancel () {
        if (mCreationMode)
            mEditedNode.creationCanceled ();
      
        setEditing (null);        
        setFormFromNode (mSelectedNode);
    }
    
    public void         edit () {
        mCreationMode = false;
        setEditing (mSelectedNode);
        mSelectedNode.beginEdit ();
    }
    
    public void         editInCreationMode (TreeEditorNode node) {
        mCreationMode = true;
        setFormFromNode (node);
        setEditing (node);
        node.beginEdit ();
    }
    
    protected void        setEditing (TreeEditorNode node) {
        mEditedNode = node;
        mTree.setBackground (node == null ? mEnabledColor : mDisabledColor);
        mTree.setEnabled (node == null);
        
        if (mCurrentForm != null)
            SwingUtil.setDeepEnabled (mCurrentForm, node != null);
        
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

        fireEditingChanged(node == null);
    }
    
    public void         updateFormHeaderFromEditedNode () {
        if (mEditedNode != null)
            setFormHeader (mEditedNode);
    }
    
    protected void        setFormHeader (TreeEditorNode userNode) {
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
    
    protected void        setFormFromNode (TreeEditorNode userNode) {
        if (userNode == null) 
            setFormComponent (null);
        else {
            setFormHeader (userNode);
            setFormComponent (userNode.getUI ());
        }
        
        mFormPanel.revalidate ();
        mFormPanel.repaint ();
    }
    
    protected void        selectionChanged (TreePath newPath) {
        if (newPath == null) {
            mSelectedNode = null;
            setEditEnabled (false);
        }
        else {        
            mSelectedNode = ((NodeAdapter) newPath.getLastPathComponent ()).getUserNode ();
            setEditEnabled (mSelectedNode.isEditable ());
        }
        
        setFormFromNode (mSelectedNode);

        if (mCurrentForm != null)
            SwingUtil.setDeepEnabled (mCurrentForm, false);
    }
    
    protected void        popup (int x, int y) {
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

    public void         collapseEntireTree () {
        SwingUtil.collapseEntireTree (mTree);
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

     public void         addEnablingChangeListener (EnablingChangeListener listener) {
        mEnablingChangeListeners.add (listener);
    }

    public void         removeEnablingChangeListener (EnablingChangeListener listener) {
        mEnablingChangeListeners.remove (listener);
    }


     protected void      fireEditingChanged (boolean edit) {
        for (EnablingChangeListener listener : mEnablingChangeListeners)
            listener.enablingChanged(edit);
    }

    //////////////////HELPER CLASSES///////////////////////////

    protected class TreeEditorTransferHandler extends TransferHandler {

        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return false;
        }

        protected boolean isTranferable(Object obj) {
            return obj instanceof NodeAdapter;
        }

        protected Transferable createTransferable(JComponent comp) {
            if (comp instanceof JTree) {
                JTree tree = (JTree) comp;
                TreePath[] treePaths = tree.getSelectionPaths();
                if (treePaths == null || treePaths.length != 1)
                    return null;

                Object obj = treePaths[0].getLastPathComponent();
                if (isTranferable(obj)) {
                    TreeEditorNode node  = ((NodeAdapter)obj).getUserNode();
                    return node.getTransferable();
                }
            }
            return null;
        }

        public int getSourceActions(JComponent comp) {
            return TransferHandler.COPY_OR_MOVE;
        }


        public boolean importData(JComponent comp, Transferable t) {
            return false;
        }
    }

    public interface EnablingChangeListener {
        public void enablingChanged(boolean enable);
    }
}
