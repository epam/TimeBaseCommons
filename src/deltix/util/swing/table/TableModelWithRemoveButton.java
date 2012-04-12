package deltix.util.swing.table;

import deltix.util.swing.SwingUtil;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

/**
 *
 */
public abstract class TableModelWithRemoveButton <T> extends AbstractTableModel {        
    private final ArrayList <JButton>   killers = 
        new ArrayList <JButton> ();

    private final ArrayList <T>         itemList = 
        new ArrayList <T> ();

    private final int                   killerColumn;
    
    protected TableModelWithRemoveButton (int killerColumn) {
        this.killerColumn = killerColumn;
    }
    
    @Override
    public boolean      isCellEditable (int rowIndex, int columnIndex) {
        return (columnIndex == killerColumn);
    }

    public int          getRowCount () {
        return (itemList.size ());
    }

    protected abstract Object getValueAt (int rowIndex, T data, int columnIndex);
    
    public final Object       getValueAt (int rowIndex, int columnIndex) {
        if (columnIndex == killerColumn)
            return (killers.get (rowIndex));
        
        return (getValueAt (rowIndex, itemList.get (rowIndex), columnIndex));
    } 

    protected JButton   createKillerButton () {
        return (new JButton (SwingUtil.X_ICON));
    }
    
    private JButton     setUpKiller () {
        final JButton     killButton = createKillerButton ();

        killButton.addActionListener (
            new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    remove (killers.indexOf (killButton));                        
                }
            }
        );

        return (killButton);
    }

    public void         remove (int row) {
        itemList.remove (row);
        killers.remove (row);
        fireTableRowsDeleted (row, row);
    }

    public void         add (T item) {
        int row = itemList.size ();
        killers.add (setUpKiller ());
        itemList.add (item);  
        fireTableRowsInserted (row, row);
    }

    public void         load (T [] items) {
        itemList.clear ();
        killers.clear ();

        for (int ii = 0; ii < items.length; ii++) {
            itemList.add (items [ii]);                
            killers.add (setUpKiller ());
        }

        fireTableDataChanged ();
    }
};
