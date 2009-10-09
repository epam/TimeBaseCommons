/**
 * 
 */
package deltix.util.swing.table;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class RadioCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JRadioButton _radioButton;

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
        _radioButton.setSelected (lValueAsBoolean.booleanValue ());
        _radioButton.setOpaque (false);
        return _radioButton;
    }

    public Object getCellEditorValue () {
        return new Boolean (_radioButton.isSelected ());
    }
}