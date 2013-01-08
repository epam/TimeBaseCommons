package deltix.util.jide;

import static deltix.qsrv.hf.tickdb.ui.administrator.res.CommonResourceBundle.RB;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Position.Bias;

import com.jidesoft.list.*;
import com.jidesoft.swing.*;

import deltix.util.swing.*;

public abstract class SelectorPanel<T> extends JPanel {

    protected CheckBoxList         list;
    protected QuickListFilterField field;
    protected TriStateCheckBox       tristateCheckBox;

    private boolean                adjust = false;

    public SelectorPanel () {
        super (new GridBagLayout ());
        init ();
    }

    protected void init () {

        this.list = new CheckBoxList () {

            @Override
            public int getNextMatch (final String prefix,
                                     final int startIndex,
                                     final Bias bias) {
                return -1;
            }

        };
        this.list.getCheckBoxListSelectionModel ().setSelectionMode (ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.field = new QuickListFilterField ();
        this.field.setHintText (RB.getString ("msg.symbolsFilterHint"));

        this.tristateCheckBox = new TriStateCheckBox (RB.getString ("btn.selectDeselectAll"),
                                                      TriStateCheckBox.NOT_SELECTED) {
            @Override
            public void nextState () {
                super.nextState ();
                updateTristateSelection ();
            }

        };

        this.field.setList (this.list);
        SearchableUtils.installSearchable (this.list);

        final CheckBoxListSelectionModel checkBoxListSelectionModel = this.list.getCheckBoxListSelectionModel ();

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

        c.insets = new Insets (4, 4, 4, 4);

        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        final StyledLabel header = new StyledLabel (getHeader ());
        header.addStyleRange (new StyleRange (Font.BOLD,
                                              Color.BLACK));
        c.gridy++;
        add (new TitledSeparator (header,
                                  TitledSeparator.TYPE_PARTIAL_ETCHED,
                                  SwingConstants.LEFT),
             c);

        c.gridy++;
        add (this.tristateCheckBox,
             c);

        c.gridy++;
        c.insets = new Insets (4,
                               4,
                               0,
                               4);
        add (this.field,
             c);

        c.gridy++;
        c.insets = new Insets (0,
                               4,
                               4,
                               4);
        final JideScrollPane sp = new JideScrollPane (this.list);
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add (sp,
             c);
    }

    @SuppressWarnings("unchecked")
    public void setCellRenderer (final ListCellRenderer cellRenderer) {
        this.list.setCellRenderer (cellRenderer);
    }

    protected abstract String getHeader ();

    protected abstract Collection<T> getDelegate ();

    public final void updateTristateCheckBox () {
        if (!this.adjust) {
            this.adjust = true;

            int selectedCount = 0;
            final int size = this.field.getListModel ().getSize ();
            for (int index = 0; index < size; index++) {
                if (this.list.getCheckBoxListSelectionModel ().isSelectedIndex (index))
                    selectedCount++;
            }

            if (selectedCount == 0) {
                this.tristateCheckBox.setState (TriStateCheckBox.NOT_SELECTED);
            } else if (selectedCount == size) {
                this.tristateCheckBox.setState (TriStateCheckBox.SELECTED);
            } else {
                this.tristateCheckBox.setState (TriStateCheckBox.DONT_CARE);
            }

            this.adjust = false;
        }
    }

    private void updateTristateSelection () {
        final CheckBoxListSelectionModel checkBoxListSelectionModel = this.list.getCheckBoxListSelectionModel ();

        if (!this.adjust) {
            this.adjust = true;
            if (this.tristateCheckBox.getState () == TriStateCheckBox.SELECTED) {
                checkBoxListSelectionModel.addSelectionInterval (0,
                                                                 this.field.getListModel ().getSize () - 1);
            } else if (this.tristateCheckBox.getState () == TriStateCheckBox.NOT_SELECTED) {
                checkBoxListSelectionModel.clearSelection ();
            }
            this.adjust = false;
        }

    }

    @SuppressWarnings("unchecked")
    protected void rebuildModel () {

        final java.util.List<T> prevoiusSelection = getSelection ();
        final DefaultListModel model = new DefaultListModel ();

        final Collection<T> delegate = getDelegate ();
        for (final T o : delegate) {
            model.addElement (o);
        }

        this.field.setListModel (model);
        this.list.setModel (this.field.getDisplayListModel ());

        final CheckBoxListSelectionModel checkBoxListSelectionModel = this.list.getCheckBoxListSelectionModel ();

        final int size = model.getSize ();
        for (int index = 0; index < size; index++) {
            if (prevoiusSelection.contains (model.get (index))) {
                checkBoxListSelectionModel.setSelectionInterval (index,
                                                                 index);
            }
        }

    }

    public final ListModel getListModel () {
        return this.field.getListModel ();
    }

    public final CheckBoxListSelectionModel getCheckBoxListSelectionModel () {
        return this.list.getCheckBoxListSelectionModel ();
    }

    @SuppressWarnings("unchecked")
    public final java.util.List<T> getSelection () {
        final ArrayList<T> result = new ArrayList<T> ();
        final DefaultListModel model = (DefaultListModel) this.field.getListModel ();
        if (model != null) {
            final CheckBoxListSelectionModel checkBoxListSelectionModel = this.list.getCheckBoxListSelectionModel ();

            final int size = model.getSize ();
            for (int index = 0; index < size; index++) {
                if (checkBoxListSelectionModel.isSelectedIndex (index)) {
                    result.add ((T) this.field.getDisplayListModel ().getElementAt (index));
                }
            }
        }

        return result;
    }

    public final void selectAll () {
        this.list.getCheckBoxListSelectionModel ().setSelectionInterval (0,
                                                                         getListModel ().getSize () - 1);
        updateTristateCheckBox ();
    }

    public final void setSelection (final java.util.List<T> prevoiusSelection) {
        final CheckBoxListSelectionModel checkBoxListSelectionModel = this.list.getCheckBoxListSelectionModel ();

        checkBoxListSelectionModel.clearSelection ();

        final DefaultListModel model = (DefaultListModel) getListModel ();
        final int size = model.getSize ();
        for (int index = 0; index < size; index++) {
            if (prevoiusSelection.contains (model.get (index))) {
                checkBoxListSelectionModel.addSelectionInterval (index,
                                                                 index);
            }
        }
        updateTristateCheckBox ();
    }

    public final boolean isSelectAll () {
        final DefaultListModel model = (DefaultListModel) this.field.getListModel ();
        if (model != null) {
            final CheckBoxListSelectionModel checkBoxListSelectionModel = this.list.getCheckBoxListSelectionModel ();
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
