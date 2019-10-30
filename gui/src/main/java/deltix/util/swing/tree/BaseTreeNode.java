package deltix.util.swing.tree;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.jidesoft.tree.*;

import deltix.util.collections.*;

public abstract class BaseTreeNode<T> implements TreeNode {
    
    protected BaseTreeNode<?>                               mParent;
    protected boolean                                       mChildrenUpdated = false;
    protected java.util.List<BaseTreeNode<?>>               mChildNodes;
    
    protected static Locale                                 LOCALE = Locale.getDefault();
    
    protected T                                             mNodeObject;
    protected final JPopupMenu                              mMenu = new JPopupMenu();
    
    protected final JTree                                   mTree;
    
    public BaseTreeNode(T nodeObject, JTree tree) {
        mNodeObject = nodeObject;
        mTree = tree;
        createMenu();
    }

    public BaseTreeNode(BaseTreeNode<?> parent, T nodeObject) {
        mParent = parent;
        mNodeObject = nodeObject;
        mTree = parent.getTree();
        createMenu();
    }
    
    protected void createMenu(){
    }
    
    private JTree getTree(){
        return mTree;
    }
    
    public void  processKeyEvent(KeyEvent e){
        //dummy function
    }
    
    
    abstract protected void updateChildren();

    abstract public boolean isLeaf();

    public String getLabelText(){
        return mNodeObject.toString();        
    }

    public Icon getIcon() {
        return null;
    }

    protected String getTooltip() {
        return null;
    }
 
    
    public TreePath getPath() {
        int depth = 1;
        TreeNode node = this;

        for (;;) {
            TreeNode next = node.getParent();

            if (next == null)
                break;

            depth++;
            node = next;
        }

        Object[] path = new Object[depth];

        node = this;

        for (;;) {
            depth--;
            path[depth] = node;

            if (depth == 0)
                break;

            node = node.getParent();
        }

        return (new TreePath(path));
    }
    
    final void select() {
        /**
         * Reset selection momentarily in order to force the reloading of the
         * node's form.
         */
        mTree.setSelectionPath(null);

        TreePath path = getPath();

        mTree.setSelectionPath(path);
        mTree.expandPath(path);
    }

    private DefaultTreeModel getDefaultTreeModel(){
        TreeModel model = mTree.getModel();
        if (model instanceof FilterableTreeModel){
            model = ((FilterableTreeModel)model).getActualModel();
        }
        if (model instanceof DefaultTreeModel){
            return (DefaultTreeModel) model;
        }
        return null;
    }
    
    public void reload() {
        mChildrenUpdated = false;
        updateChildren();
        
        DefaultTreeModel model = getDefaultTreeModel();
        if (model != null)
            model.reload(this);
        
    }
    
    public void nodeChanged() {
        DefaultTreeModel model = getDefaultTreeModel();
        if (model != null)
            model.nodeChanged(this);
    }
    
    public T getNodeObject() {
        return mNodeObject;
    }
    
    @Override
    public TreeNode getChildAt(int idx) {
        updateChildren();
        return (mChildNodes.get(idx));
    }

    @Override
    public int getChildCount() {
        updateChildren();
        return (mChildNodes == null ? 0 : mChildNodes.size());
    }

    @Override
    public int getIndex(TreeNode treeNode) {
        updateChildren();
        if (mChildNodes == null)
            return (-1);
    
        for (int ii = 0; ii < mChildNodes.size(); ii++)
            if (mChildNodes.get(ii) == treeNode)
                return (ii);
    
        return (-1);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Enumeration children() {
        updateChildren();
        return (new ArrayEnumeration(mChildNodes.toArray()));
    }

    @Override
    public boolean getAllowsChildren() {
        updateChildren();
        return (mChildNodes != null);
    }

    @Override
    public TreeNode getParent() {
        return (mParent);
    }
   
    
    public JPopupMenu getMenu() {
        return mMenu;
    }
    
    /**
     * Return the font for the label to display.
     */
    protected Font getFont(Font defFont) {
        return (defFont);
    }

    /**
     * Return the color for the label to display, or null.
     */
    protected Color getColor() {
        return (null);
    }
    
    /**
     * User can override this method instead of {@link #configureLabel}, to
     * realize the full power of JTree.
     */
    private JComponent render(boolean selected, boolean hasFocus, JLabel defaultRendering) {
        configureLabel(defaultRendering, selected, hasFocus);
        return (defaultRendering);
    }
    
    /**
     *  User can override this method instead of {@link #getColor}, 
     *  {@link #getFont}, {@link #getLabelText} and {@link #getIcon}. Default
     *  implementation sets the above properties in the label.
     */
    public void configureLabel(JLabel label, boolean selected, boolean hasFocus) {

        Color c = getColor();
        Font f = getFont(label.getFont());

        if (c != null)
            label.setForeground(c);

        if (f != null)
            label.setFont(f);

        Icon icon = getIcon();

        label.setIcon(icon);
        label.setDisabledIcon(icon);
        label.setText(getLabelText());
    }
    
    public static class NodeRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 1L;
        private Font mDefaultFont = getFont();

        public Component getTreeCellRendererComponent(
                                                      JTree jTree,
                                                      Object node,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus) {
            
            
            JLabel label = (JLabel) super.getTreeCellRendererComponent(
                    jTree,
                    node,
                    selected,
                    expanded,
                    leaf,
                    row,
                    hasFocus);

            if (node instanceof BaseTreeNode) {

                String tootip = ((BaseTreeNode<?>) node).getTooltip();
                if (tootip != null)
                    setToolTipText(tootip);
            }

            /**
             * Reset font because it may have been tweaked
             */
            label.setFont(mDefaultFont);

            BaseTreeNode<?> anode = (BaseTreeNode<?>) node;
            return anode.render(selected, hasFocus, label);
        }
    }
   
}
