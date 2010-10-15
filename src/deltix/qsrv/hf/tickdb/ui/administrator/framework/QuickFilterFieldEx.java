package deltix.qsrv.hf.tickdb.ui.administrator.framework;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.jdesktop.jxlayer.plaf.ext.*;
import org.springframework.util.*;

import com.jidesoft.grid.*;

import deltix.qsrv.hf.tickdb.ui.administrator.tree.*;
import deltix.qsrv.ui.util.*;
import deltix.util.lang.StringUtils;

final class QuickFilterFieldEx extends QuickFilterField {

    private transient ChangeEvent _changeEvent = null;
    private TreeModel             _treeModel;
    private boolean               _filterAdded = false;
    private Pattern               _pattern;
    private String                _searchText;

    private final LockableUI      _lockableUI;

    public QuickFilterFieldEx (final LockableUI lockableUI) {
        super ();
        setWildcardEnabled (true);
        getTextField ().getDocument ().addDocumentListener (new DocumentListener () {

            @Override
            public void removeUpdate (final DocumentEvent e) {
                fireStateChanged ();
            }

            @Override
            public void insertUpdate (final DocumentEvent e) {
                fireStateChanged ();
            }

            @Override
            public void changedUpdate (final DocumentEvent e) {
            }
        });
        setResetIcon (Icons.RESET);
        _lockableUI = lockableUI;
    }

    public void setTreeModel (final TreeModel treeModel) {
        _treeModel = treeModel;
        _filterAdded = false;
        if (_treeModel != null) {
            applyFilter ();
        }
    }

    public boolean isFiltersApplied () {
        final String searchingText = getSearchingText ();
        return (_searchText == null && searchingText != null && searchingText.isEmpty ()) ||
               (searchingText == null && _searchText != null && _searchText.isEmpty ()) ||
               (StringUtils.equals (_searchText,
                                    searchingText));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyFilter (final String text) {
        if (_treeModel != null) {

            TickDBTreeNode root = null;
            try {
                setLocked (true);
                final Object o = _treeModel.getRoot ();
                Assert.isInstanceOf (TickDBTreeNode.class,
                                     o);
                root = (TickDBTreeNode) o;
                if (!_filterAdded) { // only add filter for the first time.
                    root.addFilter (getFilter ());
                    _filterAdded = true;
                }
                if (root != null)
                    root.reload ();
            } finally {
                setLocked (false);
            }

        }
        fireStateChanged ();
    }

    private void setLocked (final boolean isLocked) {
        if (_lockableUI != null)
            _lockableUI.setLocked (isLocked);
    }

    @Override
    protected com.jidesoft.filter.Filter<String> createFilter () {
        return new com.jidesoft.filter.AbstractFilter<String> () {
            @Override
            public boolean isValueFiltered (final String value) {
                return !compare (value,
                                 _searchingText);
            }
        };
    }

    @Override
    protected boolean compare (final String text,
                               final String searchingText) { // note this method
        // is same as the
        // same-name method
        // in Searchable
        if (searchingText == null || searchingText.trim ().length () == 0) {
            _pattern = null;
            _searchText = searchingText;
            return true;
        }

        if (!isWildcardEnabled ()) {
            return searchingText != null &&
                   (searchingText.equals (text) || searchingText.length () > 0
                                                   &&
                                                   (isFromStart () ? text.startsWith (searchingText)
                                                                  : text.indexOf (searchingText) != -1));
        } else {
            if (_searchText != null && _searchText.equals (searchingText) && _pattern != null) {
                return _pattern.matcher (text).find ();
            }

            _searchText = searchingText;

            try {
                _pattern = Pattern.compile (isFromStart () ? ("^" + searchingText) : searchingText,
                                            isCaseSensitive () ? 0 : Pattern.CASE_INSENSITIVE);
                return _pattern.matcher (text).find ();
            } catch (final PatternSyntaxException e) {
                return false;
            }
        }
    }

    @Override
    protected AbstractButton createButton () {
        final JButton button = new JButton () {
            @Override
            public Dimension getPreferredSize () {
                return new Dimension (getResetIcon ().getIconWidth (),
                                      getResetIcon ().getIconHeight ());
            }

            @Override
            public void updateUI () {
                super.updateUI ();
                setOpaque (false);
                setContentAreaFilled (false);
                setBorderPainted (false);
                setBorder (BorderFactory.createEmptyBorder ());
            }
        };
        button.addActionListener (new AbstractAction () {
            @Override
            public void actionPerformed (final ActionEvent e) {
                _textField.setText ("");
                applyFilter ();
            }
        });
        button.setRequestFocusEnabled (false);
        button.setFocusable (false);
        return button;
    }

    public void addChangeListener (final ChangeListener l) {
        listenerList.add (ChangeListener.class,
                          l);
    }

    public void removeChangeListener (final ChangeListener l) {
        listenerList.remove (ChangeListener.class,
                             l);
    }

    public ChangeListener[] getChangeListeners () {
        return listenerList.getListeners (
                ChangeListener.class);
    }

    protected void fireStateChanged () {
        final Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (_changeEvent == null) {
                    _changeEvent = new ChangeEvent (this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged (_changeEvent);
            }
        }
    }
}