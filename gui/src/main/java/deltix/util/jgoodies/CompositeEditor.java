package deltix.util.jgoodies;

import javax.swing.*;
import javax.swing.event.*;

import com.jgoodies.binding.value.*;

public abstract class CompositeEditor extends AbstractUIBean {

    /** A list of event listeners for this component. */
    protected EventListenerList listenerList = new EventListenerList ();

    public CompositeEditor () {
        super ();
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
    public void addChangeListener (final ChangeListener l) {
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
    public void removeChangeListener (final ChangeListener l) {
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
        return listenerList.getListeners (
                ChangeListener.class);
    }

    /**
     * Run each ChangeListeners stateChanged() method.
     * 
     * @see CompositeEditor#setEditorValue(Object)
     * @see EventListenerList
     */
    protected void fireStateChanged () {
        final Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent (this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged (changeEvent);
            }
        }
    }

    public static void bind (final CompositeEditor editor,
                             final ValueModel valueModel) {
        final CompositeEditorToValueModelConnector connector = new CompositeEditorToValueModelConnector (editor,
                                                                                                         valueModel);
        connector.updateComponent ();
    }
}
