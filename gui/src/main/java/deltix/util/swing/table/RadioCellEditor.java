package deltix.util.swing.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadioCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JRadioButton _radioButton;

    public RadioCellEditor () {
        super ();
        _radioButton = new JRadioButton ();
        _radioButton.addActionListener (new ActionListener () {
            public void actionPerformed (ActionEvent event) {
                fireEditingStopped ();
            }
        });
    }

    public Component getTableCellEditorComponent (JTable table,
                                                  Object value,
                                                  boolean isSelected,
                                                  int row,
                                                  int column) {

        _radioButton.setHorizontalAlignment (SwingUtilities.CENTER);
        Boolean lValueAsBoolean = (Boolean) value;
        _radioButton.setSelected (lValueAsBoolean);
        _radioButton.setOpaque (false);
        return _radioButton;
    }

    public Object getCellEditorValue () {
        return _radioButton.isSelected();
    }
}