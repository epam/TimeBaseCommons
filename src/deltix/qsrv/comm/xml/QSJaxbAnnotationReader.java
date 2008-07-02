package deltix.qsrv.comm.xml;

import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import deltix.util.lang.IKVMUtil;

/**
 * Patched version of JAXB's RuntimeAnnotationReader that marks java.lang.Exception as @XmlTransient
 */
public class QSJaxbAnnotationReader extends TransientAnnotationReader implements RuntimeAnnotationReader {

    public QSJaxbAnnotationReader () {
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
