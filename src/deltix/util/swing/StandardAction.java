package deltix.util.swing;

import deltix.util.io.UncheckedIOException;
import deltix.util.lang.SafeResourceBundle;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;

import deltix.util.lang.Util;

/**
 *  Base class for Action implementations. Action properties
 *  name, tooltip and icon are retrieved, based on a single key.
 *  <p>
 *  Usage:
 *<pre>
 *private Action          MY_ACTION =
 *    new StandardAction (MyClass.class, "myKey") {
 *        public void actionPerformed (ActionEvent e) { doSomething (); }
 *    };
 *</pre>
 *
 */
public abstract class StandardAction extends AbstractAction {

    public static final String IMAGE_FOLDER = "images";

    /**
     *  Creates a standard action as follows:
     *
     *  <ol>
     *      <li>Figures out the package name of the specified class.
     *      <li>The action name is retrieved from the resource bundle
     *          called <tt>actions</tt>, under the same package, under the key of
     *          <tt><i>nameKey</i></tt>.
     *      <li>The tooltip text is retrieved from the resource bundle
     *          called <tt>actions</tt>, under the same package, under the key of
     *          <tt><i>nameKey</i>.tt</tt>.
     *      <li>Loads the icon image from this package's resource path,
     *          where the icon name is same as the <i>nameKey</i> argument;
     *          extension <tt>imageType</tt>, unless the <tt>actions</tt>
     *          resource bundle contains a key called <tt><i>nameKey</i>.tt</tt>,
     *          in which case the value of that key is used as the icon resource path.
     *  </ol>
     *
     *  @param nameKey  Used to look up the action properties.
     */
    public StandardAction (Class forClass, String nameKey) {
        key = nameKey;
        setUpAction (this, forClass, nameKey);
    }

    public static final String []   IMAGE_EXTENSIONS = { "gif", "jpg", "png" };

    private final String    key;

    public static void      setUpAction (Action action, Class <?> forClass, String nameKey) {
        String          className = forClass.getName();
        int             dot = className.lastIndexOf ('.');

        if (dot < 0)
            dot = 0;

        String          packName = className.substring (0, dot);
        String          packPath = packName.replace ('.', '/');

        setUpAction (action, packPath, nameKey);
    }

    public static void      setUpAction (Action action, String resPath, String nameKey) {
        ResourceBundle  rb = SafeResourceBundle.getBundle (resPath + "/actions");

        try {
            action.putValue (NAME, rb.getString (nameKey));
        } catch (MissingResourceException mrx) {
            //  Ignore the name
        }

        try {
            action.putValue (SHORT_DESCRIPTION, rb.getString (nameKey + ".tt"));
        } catch (MissingResourceException mrx) {
            Util.LOGGER.log (
                Level.WARNING,
                "Missing tooltip for action " + nameKey,
                mrx
            );
        }

        Icon            icon = null;

        try {
            //WARNING 
            //rb could be SafeResourceBundle, in which case it doesn't throw 
            //MissingResourceException for missing keys
            String      imageResourcePath = rb.getString (nameKey + ".img");
            icon = SwingUtil.loadIcon (imageResourcePath);
        } catch (UncheckedIOException iox) {
                    // Ignore
        } catch (MissingResourceException x) {
            boolean     ok = false;

            for (String ext : IMAGE_EXTENSIONS) {
                String imageResourcePath          = String.format("%s/%s.%s", resPath, nameKey, ext                 );
                String imageAlternateResourcePath = String.format("%s/%s/%s.%s", resPath, IMAGE_FOLDER, nameKey, ext);

                try {
                    icon = SwingUtil.loadIcon (imageResourcePath, imageAlternateResourcePath);
                    break;
                } catch (UncheckedIOException iox) {
                    // Ignore
                }
            }
        }

        if (icon != null)
            action.putValue (SMALL_ICON, icon);
            
        // kbd accelerator (hot key)
        try {
            action.putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke(rb.getString (nameKey + ".key")));
        } catch (MissingResourceException mrx) {
            //  Ignore the missing hotkey
        }

        // mnemonic
        try {
            action.putValue (MNEMONIC_KEY, (int) rb.getString(nameKey + ".mnemonic").charAt(0));
        } catch (MissingResourceException mrx) {
            //  Ignore the missing mnemonic
        }
    }

    @Override
    public String toString () {
        return getClass ().getSimpleName () + ":" + key;
    }
}
