package deltix.util.jgoodies;

import java.awt.*;
import java.beans.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

import com.jgoodies.validation.*;
import com.jgoodies.validation.view.*;
import com.jgoodies.validation.view.ValidationComponentUtils.*;

public class DeltixValidationComponentUtils {

    // Colors *****************************************************************

    public static final Color                                                 ERROR_BACKGROUND      = Color.RED;

    public static final Color                                                 WARNING_BACKGROUND    = new Color (255,
                                                                                                                 235,
                                                                                                                 205);

    // Client Property Keys **************************************************

    public static final String                                                MESSAGE_KEYS          = "validation.messageKeys";

    public static final String                                                STORED_BACKGROUND_KEY = "validation.storedBackground";

    // A Map that holds reusable prototype text components ********************

    private static final Map<Class<? extends JTextComponent>, JTextComponent> PROTOTYPE_COMPONENTS  = new HashMap<Class<? extends JTextComponent>, JTextComponent> ();

    // Predefined Component Tree Updates **************************************

    public static void updateComponentTreeSeverityBackground (Container container,
                                                              ValidationResult result) {
        ValidationComponentUtils.visitComponentTree (container,
                                                     result.keyMap (),
                                                     new SeverityBackgroundVisitor ());
    }

    // Visiting Text Components in a Component Tree ***************************

    public static boolean visitComponentTree (final Container container,
                                              final deltix.util.collections.Visitor<JComponent> visitor) {
        final int componentCount = container.getComponentCount ();
        for (int i = 0; i < componentCount; i++) {
            final Component child = container.getComponent (i);
            if (child instanceof JTextComponent) {
                final JComponent component = (JComponent) child;
                if (!visitor.visit (component))
                    return false;
            } else if (child instanceof Container) {
                if (!visitComponentTree ((Container) child,
                                         visitor))
                    return false;
            }
        }

        return true;
    }

    // Helper Code ************************************************************

    public static void setErrorBackground (JTextComponent comp) {
        comp.setBackground (ERROR_BACKGROUND);
    }

    public static void setWarningBackground (JTextComponent comp) {
        comp.setBackground (WARNING_BACKGROUND);
    }

    private static void ensureCustomBackgroundStored (JTextComponent comp) {
        if (getStoredBackground (comp) != null) {
            return;
        }
        Color background = comp.getBackground ();
        if ((background == null)
            || (background instanceof UIResource)
            || (background == WARNING_BACKGROUND)
            || (background == ERROR_BACKGROUND)) {
            return;
        }
        comp.putClientProperty (STORED_BACKGROUND_KEY,
                                background);
    }

    private static Color getStoredBackground (JTextComponent comp) {
        return (Color) comp.getClientProperty (STORED_BACKGROUND_KEY);
    }

    private static void restoreBackground (JTextComponent comp) {
        Color storedBackground = getStoredBackground (comp);
        comp.setBackground (storedBackground == null
                                                    ? getDefaultBackground (comp)
                                                    : storedBackground);
    }

    private static Color getDefaultBackground (JTextComponent component) {
        JTextComponent prototype = getPrototypeFor (component.getClass ());
        prototype.setEnabled (component.isEnabled ());
        prototype.setEditable (component.isEditable ());
        return prototype.getBackground ();
    }

    private static JTextComponent getPrototypeFor (Class<? extends JTextComponent> prototypeClass) {
        ensureLookAndFeelChangeHandlerRegistered ();
        JTextComponent prototype = PROTOTYPE_COMPONENTS.get (prototypeClass);
        if (prototype == null) {
            try {
                prototype = prototypeClass.newInstance ();
            } catch (Exception e) {
                prototype = new JTextField ();
            }
            PROTOTYPE_COMPONENTS.put (prototypeClass,
                                      prototype);
        }
        return prototype;
    }

    public static Object[] getMessageKeys (final JComponent comp) {
        return (Object[]) comp.getClientProperty (MESSAGE_KEYS);
    }

    // Handling L&f Changes ***************************************************

    /**
     * Describes whether the <code>LookAndFeelChangeHandler</code> has been
     * registered with the <code>UIManager</code> or not. It is registered
     * lazily when the first prototype component is requested in
     * <code>#getPrototypeFor(Class)</code>.
     */
    private static boolean lafChangeHandlerRegistered = false;

    private static synchronized void ensureLookAndFeelChangeHandlerRegistered () {
        if (!lafChangeHandlerRegistered) {
            UIManager.addPropertyChangeListener (new LookAndFeelChangeHandler ());
            lafChangeHandlerRegistered = true;
        }
    }

    /**
     * Clears the cached prototype components when the L&amp; changes.
     */
    private static final class LookAndFeelChangeHandler implements PropertyChangeListener {

        /**
         * Clears the cached prototype components, if the UIManager has fired
         * any property change event. Since we need to handle look&amp;feel
         * changes only, we check the event's property name to be "lookAndFeel"
         * or {@code null}. The check for null is necessary to handle the
         * special event where property name, old and new value are all
         * {@code null} to indicate that multiple properties have changed.
         * 
         * @param evt
         *            describes the property change
         */
        public void propertyChange (PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName ();
            if ((propertyName == null) || propertyName.equals ("lookAndFeel")) {
                PROTOTYPE_COMPONENTS.clear ();
            }
        }
    }

    // Visitor Definition and Predefined Visitor Implementations **************
    public static final class SeverityBackgroundVisitor implements Visitor {

        public void visit (JComponent component,
                           Map<Object, ValidationResult> keyMap) {
            Object messageKeys = getMessageKeys (component);
            if (messageKeys == null) {
                return;
            }
            JTextComponent textChild = (JTextComponent) component;
            ensureCustomBackgroundStored (textChild);
            ValidationResult result = ValidationComponentUtils.getAssociatedResult (component,
                                                                                    keyMap);
            if ((result == null) || result.isEmpty ()) {
                restoreBackground (textChild);
            } else if (result.hasErrors ()) {
                setErrorBackground (textChild);
            } else if (result.hasWarnings ()) {
                setWarningBackground (textChild);
            }
        }
    }
}
