package deltix.util.jgoodies;

import java.beans.*;

import javax.swing.event.*;

import com.jgoodies.binding.value.*;

public class CompositeEditorToValueModelConnector {

    private final CompositeEditor editor;
    private final ValueModel      valueModel;

    /**
     * Implements PropertyChangeListener and ChangeListener and is used to
     * update both the SpinnerModel and the ValueModel.
     */
    private final UpdateHandler   updateHandler;

    public CompositeEditorToValueModelConnector (final CompositeEditor component,
                                                 final ValueModel valueModel) {
        super ();
        this.editor = component;
        this.valueModel = valueModel;

        this.updateHandler = new UpdateHandler ();
        component.addChangeListener (updateHandler);
        valueModel.addValueChangeListener (updateHandler);
    }

    public void updateComponent () {
        final Object value = valueModel.getValue ();
        setComponentValueSilently (value);
    }

    private void setComponentValueSilently (final Object newValue) {
        editor.removeChangeListener (updateHandler);
        editor.setEditorValue (newValue);
        editor.addChangeListener (updateHandler);
    }

    public void updateValueModel () {
        setValueModelValueSilently (editor.getEditorValue ());
    }

    private void setValueModelValueSilently (final Object newValue) {
        valueModel.removeValueChangeListener (updateHandler);
        valueModel.setValue (newValue);
        valueModel.addValueChangeListener (updateHandler);
    }

    // Event Handling Class ***************************************************

    /**
     * Registered with both the SpinnerModel and the ValueModel. Used to update
     * the spinner if the value changes, and vice versa.
     */
    private final class UpdateHandler implements PropertyChangeListener, ChangeListener {

        /**
         * The valueModel's value has changed; update the spinner model.
         * 
         * @param evt
         *            the event to handle
         */
        @Override
        public void propertyChange (final PropertyChangeEvent evt) {
            updateComponent ();
        }

        /**
         * The spinner value has changed; update the valueModel and notify all
         * listeners about the state change.
         * 
         * @param evt
         *            the change event
         */
        @Override
        public void stateChanged (final ChangeEvent evt) {
            updateValueModel ();
        }

    }
}
