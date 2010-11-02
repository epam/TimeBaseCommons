package deltix.util.swing.tree;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.jdesktop.jxlayer.plaf.ext.*;
import org.springframework.util.*;

import com.jidesoft.grid.*;
import com.jidesoft.swing.*;
import com.jidesoft.utils.*;

import deltix.qsrv.ui.util.*;
import deltix.util.lang.StringUtils;

public class QuickNodeFilterField extends QuickFilterField {

    private transient ChangeEvent  _changeEvent = null;
    private TreeModel              _treeModel;
    private boolean                _filterAdded = false;
    private Pattern                _pattern;
    private String                 _searchText;

    private DefaultWildcardSupport _defaultWildcardSupport;
    private final LockableUI       _lockableUI;

    public QuickNodeFilterField (final LockableUI lockableUI) {
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

    @Override
    protected JLabel createLabel () {
        return null;
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
            FilterableNode<?> root = null;
            setLocked (true);
            try {
                final Object o = _treeModel.getRoot ();
                Assert.isInstanceOf (FilterableNode.class,
                                     o);
                root = (FilterableNode<?>) o;
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

    public void setLocked (final boolean isLocked) {
        if (_lockableUI != null) {
            _lockableUI.setLocked (isLocked);
        }
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
                               final String searchingText) {
        // note this method is same as the same-name method in Searchable
        if (searchingText == null || searchingText.trim ().length () == 0) {
            _pattern = null;
            _searchText = searchingText;
            return true;
        }

        if (_searchText != null && _searchText.equals (searchingText) && _pattern != null) {
            return _pattern.matcher (text).matches ();
        }

        _searchText = searchingText;

        try {
            _pattern = Pattern.compile (convertFromPatternToRegex (searchingText),
                                        Pattern.CASE_INSENSITIVE);
            return _pattern.matcher (text).matches ();
        } catch (final PatternSyntaxException e) {
            return false;
        }

    }

    @Override
    protected JidePopupMenu createContextMenu () {
        return null;
    }

    private String convertFromPatternToRegex (final String pattern) {
        if (pattern == null) {
            return null;
        }

        if (_defaultWildcardSupport == null) {
            _defaultWildcardSupport = new DefaultWildcardSupport () {

                @Override
                public String convert (final String s) {
                    final char c = getZeroOrMoreQuantifier ();
                    final int i = c != 0 ? s.indexOf (c) : -1;
                    final char c1 = getZeroOrOneQuantifier ();
                    final int j = c1 != 0 ? s.indexOf (c1) : -1;
                    final char c2 = getOneOrMoreQuantifier ();
                    final int k = c2 != 0 ? s.indexOf (c2) : -1;
                    if (i == -1 && j == -1 && k == -1)
                        return s;
                    final StringBuffer stringbuffer = new StringBuffer ();
                    final int l = s.length ();
                    for (int i1 = 0; i1 < l; i1++) {
                        final char c3 = s.charAt (i1);
                        if (c1 != 0 && c3 == c1) {
                            stringbuffer.append (".");
                            continue;
                        }
                        if (c != 0 && c3 == c) {
                            stringbuffer.append (".*");
                            continue;
                        }
                        if (c2 != 0 && c3 == c2) {
                            stringbuffer.append ("..*");
                            continue;
                        }
                        if ("(){}[].^$\\".indexOf (c3) != -1) {
                            stringbuffer.append ('\\');
                            stringbuffer.append (c3);
                        } else {
                            stringbuffer.append (c3);
                        }
                    }

                    return stringbuffer.toString ();
                }
            };
        }

        return _defaultWildcardSupport.convert (pattern);
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
        return listenerList.getListeners (ChangeListener.class);
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