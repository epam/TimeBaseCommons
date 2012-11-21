package deltix.util.swing;

import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  Extension of StandardAction, which delegates the action to a void no-arguments
 *  method whose name coincides with the name of the action. 
 *  Please see
 *  {@link deltix.util.swing.StandardAction} documentation for a detailed description
 *  of how action properties are retrieved, based on the package of the object and
 *  the name key. The supplied object must be of a <tt>public</tt> class.
 *  <p>
 *  Usage:
 *<pre>
 *private Action          MY_ACTION =
 *    new SimpleAction (this, "methodName");
 *</pre>
 */
public final class SimpleAction extends StandardAction {
    private static final Class<?> []   NO_ARGS_SIG = { };

    private Object      	mObject;
    private Method      	mMethod;
    private final String	mNameKey;

    /**
     *  Constructs a StandardAction which will call a method of the
     *  supplied object, whose name is identical to the name key.
     *
     *  @exception RuntimeException     If such a method was not found.
     */
    public SimpleAction (Object delegate, String nameKey) {
        super (delegate.getClass (), nameKey);
        mNameKey = nameKey;
        mObject = delegate;

        Class<?>       c = mObject.getClass ();

        while (c != Object.class) {
            try {
                mMethod = c.getDeclaredMethod (nameKey, NO_ARGS_SIG);
                mMethod.setAccessible (true);
                break;
            } catch (NoSuchMethodException x) {
            }

            c = c.getSuperclass ();
        }

        if (mMethod == null)
            throw new RuntimeException (
                "Did not find public void " + nameKey + " () in class " +
                mObject.getClass () + " or any of its ancestors."
            );        
    }

    private void setCursor ( Cursor cursor ) {
		if (mObject instanceof Component)
			((Component) mObject).setCursor ( cursor );
	}
    
    public void         actionPerformed (ActionEvent e) {
    	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            mMethod.invoke (mObject, (Object []) null);
        } catch (IllegalAccessException iax) {
            throw new RuntimeException (
                "Failed to invoke " + mMethod + ": " + iax.toString ()
            );
        } catch (InvocationTargetException itx) {
            Throwable   cause = itx.getTargetException ();
            if (cause instanceof RuntimeException)
                throw ((RuntimeException) cause);
            else if (cause instanceof Error)
                throw ((Error) cause);
            else
                throw new RuntimeException (cause);
        }
        finally{
        	setCursor(Cursor.getDefaultCursor());
        }
    }

	public final String getNameKey ( ) {
    	return mNameKey;
    }
    
}
