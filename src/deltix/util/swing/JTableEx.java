package deltix.util.swing;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *  A version of javax.swing.JTable that selects cell renderers and editors based
 *  on the class of cell values, and not only table column class. Column class
 *  setting overrides cell-based selection. Also sets renderers and editors for the
 *  JComponent class so that the component is displayed live inside the table.
 */
public class JTableEx extends JTable {
    /**
     *  See JTable.
     */
    public JTableEx (TableModel m) {
        this (m, null, null);
    }
    
    /**
     *  See JTable.
     */
    public JTableEx () {
        this (null, null, null);
    }
    
    /**
     *  See JTable.
     */
    public JTableEx (TableModel dm, TableColumnModel cm) {
        this (dm, cm, null);
    }

    /**
     *  See JTable.
     */
    public JTableEx (TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super (dm, cm, sm);
        
        setDefaultRenderer (JComponent.class, JComponentCellRenderer.INSTANCE);
		setDefaultEditor (JComponent.class, new JComponentCellEditor ());  
    }
    
    /**
     *  If JTable's logic fails to find a specific renderer, get the 
     *  default renderer for the <i>class of the cell's value object</i>.
     */
    public TableCellRenderer getCellRenderer (int row, int column) {
        TableColumn         tableColumn = getColumnModel ().getColumn (column);
        TableCellRenderer   renderer = tableColumn.getCellRenderer ();
        
        if (renderer == null) {
            Class           c = getColumnClass (column);
            
            /**
             *  Get a more specific class from the value.
             */
            if (c == Object.class) {
                Object      o = getValueAt (row, column);
                
                if (o != null)
                    c = o.getClass ();
            }
            
            renderer = getDefaultRenderer (c);
        }
        
        return (renderer);
    }

    /**
     *  If JTable's logic fails to find a specific editor, get the 
     *  default editor for the <i>class of the cell's value object</i>.
     */
    public TableCellEditor getCellEditor(int row, int column) {
        TableColumn         tableColumn = getColumnModel ().getColumn (column);
        TableCellEditor     editor = tableColumn.getCellEditor ();
        
        if (editor == null) {
            Class           c = getColumnClass (column);
            
            /**
             *  Get a more specific class from the value.
             */
            if (c == Object.class) {
                Object      o = getValueAt (row, column);
                
                if (o != null)
                    c = o.getClass ();
            }
            
            editor = getDefaultEditor (c);
        }
        
        return (editor);
    }	
}


