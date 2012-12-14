package deltix.util.xml;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.bind.v2.model.annotation.*;
import deltix.util.lang.IKVMUtil;
import java.util.*;
import javax.xml.bind.*;

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
    
    public static JAXBContext      createContext (String packPath) 
        throws JAXBException 
    {
        Map<String, Object> jaxbConfig = new HashMap<String, Object>();
        AnnotationReader reader = new JAXBStackTraceSuppressor();
        jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);

        return JAXBContextFactory.newInstance (packPath, jaxbConfig);            
    }
}
