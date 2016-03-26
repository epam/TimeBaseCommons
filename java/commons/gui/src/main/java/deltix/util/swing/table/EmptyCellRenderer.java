/**
 * 
 */
package deltix.util.swing.table;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class EmptyCellRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent ( JTable table,
	                                                 Object value,
	                                                 boolean isSelected,
	                                                 boolean hasFocus,
	                                                 int row,
	                                                 int column ) {

		JLabel label = (JLabel) super.getTableCellRendererComponent ( table,
		                                                              value,
		                                                              isSelected,
		                                                              hasFocus,
		                                                              row,
		                                                              column );

		label.setText ( "" );
		return label;
	}
}