package deltix.util.swing;

import javax.swing.*;
import java.util.*;
import java.util.logging.Level;

import deltix.util.Util;

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
    public StandardAction (Class forClass, String nameKey, String imageType) {
        String          className = forClass.getName ();
        int             dot = className.lastIndexOf ('.');
        
        if (dot < 0)
            dot = 0;
        
        String          packName = className.substring (0, dot);
        String          packPath = packName.replace ('.', '/');        
        String          packFull = packPath + ".actions";
        ResourceBundle  rb = ResourceBundle.getBundle (packFull);
        
        try {
            putValue (NAME, rb.getString (nameKey));
        } catch (MissingResourceException mrx) {
            //  Ignore the name
        }
        
        try {
            putValue (SHORT_DESCRIPTION, rb.getString (nameKey + ".tt"));
        } catch (MissingResourceException mrx) {
            Util.LOGGER.log (
                Level.WARNING, 
                "Missing tooltip for " + nameKey + " in " + packFull,
                mrx
            );            
        }
        
        String          imageResourcePath = null;
        
        try {
            imageResourcePath = rb.getString (nameKey + ".img");
        } catch (MissingResourceException x) {
            imageResourcePath = packPath + "/" + nameKey + "." + imageType;
        }
        
        putValue (
            SMALL_ICON,
            SwingUtil.loadIcon (imageResourcePath)
        );

        // kbd accelerator (hot key)
        try {
            putValue (ACCELERATOR_KEY, KeyStroke.getKeyStroke(rb.getString (nameKey + ".key")));
        } catch (MissingResourceException mrx) {
            //  Ignore the missing hotkey
        }

        // mnemonic
        try {
            putValue (MNEMONIC_KEY,  new Integer(rb.getString (nameKey + ".mnemonic").charAt(0)));
        } catch (MissingResourceException mrx) {
            //  Ignore the missing mnemonic
        }

    }
    
    /**
     *  Same as above, hardcodes image type to <tt>gif</tt>.
     */
    public StandardAction (Class forClass, String nameKey) {
        this (forClass, nameKey, "gif");
    }
}
