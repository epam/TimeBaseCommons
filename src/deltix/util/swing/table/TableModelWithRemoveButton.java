package deltix.util.swing.table;

import deltix.util.swing.JTableEx;
import deltix.util.swing.SwingUtil;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.event.TableModelListener;
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
    private JTableEx                    table;
    
    protected TableModelWithRemoveButton (int killerColumn) {
        this.killerColumn = killerColumn;
    }
    
    /**
     *  At a minimum the killer column must be editable so that 
     *  the buttons will function. Override to make more columns editable.
     */
    @Override
    public boolean              isCellEditable (int rowIndex, int columnIndex) {
        return (columnIndex == killerColumn);
    }

    public final int            getRowCount () {
        return (itemList.size ());
    }

    protected abstract Object   getValueAt (int rowIndex, T data, int columnIndex);
    
    protected void              rowRemoved (int rowIndex, T data) {        
    }
    
    public final Object         getValueAt (int rowIndex, int columnIndex) {
        if (columnIndex == killerColumn) 
            return (killers.get (rowIndex));        
        
        return (getValueAt (rowIndex, itemList.get (rowIndex), columnIndex));
    } 

    protected JButton           createKillerButton () {
        return (new JButton (SwingUtil.X_ICON));
    }

    @Override
    public void                 addTableModelListener (TableModelListener l) {
        super.addTableModelListener (l);
        
        if (l instanceof JTableEx) {
            if (table != null)
                throw new IllegalStateException (
                    "Registered with two tables at once: " + l + " and " + table
                );
            
            table = (JTableEx) l;
        }            
    }

    @Override
    public void removeTableModelListener (TableModelListener l) {
        super.removeTableModelListener (l);
        
        if (table == l)
            table = null;
    }        
    
    private JButton             setUpKiller () {
        final JButton     killButton = createKillerButton ();

        killButton.addActionListener (
            new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    if (table == null)
                        throw new IllegalStateException (
                            "This model only works with a JTableEx"
                        );
                    
                    // Turn editing off so button can be REMOVED.
                    table.removeEditor (); 
                    
                    int  idx = killers.indexOf (killButton);
                    
                    if (idx < 0)
                        throw new IllegalStateException (
                            "Failed to find killer button " + 
                            System.identityHashCode (killButton) +
                            " among " + killers.size ()
                        );
                    
                    remove (idx);                        
                }
            }
        );

        return (killButton);
    }

    public final void           remove (int row) {
        T       object = itemList.remove (row);
        JButton killer = killers.remove (row);
        
        fireTableRowsDeleted (row, row);        
        rowRemoved (row, object);
    }

    public void                 add (T item) {
        int row = itemList.size ();
        killers.add (setUpKiller ());
        itemList.add (item);  
        fireTableRowsInserted (row, row);
    }

    public void                 load (T [] items) {
        itemList.clear ();
        killers.clear ();

        if (items != null) {
            for (int ii = 0; ii < items.length; ii++) {
                itemList.add (items [ii]);                
                killers.add (setUpKiller ());
            }
        }
        
        fireTableDataChanged ();
    }

    /**
     *  toArray() semantics
     */
    public T []                 getItems (T [] out) {
        return (itemList.toArray (out));
    }        
    
    public void                 removeAll () {
        int         n = itemList.size ();
        
        for (int ii = 0; ii < n; ii++) 
            rowRemoved (ii, itemList.get (ii));        
        
        killers.clear ();
        itemList.clear ();
    }         
};
