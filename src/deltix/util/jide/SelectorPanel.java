package deltix.util.jide;

import static deltix.qsrv.hf.tickdb.ui.administrator.res.CommonResourceBundle.RB;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Position.*;

import com.jidesoft.list.*;
import com.jidesoft.swing.*;

import deltix.qsrv.hf.tickdb.ui.administrator.util.*;
import deltix.util.swing.*;

public abstract class SelectorPanel<T> extends JPanel {

    private CheckBoxList         _list;
    private QuickListFilterField _field;
    private TriStateCheckBox     _tristateCheckBox;

    private boolean              _adjust = false;

    public SelectorPanel () {
        super (new GridBagLayout ());
        init ();
    }

    protected void init () {

        _list = new CheckBoxList () {

            @Override
            public int getNextMatch (final String prefix,
                                     final int startIndex,
                                     final Bias bias) {
                return -1;
            }

        };
        _list.getCheckBoxListSelectionModel ().setSelectionMode (ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        _field = new QuickListFilterField ();
        _field.setHintText (RB.getString ("msg.symbolsFilterHint"));

        _tristateCheckBox = new TriStateCheckBox (RB.getString ("btn.selectDeselectAll"),
                                                  TriStateCheckBox.NOT_SELECTED) {
            @Override
            public void nextState () {
                super.nextState ();
                updateTristateSelection ();
            }

        };

        _field.setList (_list);
        SearchableUtils.installSearchable (_list);

        final CheckBoxListSelectionModel checkBoxListSelectionModel = _list.getCheckBoxListSelectionModel ();

        checkBoxListSelectionModel.addListSelectionListener (new ListSelectionListener () {
            @Override
            public void valueChanged (final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting ())
                    updateTristateCheckBox ();
            }
        });

        final GridBagConstraints c = new GridBagConstraints ();
        c.gridx = 0;
        c.gridy = 0;

        final Insets insets = new Insets (4,
                                          4,
                                          4,
                                          4);
        c.insets = insets;

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        final StyledLabel lblTime = new StyledLabel (getHeader ());
        lblTime.addStyleRange (new StyleRange (Font.BOLD,
                                               Color.BLACK));
        c.gridy++;
        add (new TitledSeparator (lblTime,
                                  TitledSeparator.TYPE_PARTIAL_ETCHED,
                                  SwingConstants.LEFT),
             c);

        c.gridy++;
        add (_tristateCheckBox,
             c);

        c.gridy++;
        c.insets = new Insets (4,
                               4,
                               0,
                               4);
        add (_field,
             c);

        c.gridy++;
        c.insets = new Insets (0,
                               4,
                               4,
                               4);
        final JideScrollPane sp = new JideScrollPane (_list);
        Common.setWeightAndFill (sp,
                                 c);
        add (sp,
             c);
    }

    public void setCellRenderer (ListCellRenderer cellRenderer) {
        _list.setCellRenderer (cellRenderer);
    }

    protected abstract String getHeader ();

    protected abstract java.util.List<T> getDelegate ();

    public final void updateTristateCheckBox () {
        if (!_adjust) {
            _adjust = true;

            int selectedCount = 0;
            final int size = _field.getListModel ().getSize ();
            for (int index = 0; index < size; index++) {
                if (_list.getCheckBoxListSelectionModel ().isSelectedIndex (index))
                    selectedCount++;
            }

            if (selectedCount == 0) {
                _tristateCheckBox.setState (TriStateCheckBox.NOT_SELECTED);
            } else if (selectedCount == size) {
                _tristateCheckBox.setState (TriStateCheckBox.SELECTED);
            } else {
                _tristateCheckBox.setState (TriStateCheckBox.DONT_CARE);
            }

            _adjust = false;
        }
    }

    private void updateTristateSelection () {
        final CheckBoxListSelectionModel checkBoxListSelectionModel = _list.getCheckBoxListSelectionModel ();

        if (!_adjust) {
            _adjust = true;
            if (_tristateCheckBox.getState () == TriStateCheckBox.SELECTED) {
                checkBoxListSelectionModel.addSelectionInterval (0,
                                                                 _field.getListModel ().getSize () - 1);
            } else if (_tristateCheckBox.getState () == TriStateCheckBox.NOT_SELECTED) {
                checkBoxListSelectionModel.clearSelection ();
            }
            _adjust = false;
        }

    }

    protected void rebuildModel () {

        final java.util.List<T> prevoiusSelection = getSelection ();
        final DefaultListModel model = new DefaultListModel ();

        final List<T> delegate = getDelegate ();
        for (final T o : delegate) {
            model.addElement (o);
        }

        _field.setListModel (model);
        _list.setModel (_field.getDisplayListModel ());

        final CheckBoxListSelectionModel checkBoxListSelectionModel = _list.getCheckBoxListSelectionModel ();

        final int size = model.getSize ();
        for (int index = 0; index < size; index++) {
            if (prevoiusSelection.contains (model.get (index))) {
                checkBoxListSelectionModel.setSelectionInterval (index,
                                                                 index);
            }
        }

    }

    public final ListModel getListModel () {
        return _field.getListModel ();
    }

    public final CheckBoxListSelectionModel getCheckBoxListSelectionModel () {
        return _list.getCheckBoxListSelectionModel ();
    }

    @SuppressWarnings("unchecked")
    public final java.util.List<T> getSelection () {
        final ArrayList<T> result = new ArrayList<T> ();
        final DefaultListModel model = (DefaultListModel) _field.getListModel ();
        if (model != null) {
            final CheckBoxListSelectionModel checkBoxListSelectionModel = _list.getCheckBoxListSelectionModel ();

            final int size = model.getSize ();
            for (int index = 0; index < size; index++) {
                if (checkBoxListSelectionModel.isSelectedIndex (index)) {
                    result.add ((T) model.getElementAt (index));
                }
            }
        }

        return result;
    }

    public final void selectAll () {
        _list.getCheckBoxListSelectionModel ().setSelectionInterval (0,
                                                                     getListModel ().getSize () - 1);
        updateTristateCheckBox ();
    }

    public final void setSelection (java.util.List<T> prevoiusSelection) {
        final CheckBoxListSelectionModel checkBoxListSelectionModel = _list.getCheckBoxListSelectionModel ();

        final DefaultListModel model = (DefaultListModel) getListModel ();
        final int size = model.getSize ();
        for (int index = 0; index < size; index++) {
            if (prevoiusSelection.contains (model.get (index))) {
                checkBoxListSelectionModel.setSelectionInterval (index,
                                                                 index);
            }
        }
        updateTristateCheckBox ();
    }

    public final boolean isSelectAll () {
        final DefaultListModel model = (DefaultListModel) _field.getListModel ();
        if (model != null) {
            final CheckBoxListSelectionModel checkBoxListSelectionModel = _list.getCheckBoxListSelectionModel ();
            final int size = model.getSize ();
            for (int index = 0; index < size; index++) {
                if (!checkBoxListSelectionModel.isSelectedIndex (index)) {
                    return false;
                }
            }
        }
        return true;
    }
}
