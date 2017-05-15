package deltix.util.xml;

import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

/**
 * Patched version of JAXB's RuntimeAnnotationReader that marks java.lang.Exception as @XmlTransient
 */
public class JAXBStackTraceSuppressor 
    extends TransientAnnotationReader 
    implements RuntimeAnnotationReader
{
    public JAXBStackTraceSuppressor () {
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
        jaxbConfig.put(JAXBUtil.ANNOTATION_READER_PROPERTY, new JAXBStackTraceSuppressor());

        return JAXBContextFactory.newInstance (packPath, jaxbConfig);            
    }


}
