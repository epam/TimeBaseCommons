package deltix.util.swing.tree;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.jidesoft.plaf.basic.*;
import com.jidesoft.swing.*;
import com.jidesoft.tree.*;

import deltix.qsrv.ui.util.tree.qsrv.*;

public abstract class BaseMutableTreeNode extends LazyMutableTreeNode {

    public static Locale LOCALE = Locale.getDefault ( );

    public static final Object findTreeNode ( JTree tree,
                                              Object userObject ) {
        Object root = tree.getModel ( ).getRoot ( );
        // Traverse tree from root
        return findTreeNode ( tree,
                              new TreePath ( root ),
                              userObject );
    }

    public static final Object findTreeNode ( JTree tree,
                                              TreePath parent,
                                              Object userObject ) {
        // Traverse children
        Object node = parent.getLastPathComponent ( );

        if (node instanceof TaxonomyTreeNode && JideSwingUtilities.equals ( userObject,
                                                                      ((TaxonomyTreeNode) node).getUserObject ( ) )) {
            return node;
        }

        if (tree.getModel ( ).getChildCount ( node ) >= 0) {
            for (int i = 0; i < tree.getModel ( ).getChildCount ( node ); i++) {
                Object n = tree.getModel ( ).getChild ( node,
                                                        i );
                TreePath path = parent.pathByAddingChild ( n );
                Object result = findTreeNode ( tree,
                                               path,
                                               userObject );
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected final JPopupMenu mMenu = new JPopupMenu ( );

    protected final JTree      mTree;

    public BaseMutableTreeNode ( Object userObject,
                                 boolean allowsChildren,
                                 JTree tree ) {
        super ( userObject, allowsChildren );
        mTree = tree;
        createMenu ( );
    }

    public BaseMutableTreeNode ( Object userObject,
                                 JTree tree ) {
        super ( userObject );
        mTree = tree;
        createMenu ( );
    }

    protected void createMenu ( ) {
    }

    public final JTree getTree ( ) {
        return mTree;
    }

    protected final void updateChildren ( ) {
        synchronized (this) {
            if (!_loaded) {
                _loaded = true;
                initChildren ( );
            }
        }
    }

    public void reload ( ) {
        clear ( );
        updateChildren ( );
        refreshTree ( );
    }

    public String getLabelText ( ) {
        return userObject.toString ( );
    }

    public Icon getIcon ( ) {
        return null;
    }

    protected String getTooltip ( ) {
        return null;
    }

    public TreePath getTreePath ( ) {
        int depth = 1;
        TreeNode node = this;

        for (;;) {
            TreeNode next = node.getParent ( );

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

            node = node.getParent ( );
        }

        return (new TreePath ( path ));
    }

    protected TreeModel getActualModel ( ) {
        TreeModel model = mTree.getModel ( );

        FilterableTreeModel filterableTreeModel = getFilterableTreeModel ( );
        if (filterableTreeModel != null) {
            model = filterableTreeModel.getActualModel ( );
        }

        return model;
    }

    protected FilterableTreeModel getFilterableTreeModel ( ) {
        TreeModel model = mTree.getModel ( );
        if (model instanceof FilterableTreeModel) {
            return (FilterableTreeModel) model;
        }
        return null;
    }

    protected DefaultTreeModel getActualDefaultTreeModel ( ) {
        TreeModel model = getActualModel ( );
        if (model instanceof DefaultTreeModel) {
            return (DefaultTreeModel) model;
        }
        return null;
    }

    public final void delete ( ) {
        DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.removeNodeFromParent ( this );
        }
        refreshTree ( );
    }

    public final void refreshTree ( ) {
        FilterableTreeModel _displayTreeModel = getFilterableTreeModel ( );
        if (_displayTreeModel != null) {

            // save selection and expansion states
            Enumeration<TreePath> enumeration = null;
            TreePath[] selected = null;
            if (getTree ( ) != null) {
                enumeration = TreeUtils.saveExpansionStateByTreePath ( getTree ( ) );
                selected = TreeUtils.saveSelection ( getTree ( ) );
            }

            _displayTreeModel.refresh ( );

            // restore selection and expansion states
            if (getTree ( ) != null) {
                if (enumeration != null) {
                    TreeUtils.loadExpansionStateByTreePath ( getTree ( ),
                                                             enumeration );
                }
                if (selected != null) {
                    TreeUtils.loadSelection ( getTree ( ),
                                              selected );
                }
            }
        }
    }

    public void nodeChanged ( ) {
        DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.nodeChanged ( this );
        }
    }

    public void nodeStructureChanged ( ) {
        DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.nodeStructureChanged ( this );
        }
    }

    public final void select ( ) {
        /**
         * Reset selection momentarily in order to force the reloading of the
         * node's form.
         */
        mTree.setSelectionPath ( null );
        TreePath path = getTreePath ( );
        mTree.setSelectionPath ( path );
        mTree.expandPath ( path );
    }

    public JPopupMenu getMenu ( ) {
        return mMenu;
    }

    /**
     * Return the font for the label to display.
     */
    protected Font getFont ( Font defFont ) {
        return (defFont);
    }

    /**
     * Return the color for the label to display, or null.
     */
    protected Color getColor ( ) {
        return (null);
    }

    /**
     * User can override this method instead of {@link #configureLabel}, to
     * realize the full power of JTree.
     */
    private JComponent render ( boolean selected,
                                boolean hasFocus,
                                JLabel defaultRendering ) {
        configureLabel ( defaultRendering,
                         selected,
                         hasFocus );
        return (defaultRendering);
    }

    /**
     * User can override this method instead of {@link #getColor},
     * {@link #getFont}, {@link #getLabelText} and {@link #getIcon}. Default
     * implementation sets the above properties in the label.
     */
    public void configureLabel ( JLabel label,
                                 boolean selected,
                                 boolean hasFocus ) {

        Color c = getColor ( );
        Font f = getFont ( label.getFont ( ) );

        if (c != null)
            label.setForeground ( c );

        if (f != null)
            label.setFont ( f );

        Icon icon = getIcon ( );

        label.setIcon ( icon );
        label.setDisabledIcon ( icon );
        label.setText ( getLabelText ( ) );
    }

    public static class NodeRenderer extends DefaultTreeCellRenderer {

        private Font              mDefaultFont     = getFont ( );

        public Component getTreeCellRendererComponent ( JTree jTree,
                                                        Object node,
                                                        boolean selected,
                                                        boolean expanded,
                                                        boolean leaf,
                                                        int row,
                                                        boolean hasFocus ) {

            JLabel label = (JLabel) super.getTreeCellRendererComponent ( jTree,
                                                                         node,
                                                                         selected,
                                                                         expanded,
                                                                         leaf,
                                                                         row,
                                                                         hasFocus );

            if (node instanceof BaseMutableTreeNode) {

                String tootip = ((BaseMutableTreeNode) node).getTooltip ( );
                if (tootip != null)
                    setToolTipText ( tootip );
            }

            /**
             * Reset font because it may have been tweaked
             */
            label.setFont ( mDefaultFont );

            BaseMutableTreeNode anode = (BaseMutableTreeNode) node;
            return anode.render ( selected,
                                  hasFocus,
                                  label );
        }
    }
}
