package deltix.util.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *  Courtesy of Zafir Anjum, this is a cell renderer that shows
 *  JComponent cells correctly (by showing a <b>live</b> component).
 */
public class JComponentCellRenderer implements TableCellRenderer {
    public static final JComponentCellRenderer  INSTANCE = 
        new JComponentCellRenderer ();
    
    private JComponentCellRenderer () {
    }
    
    public Component    getTableCellRendererComponent (
        JTable              table, 
        Object              value,
		boolean             isSelected,
        boolean             hasFocus, 
        int                 row, 
        int                 column
    ) 
    {            
        JComponent  comp = (JComponent)value;
        if (comp != null)
            comp.setEnabled(table.isEnabled());
        
        return (comp);
    }
}
