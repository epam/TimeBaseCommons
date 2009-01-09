/**
 * 
 */
package deltix.util.swing.table;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class RadioCellEditor extends AbstractCellEditor implements TableCellEditor {
	private JRadioButton mRadioButton;

	public RadioCellEditor ( ) {
		super ( );
		mRadioButton = new JRadioButton ( );
		mRadioButton.addActionListener ( new ActionListener ( ) {
			public void actionPerformed ( ActionEvent event ) {
				fireEditingStopped ( );
			}
		} );
	}

	public Component getTableCellEditorComponent ( JTable table,
	                                               Object value,
	                                               boolean isSelected,
	                                               int row,
	                                               int column ) {

		mRadioButton.setHorizontalAlignment ( SwingUtilities.CENTER );
		Boolean lValueAsBoolean = (Boolean) value;
		mRadioButton.setSelected ( lValueAsBoolean.booleanValue ( ) );
		mRadioButton.setOpaque ( false );
		return mRadioButton;
	}

	public Object getCellEditorValue ( ) {
		return new Boolean ( mRadioButton.isSelected ( ) );
	}
}