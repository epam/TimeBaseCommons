package deltix.util.swing.treeedit;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import deltix.util.swing.*;

/**
 *  Interface implemented by the user of the Tree Editor component. 
 */
public abstract class TreeEditorNode {
    public static final int     LEAF_NODE = -1;
    
    public interface Filter {
        public boolean      accept (TreeEditorNode node);
    }
    
    private NodeAdapter         mCallback;
    
    void                        setCallback (NodeAdapter a) {
        mCallback = a;
    }
    
    /**
     *  Return the label to display, or null.
     */
    public String               getLabelText () {
        return (null);
    }
    
    /**
     *  Return the font for the label to display.
     */
    public Font                 getFont (Font defFont) {
        return (defFont);
    }
    
    /**
     *  Return the color for the label to display, or null.
     */
    public Color                getColor () {
        return (null);
    }
    
    /**
     *  Return an icon to display, or null.
     */
    public Icon                 getIcon () {
        return (null);
    }
    
    /**
     *  User can override this method instead of {@link #getColor}, 
     *  {@link #getFont}, {@link #getLabelText} and {@link #getIcon}. Default
     *  implementation sets the above properties in the label.
     */
    public void                 configureLabel (
        JLabel                      label,
        boolean                     selected,
        boolean                     hasFocus
    )
    {
        label.setOpaque (!selected);
        
        Color           c = getColor ();
        Font            f = getFont (label.getFont ());
        
        if (c != null)
            label.setForeground (c);
        
        if (f != null)
            label.setFont (f);

        Icon            icon = getIcon ();
        
        label.setIcon (icon);
        label.setDisabledIcon (icon);
        label.setText (getLabelText ());        
    }
    
    /**
     *  User can override this method instead of {@link #configureLabel}, 
     *  to realize the full power of JTree.
     */
    public JComponent   render (
        boolean             selected,
        boolean             hasFocus,
        JLabel              defaultRendering
    ) 
    {
        configureLabel (defaultRendering, selected, hasFocus);
        return (defaultRendering);
    }
    
    /**
     *  Return a popup menu to show when this node is activated
     *  by a popup trigger (such as a right mouse button click).
     *  Return null if no menu should be shown.
     */
    public JPopupMenu           getMenu () {
        return (null);
    }
    
    /**
     *  Return the number of children, or {@link #LEAF_NODE}.
     *  Default implementation returns {@link #LEAF_NODE}.
     */
    public int                  getNumChildren () {
        return (LEAF_NODE);
    }
    
    /**
     *  Returns a child by index. Default implementation throws an
     *  IllegalStateException.
     */
    public TreeEditorNode       getChild (int idx) {
        throw new IllegalStateException ("this is a leaf node");
    }
    
    /**
     *  Determine whether there is anything to edit. Default implementation
     *  returns true.
     */
    public boolean              isEditable () {
        return (true);        
    }
    
    /**
     *  Called to add content to an empty form. 
     *  Default implementation does nothing.
     */
    public void                 configureForm (VerticalForm form) {        
    }
    
    /**
     *  Called after the form becomes editable.
     */
    public void                 beginEdit (VerticalForm form) {    
    }
    
    /**
     *  Called after the node is succesfully edited in creation mode.
     */
    public void                 created () {    
    }
    
    /**
     *  Called after the node is succesfully edited NOT in creation mode.
     */
    public void                 updated () {    
    }
    
    /**
     *  Called after the user fails to complete the creation process.
     */
    public void                 creationCanceled () {    
    }
    
    /**
     *  Called when user hits "Save". Return whether it is OK to proceed
     *  with saving the values. If false is returned, the values in the
     *  user object must not be edited.
     */
    public boolean              acceptChanges () {
        return (true);
    }
    
    /**
     *  Updates the display of this node and its children.
     */
    public final void           reload () {
        if (mCallback != null)
            mCallback.reload ();        
    }
    
    /**
     *  Select this node
     */
    public final void           select () {
        if (mCallback != null)
            mCallback.select ();
    }
    
    /**
     *  Select this node and start editing it
     */
    public final void           selectAndStartEditing () {
        mCallback.selectAndStartEditing ();
    }
    
    /**
     *  Edit the specified node (usually unconnected to the tree).
     *  If editing succeeds, node gets an additional created () notification. 
     *  If editing is canceled, node gets a creationCanceled () notification.
     */
    public final void           editInCreationMode (TreeEditorNode node) {
        mCallback.editInCreationMode (node);        
    }
    
    public final void           updateFormHeaderFromEditedNode () {
        mCallback.updateFormHeaderFromEditedNode ();
    }
    
    /**
     *  Returns the parent TreeEditorNode
     */
    public TreeEditorNode       getParent () {
        NodeAdapter nap = (NodeAdapter) mCallback.getParent ();
        
        return (nap == null ? null : nap.getUserNode ());
    }
    
    /**
     *  Sets the accessibility of the Save button
     */
    protected final void        setSaveEnabled (boolean flag) {
        mCallback.setSaveEnabled (flag);
    }
    
    /**
     *  Sets the accessibility of the Cancel button - use with caution,
     *  user should normally be able to cancel.
     */
    protected final void        setCancelEnabled (boolean flag) {
        mCallback.setCancelEnabled (flag);
    }
    
    /**
     *  Expand the tree and scroll to make this node visible
     */
    public void                 show () {
        mCallback.show ();
    }
    
    public final TreeEditorNode find (Filter filter) {
        if (mCallback == null)
            throw new IllegalStateException (this + " is not in the tree");
        
        if (filter.accept (this))
            return (this);
        
        mCallback.updateChildren ();
        
        int     numChildren = getNumChildren ();
        
        for (int ii = 0; ii < numChildren; ii++) {
            TreeEditorNode  node = getChild (ii).find (filter);
            
            if (node != null)
                return (node);
        }
        
        return (null);
    }
    
    public final TreeEditorNode findNodeByType (final Class <TreeEditorNode> type) {
        return (
            find (
                new Filter () {
                    public boolean accept (TreeEditorNode node) {
                        return (type.isAssignableFrom (node.getClass ()));
                    }
                }
            )
        );
    }    
}
