package com.epam.deltix.util.lang;

import com.epam.deltix.util.collections.EmptyEnumeration;
import java.util.*;

/**
 *
 */
public class SafeResourceBundle {
    public static ResourceBundle    getBundle (Class cls, String rbname) {
        String      s = cls.getName ();
        String      n = cls.getSimpleName ();

        assert s.endsWith (n);

        return (getBundle (s.substring (0, s.length () - n.length ()) + rbname));
    }

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
                        return (key);
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
