package deltix.util.lang;

import deltix.util.collections.EmptyEnumeration;
import java.util.*;

/**
 *
 */
public class SafeResourceBundle {
    public static ResourceBundle    getBundle (String baseName) {
        try {
            return (ResourceBundle.getBundle (baseName));
        } catch (MissingResourceException x) {
            return (
                new ResourceBundle () {

                    @Override
                    public Enumeration <String> getKeys () {
                        return (new EmptyEnumeration <String> ());
                    }

                    @Override
                    protected Object handleGetObject (String key) {
                        return (null);
                    }                
                }
            );
        }
    }

    public static String            getString (ResourceBundle rb, String key) {
        if (rb == null)
            return (key);
        else
            try {
                return (rb.getString (key));
            } catch (MissingResourceException x) {
                return (key);
            }
    }
}
