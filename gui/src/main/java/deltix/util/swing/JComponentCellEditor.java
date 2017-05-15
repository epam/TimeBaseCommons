package deltix.util.swing;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 *  Courtesy of Zafir Anjum, this is a cell editor that shows
 *  JComponent cells correctly (by showing a <b>live</b> component).
 *  Some corrections have been made to make buttons work correctly.
 */
public class JComponentCellEditor implements TableCellEditor, TreeCellEditor, Serializable {	
    private class ClickGenerator extends MouseAdapter {
        public void mouseReleased (MouseEvent e) {
            Component       dispatchComponent = e.getComponent ();
            
			dispatchComponent.dispatchEvent (
                new MouseEvent ( 
                    dispatchComponent, 
                    MouseEvent.MOUSE_CLICKED,
                    e.getWhen (),
                    e.getModifiers (), 
                    e.getX (), 
                    e.getY (), 
                    e.getClickCount (),
                    e.isPopupTrigger () 
                )
            ); 
            
            mEditorComponent.removeMouseListener (this);
        }
    }
    
    private ClickGenerator          mClickGenerator = new ClickGenerator();
    
	protected EventListenerList     mListenerList = new EventListenerList ();    
	transient protected ChangeEvent mChangeEvent = null;	// Lazily created
	protected JComponent            mEditorComponent = null;
	protected JComponent            mContainer = null;		// Can be tree or table
		
	public Component                getComponent () {
		return (mEditorComponent);
	}
		
	public Object                   getCellEditorValue () {
		return (mEditorComponent);
	}
	
	public boolean                  isCellEditable (EventObject anEvent) {
		return (true);
	}
	
	public boolean                  shouldSelectCell (EventObject anEvent) {
		return (false);        
	}
	
	public boolean          stopCellEditing () {
		fireEditingStopped ();
		return (true);
	}
	
	public void             cancelCellEditing() {
		fireEditingCanceled();
	}
	
	public void             addCellEditorListener (CellEditorListener l) {
		mListenerList.add (CellEditorListener.class, l);
	}
	
	public void             removeCellEditorListener (CellEditorListener l) {
		mListenerList.remove (CellEditorListener.class, l);
	}
	
	protected void          fireEditingStopped () {
		Object[] listeners = mListenerList.getListenerList ();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CellEditorListener.class) {
				// Lazily create the event:
				if (mChangeEvent == null)
					mChangeEvent = new ChangeEvent(this);
				((CellEditorListener)listeners[i+1]).editingStopped(mChangeEvent);
			}	       
		}
	}
	
	protected void          fireEditingCanceled () {
		// Guaranteed to return a non-null array
		Object[] listeners = mListenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CellEditorListener.class) {
				// Lazily create the event:
				if (mChangeEvent == null)
					mChangeEvent = new ChangeEvent(this);
				((CellEditorListener)listeners[i+1]).editingCanceled(mChangeEvent);
			}	       
		}
	}
	
	private void            init (
        JComponent              container, 
        Object                  value
    ) 
    {
		mEditorComponent = (JComponent) value;
		mContainer = container;
        mEditorComponent.addMouseListener (mClickGenerator);
	}
	
    // implements javax.swing.tree.TreeCellEditor
	public Component        getTreeCellEditorComponent (
        JTree                   tree, 
        Object                  value,
		boolean                 isSelected, 
        boolean                 expanded,
        boolean                 leaf, 
        int                     row
    ) 
    {
		init (tree, value);
		return (mEditorComponent);
	}
	
	// implements javax.swing.table.TableCellEditor
	public Component        getTableCellEditorComponent (
        JTable                  table, 
        Object                  value,
		boolean                 isSelected, 
        int                     row,
        int                     column
    ) 
    {		
		init (table, value);
		return (mEditorComponent);
	}	
}


