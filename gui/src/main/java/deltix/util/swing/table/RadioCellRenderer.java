/**
 * 
 */
package deltix.util.swing.table;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class RadioCellRenderer extends JRadioButton implements TableCellRenderer {
	// We need a place to store the color the JLabel should be returned
	// to after its foreground and background colors have been set
	// to the selection background color.
	// These ivars will be made protected when their names are finalized.
	private Color unselectedForeground;
	private Color unselectedBackground;

	public Component getTableCellRendererComponent ( JTable table,
	                                                 Object value,
	                                                 boolean isSelected,
	                                                 boolean hasFocus,
	                                                 int row,
	                                                 int column ) {

		Color fg = null;
		Color bg = null;

		JTable.DropLocation dropLocation = table.getDropLocation ( );
		if (dropLocation != null &&
		    !dropLocation.isInsertRow ( ) &&
		    !dropLocation.isInsertColumn ( ) &&
		    dropLocation.getRow ( ) == row &&
		    dropLocation.getColumn ( ) == column) {

			fg = UIManager.getColor ("Table.dropCellForeground" );
			bg = UIManager.getColor ( "Table.dropCellBackground" );

			isSelected = true;
		}

		if (isSelected) {
			this.setForeground ( fg == null ? table.getSelectionForeground ( ) : fg );
			this.setBackground ( bg == null ? table.getSelectionBackground ( ) : bg );
		} else {
			Color background = unselectedBackground != null ? unselectedBackground : table.getBackground ( );
			if (background == null || background instanceof javax.swing.plaf.UIResource) {
				Color alternateColor = UIManager.getColor ("Table.alternateRowColor" );
				if (alternateColor != null && row % 2 == 0)
					background = alternateColor;
			}
			this.setForeground ( unselectedForeground != null ? unselectedForeground : table.getForeground ( ) );
			this.setBackground ( background );
		}

		this.setSelected(value != null && (Boolean) value);
		this.setHorizontalAlignment ( SwingConstants.CENTER );

		return this;
	}
}