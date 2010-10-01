package deltix.util.jgoodies;

import java.beans.*;

import com.jgoodies.binding.*;
import com.jgoodies.binding.value.*;
import com.jgoodies.validation.*;
import com.jgoodies.validation.util.*;

public abstract class ValidatableParametersModel<B> extends PresentationModel<B> {

    private final ValidationResultModel _validationResultModel = new DefaultValidationResultModel ();

    public ValidatableParametersModel (final B bean,
                                       final ValueModel triggerChannel) {
        super (bean,
               triggerChannel);
    }

    public ValidatableParametersModel (final B bean) {
        super (bean);
    }

    public ValidatableParametersModel (final ValueModel beanChannel,
                                       final ValueModel triggerChannel) {
        super (beanChannel,
               triggerChannel);
        initEventHandling ();
        updateValidationResult ();
    }

    public ValidatableParametersModel (final ValueModel beanChannel) {
        super (beanChannel);
    }

    // Exposing Models ********************************************************

    public ValidationResultModel getValidationResultModel () {
        return _validationResultModel;
    }

    // Initialization *********************************************************

    /**
     * Listens to changes in all properties of the current Order and to Order
     * changes.
     */
    private void initEventHandling () {
        final PropertyChangeListener handler = new ValidationUpdateHandler ();
        addBeanPropertyChangeListener (handler);
        getBeanChannel ().addValueChangeListener (handler);
    }

    // Event Handling *********************************************************

    private void updateValidationResult () {
        final ValidationResult result = createValidator ().validate (getBean ());
        _validationResultModel.setResult (result);
    }

    /**
     * Validates the order using an NewStreamParametersValidator and updates the
     * validation result.
     */
    private final class ValidationUpdateHandler implements PropertyChangeListener {

        @Override
        public void propertyChange (final PropertyChangeEvent evt) {
            updateValidationResult ();
        }

    }

    protected abstract Validator<B> createValidator ();
}
