package deltix.util.jgoodies;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

public abstract class CompositeEditor extends JPanel {

    public CompositeEditor () {
        super ();
    }

    public CompositeEditor (boolean isDoubleBuffered) {
        super (isDoubleBuffered);
    }

    public CompositeEditor (LayoutManager layout,
                            boolean isDoubleBuffered) {
        super (layout,
               isDoubleBuffered);
    }

    public CompositeEditor (LayoutManager layout) {
        super (layout);
    }

    public abstract Object getEditorValue ();

    public abstract void setEditorValue (final Object value);

    /**
     * Only one ChangeEvent is needed per model instance since the event's only
     * (read-only) state is the source property. The source of events generated
     * here is always "this".
     */
    private transient ChangeEvent changeEvent = null;

    /**
     * Adds a ChangeListener to the model's listener list. The ChangeListeners
     * must be notified when the models value changes.
     * 
     * @param l
     *            the ChangeListener to add
     * @see #removeChangeListener
     * @see SpinnerModel#addChangeListener
     */
    public void addChangeListener (ChangeListener l) {
        listenerList.add (ChangeListener.class,
                          l);
    }

    /**
     * Removes a ChangeListener from the model's listener list.
     * 
     * @param l
     *            the ChangeListener to remove
     * @see #addChangeListener
     * @see SpinnerModel#removeChangeListener
     */
    public void removeChangeListener (ChangeListener l) {
        listenerList.remove (ChangeListener.class,
                             l);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code>s added to this
     * AbstractSpinnerModel with addChangeListener().
     * 
     * @return all of the <code>ChangeListener</code>s added or an empty array
     *         if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners () {
        return (ChangeListener[]) listenerList.getListeners (
                ChangeListener.class);
    }

    /**
     * Run each ChangeListeners stateChanged() method.
     * 
     * @see #setValue
     * @see EventListenerList
     */
    protected void fireStateChanged () {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent (this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged (changeEvent);
            }
        }
    }
}
