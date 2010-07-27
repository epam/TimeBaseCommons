package deltix.util.swing.tree;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.jidesoft.plaf.basic.*;
import com.jidesoft.swing.*;
import com.jidesoft.tree.*;

public abstract class BaseMutableTreeNode extends LazyMutableTreeNode {

    public static Locale LOCALE = Locale.getDefault ( );

    public static final Object findTreeNode ( final JTree tree,
                                              final Object userObject ) {
        final Object root = tree.getModel ( ).getRoot ( );
        // Traverse tree from root
        return findTreeNode ( tree,
                              new TreePath ( root ),
                              userObject );
    }

    public static final Object findTreeNode ( final JTree tree,
                                              final TreePath parent,
                                              final Object userObject ) {
        // Traverse children
        final Object node = parent.getLastPathComponent ( );

        if (node instanceof BaseMutableTreeNode
            && JideSwingUtilities.equals ( userObject,
                                           ((BaseMutableTreeNode) node).getUserObject ( ) )) {
            return node;
        }

        if (tree.getModel ( ).getChildCount ( node ) >= 0) {
            for (int i = 0; i < tree.getModel ( ).getChildCount ( node ); i++) {
                final Object n = tree.getModel ( ).getChild ( node,
                                                              i );
                final TreePath path = parent.pathByAddingChild ( n );
                final Object result = findTreeNode ( tree,
                                                     path,
                                                     userObject );
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected final JPopupMenu _menu = new JPopupMenu ( );

    protected final JTree      _tree;

    public BaseMutableTreeNode ( final Object userObject,
                                 final boolean allowsChildren,
                                 final JTree tree ) {
        super ( userObject,
                allowsChildren );
        _tree = tree;
        createMenu ( );
    }

    public BaseMutableTreeNode ( final Object userObject,
                                 final JTree tree ) {
        super ( userObject );
        _tree = tree;
        createMenu ( );
    }

    protected void createMenu ( ) {
    }

    public final JTree getTree ( ) {
        return _tree;
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
            final TreeNode next = node.getParent ( );

            if (next == null)
                break;

            depth++;
            node = next;
        }

        final Object[] path = new Object[depth];

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
        TreeModel model = _tree.getModel ( );

        final FilterableTreeModel filterableTreeModel = getFilterableTreeModel ( );
        if (filterableTreeModel != null) {
            model = filterableTreeModel.getActualModel ( );
        }

        return model;
    }

    protected FilterableTreeModel getFilterableTreeModel ( ) {
        final TreeModel model = _tree.getModel ( );
        if (model instanceof FilterableTreeModel) {
            return (FilterableTreeModel) model;
        }
        return null;
    }

    protected DefaultTreeModel getActualDefaultTreeModel ( ) {
        final TreeModel model = getActualModel ( );
        if (model instanceof DefaultTreeModel) {
            return (DefaultTreeModel) model;
        }
        return null;
    }

    public void delete ( ) {
        final DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.removeNodeFromParent ( this );
        }
        refreshTree ( );
    }

    public final void refreshTree ( ) {
        final FilterableTreeModel _displayTreeModel = getFilterableTreeModel ();
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
        final DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.nodeChanged ( this );
        }
    }

    public void nodeStructureChanged ( ) {
        final DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.nodeStructureChanged ( this );
        }
    }

    public final void insertNodeInto ( final BaseMutableTreeNode node ) {
        final DefaultTreeModel model = getActualDefaultTreeModel ( );
        if (model != null) {
            model.insertNodeInto ( node,
                                   this,
                                   this.getChildCount ( ) );
        } else {
            reload ( );
        }

        add ( node );
    }

    public final void select ( ) {
        /**
         * Reset selection momentarily in order to force the reloading of the
         * node's form.
         */
        _tree.setSelectionPath ( null );
        final TreePath path = getTreePath ( );
        _tree.setSelectionPath ( path );
        _tree.expandPath ( path );
    }

    public JPopupMenu getMenu ( ) {
        return _menu;
    }

    /**
     * Return the font for the label to display.
     */
    protected Font getFont ( final Font defFont ) {
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
    private JComponent render ( final boolean selected,
                                final boolean hasFocus,
                                final JLabel defaultRendering ) {
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
    public void configureLabel ( final JLabel label,
                                 final boolean selected,
                                 final boolean hasFocus ) {

        final Color c = getColor ( );
        final Font f = getFont ( label.getFont ( ) );

        if (c != null)
            label.setForeground ( c );

        if (f != null)
            label.setFont ( f );

        final Icon icon = getIcon ( );

        label.setIcon ( icon );
        label.setDisabledIcon ( icon );
        label.setText ( getLabelText ( ) );
    }

    public static class NodeRenderer extends DefaultTreeCellRenderer {

        private final Font _defaultFont = getFont ( );

        @Override
        public Component getTreeCellRendererComponent ( final JTree jTree,
                                                        final Object node,
                                                        final boolean selected,
                                                        final boolean expanded,
                                                        final boolean leaf,
                                                        final int row,
                                                        final boolean hasFocus ) {

            final JLabel label = (JLabel) super.getTreeCellRendererComponent ( jTree,
                                                                               node,
                                                                               selected,
                                                                               expanded,
                                                                               leaf,
                                                                               row,
                                                                               hasFocus );

            if (node instanceof BaseMutableTreeNode) {

                final String tootip = ((BaseMutableTreeNode) node).getTooltip ( );
                if (tootip != null)
                    setToolTipText ( tootip );
            }

            /**
             * Reset font because it may have been tweaked
             */
            label.setFont ( _defaultFont );

            final BaseMutableTreeNode anode = (BaseMutableTreeNode) node;
            return anode.render ( selected,
                                  hasFocus,
                                  label );
        }
    }
}
