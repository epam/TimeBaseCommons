/*
 * NodeAdapter.java
 *
 * Created on July 13, 2004, 10:34 AM
 */

package deltix.util.swing.treeedit;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import deltix.util.collections.*;

/**
 *  Internal class that adapts the {@link TreeEditorNode} class to JTree.
 */
final class NodeAdapter implements TreeNode {
    private TreeEditorPanel     mPanel;
    private NodeAdapter         mParent;
    private TreeEditorNode      mUserNode;
    private boolean             mChildrenUpdated = false;
    private NodeAdapter []      mChildNodeAdapters;
    
    public NodeAdapter (TreeEditorPanel panel, TreeEditorNode userNode) {
        mParent = null;
        mPanel = panel;
        mUserNode = userNode;
        mUserNode.setCallback (this);
    }
    
    public NodeAdapter (NodeAdapter parent, TreeEditorNode userNode) {
        mParent = parent;
        mPanel = parent.mPanel;
        mUserNode = userNode;
        mUserNode.setCallback (this);
    }
    
    void                    updateChildren () {
        if (mChildrenUpdated)
            return;
        
        int             numChildren = mUserNode.getNumChildren ();
        
        if (numChildren == TreeEditorNode.LEAF_NODE) {
            mChildNodeAdapters = null;
            return;
        }
        
        mChildNodeAdapters = new NodeAdapter [numChildren];
        
        for (int ii = 0; ii < numChildren; ii++) 
            mChildNodeAdapters [ii] = 
                new NodeAdapter (this, mUserNode.getChild (ii)); 
        
        mChildrenUpdated = true;       
    }
    
    public Enumeration      children () {
        updateChildren ();
        return (new ArrayEnumeration (mChildNodeAdapters));
    }
    
    public boolean          getAllowsChildren () {
        updateChildren ();
        return (mChildNodeAdapters != null);
    }
    
    public TreeNode         getChildAt (int idx) {
       updateChildren ();
       return (mChildNodeAdapters [idx]);
    }
    
    public int              getChildCount () {
        updateChildren ();
        return (mChildNodeAdapters == null ? 0 : mChildNodeAdapters.length);
    }
    
    public int              getIndex (TreeNode treeNode) {
        updateChildren ();
        if (mChildNodeAdapters == null)
            return (-1);
        
        for (int ii = 0; ii < mChildNodeAdapters.length; ii++) 
            if (mChildNodeAdapters [ii] == treeNode)
                return (ii);
        
        return (-1);
    }
    
    public TreeNode         getParent () {
        return (mParent);
    }
    
    public boolean          isLeaf () {
        return (mUserNode.getNumChildren () == TreeEditorNode.LEAF_NODE);
    }    
    
    TreeEditorNode          getUserNode () {
        return (mUserNode);
    }
    
    void                    reload () {
        mChildrenUpdated = false;
        updateChildren ();
        mPanel.getTreeModel ().reload (this);
    }
    
    TreePath                getPath () {
        int         depth = 1;        
        TreeNode    node = this; 
        
        for (;;) {
            TreeNode    next = node.getParent ();
            
            if (next == null)
                break;
            
            depth++;
            node = next;
        }
        
        Object []   path = new Object [depth];
        
        node = this; 
        
        for (;;) {
            depth--;
            path [depth] = node;
            
            if (depth == 0)
                break;
            
            node = node.getParent ();
        }
        
        return (new TreePath (path));
    }
    
    final void           select () {
        /**    
         *  Reset selection momentarily in order to 
         *      force the reloading of the node's form.
         */  
        mPanel.getTree ().setSelectionPath (null);
        mPanel.getTree ().setSelectionPath (getPath ());            
    }
    
    final void           selectAndStartEditing () {
        select ();
        mPanel.edit ();
    }
    
    final void          updateFormHeaderFromEditedNode () {
        mPanel.updateFormHeaderFromEditedNode ();
    }
    
    final void           editInCreationMode (TreeEditorNode node) {
        mPanel.editInCreationMode (node);
    }
   
    final void           setSaveEnabled (boolean flag) {
        mPanel.setSaveEnabled (flag);
    }
    
    final void           setCancelEnabled (boolean flag) {
        mPanel.setCancelEnabled (flag);
    }    
    
    final void           show () {
        mPanel.getTree ().expandPath (getPath ());
    }
}
