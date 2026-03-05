package deltix.util.swing.tree;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.jidesoft.plaf.basic.*;
import com.jidesoft.swing.*;
import com.jidesoft.tree.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class BaseMutableTreeNode extends LazyMutableTreeNode {

    public static Locale LOCALE = Locale.getDefault ();

    public static Object findTreeNode (final JTree tree,
                                       final Object userObject) {
        final Object root = tree.getModel ().getRoot ();
        // Traverse tree from root
        return findTreeNode (tree,
                             new TreePath (root),
                             userObject);
    }

    public static Object findTreeNode (final JTree tree,
                                       final TreePath parent,
                                       final Object userObject) {
        // Traverse children
        final Object node = parent.getLastPathComponent ();

        if (node instanceof DefaultMutableTreeNode &&
                JideSwingUtilities.equals (userObject,
                                           ((DefaultMutableTreeNode) node).getUserObject ())) {
            return node;
        }

        if (tree.getModel ().getChildCount (node) >= 0) {
            for (int i = 0; i < tree.getModel ().getChildCount (node); i++) {
                final Object n = tree.getModel ().getChild (node,
                                                            i);
                final TreePath path = parent.pathByAddingChild (n);
                final Object result = findTreeNode (tree,
                                                    path,
                                                    userObject);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected final JTree           tree;
    private         ReloadTreeTask  currentReloadTask  = null;
    private         DeleteNodeTask  currentDeleteTask  = null;

    public BaseMutableTreeNode (final Object userObject,
                                final boolean allowsChildren,
                                final JTree tree) {
        super (userObject,
               allowsChildren);
        this.tree = tree;
        createMenu ();
    }

    public BaseMutableTreeNode (final Object userObject,
                                final JTree tree) {
        this (userObject, true, tree);
    }

    public BaseMutableTreeNode (final JTree tree) {
        this(null, true, tree);
    }


    protected void createMenu () {
    }

    public final JTree getTree () {
        return tree;
    }

    protected final void updateChildren () {
        synchronized (this) {
            if (!_loaded) {
                _loaded = true;
                initChildren ();
            }
        }
    }

    public  void reload () {
        if (!preProcess(currentReloadTask, false)){
            return;
        }
        currentReloadTask = new ReloadTreeTask(this);
        currentReloadTask.execute();
    }

    private boolean preProcess (SwingWorker task, final boolean interruptIfRun) {
        if (task != null && !task.isDone () && !task.isCancelled ()) {
            if (interruptIfRun) {
                task.cancel (false);
            } else
                return false;
        }
        return true;
    }

    public String getLabelText () {
        return userObject.toString ();
    }

    public Icon getIcon () {
        return null;
    }

    protected String getTooltip () {
        return null;
    }

    public TreePath getTreePath () {
        return getTreePath(this);
    }

    public TreePath getParentTreePath () {
        TreeNode node = this.getParent();

        return node != null ? getTreePath(node) : null;
    }

    public static TreePath getTreePath (TreeNode treeNode) {
        int depth = 1;

        TreeNode node = treeNode;

        for (;;) {
            final TreeNode next = node.getParent ();

            if (next == null)
                break;

            depth++;
            node = next;
        }

        final Object[] path = new Object[depth];

        node = treeNode;

        for (;;) {
            depth--;
            path[depth] = node;

            if (depth == 0)
                break;

            node = node.getParent ();
        }

        return (new TreePath (path));
    }

    protected TreeModel getActualModel () {
        TreeModel model = tree.getModel ();

        final FilterableTreeModel filterableTreeModel = getFilterableTreeModel ();
        if (filterableTreeModel != null) {
            model = filterableTreeModel.getActualModel ();
        }

        return model;
    }

    protected FilterableTreeModel getFilterableTreeModel () {
        final TreeModel model = tree.getModel ();
        if (model instanceof FilterableTreeModel) {
            return (FilterableTreeModel) model;
        }
        return null;
    }

    protected DefaultTreeModel getActualDefaultTreeModel () {
        final TreeModel model = getActualModel();
        if (model instanceof DefaultTreeModel) {
            return (DefaultTreeModel) model;
        }
        return null;
    }

    public void delete () {
        if (this.parent != null) {
            synchronized (this.parent) {
                if (!preProcess(currentDeleteTask, false)) {
                    return;
                }
                currentDeleteTask = new DeleteNodeTask(this);
                currentDeleteTask.execute();
            }
        }
    }

    public void refreshTree () {

        // save selection and expansion states
        Enumeration<TreePath> enumeration = null;
        TreePath[] selected = null;
        if (getTree () != null) {
            enumeration = TreeUtils.saveExpansionStateByTreePath (getTree ());
            selected = TreeUtils.saveSelection (getTree ());
        }

        refresh ();

        // restore selection and expansion states
        if (getTree () != null) {
            if (enumeration != null) {
                TreeUtils.loadExpansionStateByTreePath (getTree (),
                                                        enumeration);
            }
            if (selected != null) {
                TreeUtils.loadSelection (getTree (),
                                         selected);
            }
        }

    }

    protected void refresh () {
        final FilterableTreeModel displayTreeModel = getFilterableTreeModel ();
        if (displayTreeModel != null) {
            displayTreeModel.refresh ();
        } else {
            final TreeModel model = tree.getModel ();
            if (model instanceof AbstractTreeModel)
                fireTreeStructureChanged ((AbstractTreeModel) model,
                                          new TreePath (getRoot ()));
            else if (model instanceof DefaultTreeModel)
                ((DefaultTreeModel) model).reload ();
        }
    }

    @SuppressFBWarnings(value="EC_UNRELATED_TYPES_USING_POINTER_EQUALITY", justification = "Legacy code. Developer stored pairs Class,InstanceOfClass in listeners array")
    protected void fireTreeStructureChanged (final AbstractTreeModel source,
                                             final TreePath path) {
        // Guaranteed to return a non-null array
        final Object[] listeners = source.getTreeModelListeners ();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent (source,
                                            path);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged (e);
            }
        }
    }

    @SuppressFBWarnings(value="EC_UNRELATED_TYPES_USING_POINTER_EQUALITY", justification = "Legacy code. Developer stored pairs Class,InstanceOfClass in listeners array")
    protected void fireTreeStructureChanged (final DefaultTreeModel source,
                                             final TreePath path) {
        // Guaranteed to return a non-null array
        final Object[] listeners = source.getTreeModelListeners ();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent (source,
                                            path);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged (e);
            }
        }
    }

    public void nodeChanged () {
        final DefaultTreeModel model = getActualDefaultTreeModel ();
        if (model != null) {
            model.nodeChanged (this);
        }
    }

    public void nodeStructureChanged () {
        final DefaultTreeModel model = getActualDefaultTreeModel ();
        if (model != null) {
            model.nodeStructureChanged (this);
        }
    }

    public final void insertNodeInto (final BaseMutableTreeNode node) {
        final DefaultTreeModel model = getActualDefaultTreeModel ();
        if (model != null) {
            model.insertNodeInto (node,
                                  this,
                                  this.getChildCount ());
        } else {
            reload ();
        }

        add (node);
    }

    public final void select () {
        /**
         * Reset selection momentarily in order to force the reloading of the
         * node's form.
         */
        tree.setSelectionPath (null);
        final TreePath path = getTreePath ();
        tree.setSelectionPath (path);
        tree.expandPath (path);
    }

    /**
     * Return the font for the label to display.
     */
    protected Font getFont (final Font defFont) {
        return (defFont);
    }

    /**
     * Return the color for the label to display, or null.
     */
    protected Color getColor () {
        return (null);
    }

    /**
     * User can override this method instead of {@link #configureLabel}, to
     * realize the full power of JTree.
     */
    private JComponent render (final boolean selected,
                               final boolean hasFocus,
                               final JLabel defaultRendering) {
        configureLabel (defaultRendering,
                        selected,
                        hasFocus);
        return (defaultRendering);
    }

    /**
     * User can override this method instead of {@link #getColor},
     * {@link #getFont}, {@link #getLabelText} and {@link #getIcon}. Default
     * implementation sets the above properties in the label.
     */
    public void configureLabel (final JLabel label,
                                final boolean selected,
                                final boolean hasFocus) {

        final Color c = getColor ();
        final Font f = getFont (label.getFont ());

        if (c != null)
            label.setForeground (c);

        if (f != null)
            label.setFont (f);

        final Icon icon = getIcon ();

        label.setIcon (icon);
        label.setDisabledIcon (icon);
        label.setText (getLabelText ());
    }

    public static class NodeRenderer extends DefaultTreeCellRenderer {

        private final Font _defaultFont = getFont ();

        @Override
        public Component getTreeCellRendererComponent (final JTree jTree,
                                                       final Object node,
                                                       final boolean selected,
                                                       final boolean expanded,
                                                       final boolean leaf,
                                                       final int row,
                                                       final boolean hasFocus) {

            final JLabel label = (JLabel) super.getTreeCellRendererComponent (jTree,
                                                                              node,
                                                                              selected,
                                                                              expanded,
                                                                              leaf,
                                                                              row,
                                                                              hasFocus);

            if (node instanceof BaseMutableTreeNode) {

                final String tootip = ((BaseMutableTreeNode) node).getTooltip ();
                setToolTipText (tootip);
                /**
                 * Reset font because it may have been tweaked
                 */
                label.setFont (_defaultFont);

                final BaseMutableTreeNode anode = (BaseMutableTreeNode) node;
                return anode.render (selected,
                                     hasFocus,
                                     label);
            }

            return label;


        }
    }

    public  static class ReloadTreeTask extends SwingWorker<Object, Object> {
        private BaseMutableTreeNode processedNode;

        public ReloadTreeTask(BaseMutableTreeNode processedNode) {
            this.processedNode = processedNode;
        }

        @Override
        public Object doInBackground() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        processedNode.clear();
                        processedNode.updateChildren();
                        processedNode.refreshTree();
                    }
                });
            return this;
        }

        @Override
        protected void done() {
            try {
                if (!isCancelled())
                    get();
            }  catch (Throwable ignore) {
          }
        }
    }

    public  static class DeleteNodeTask extends SwingWorker<Object, Object> {
        private BaseMutableTreeNode processedNode;

        public DeleteNodeTask(BaseMutableTreeNode processedNode) {
            this.processedNode = processedNode;
        }

        @Override
        public Object doInBackground() {
            final DefaultTreeModel model = processedNode.getActualDefaultTreeModel();
            if (model != null) {
                model.removeNodeFromParent(processedNode);
            }
            return this;
        }

        @Override
        protected void done() {
            try {
                if (!isCancelled())
                    get();
            } catch (Throwable ignore) {
            }
        }
    }

}
