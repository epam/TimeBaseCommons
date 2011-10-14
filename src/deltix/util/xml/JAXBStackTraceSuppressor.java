package deltix.util.xml;

import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import deltix.util.lang.IKVMUtil;

/**
 * Patched version of JAXB's RuntimeAnnotationReader that marks java.lang.Exception as @XmlTransient
 */
public class JAXBStackTraceSuppressor 
    extends TransientAnnotationReader 
    implements RuntimeAnnotationReader 
{
    public JAXBStackTraceSuppressor () {
        if (!IKVMUtil.IS_IKVM)
            try {
                addTransientField(Throwable.class.getDeclaredField("stackTrace"));            
            } catch (NoSuchFieldException unexpected) {
                throw new RuntimeException (unexpected);
            }
        
        try {
            addTransientMethod(Throwable.class.getDeclaredMethod("getStackTrace"));
        } catch (NoSuchMethodException unexpected) {
            throw new RuntimeException (unexpected);
        }
    }
}
