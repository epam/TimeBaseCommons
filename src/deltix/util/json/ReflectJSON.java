package deltix.util.json;

import deltix.util.lang.*;
import java.lang.reflect.*;
import org.fife.rsta.ac.java.rjc.lang.*;

/**
 *
 */
public class ReflectJSON {
    public static void      format (Object obj, StringBuilder sb) 
        throws Exception 
    {
        if (obj == null) {
            sb.append ("null");
            return;
        }
            
        Class <?>       cls = obj.getClass ();
        
        if (cls.isPrimitive () ||
            obj instanceof Number ||
            obj instanceof Boolean)
        {
            sb.append (obj);
        }
        else if (obj instanceof CharSequence) {
            sb.append ('"');
            StringUtils.escapeJavaString (obj.toString (), sb);
            sb.append ('"');
        }
        else if (cls.isArray ()) {
            sb.append ("[ ");
            
            int         len = Array.getLength (obj);
            
            for (int ii = 0; ii < len; ii++) {
                if (ii > 0) 
                    sb.append (',');
                
                format (Array.get (obj, ii), sb);
            }
            
            sb.append (" ]");
        }
        else {
            sb.append ("{ ");

            boolean         first = true;

            for (Field f : obj.getClass ().getFields ()) {
                if ((f.getModifiers () & Modifiers.STATIC) != 0)
                    continue;

                if (first)
                    first = false;
                else
                    sb.append (',');

                sb.append ('"');
                StringUtils.escapeJavaString (f.getName (), sb);
                sb.append ("\":");
                format (f.get (obj), sb);                
            }

            sb.append (" }");
        }
    }
}
