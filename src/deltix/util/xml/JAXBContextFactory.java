package deltix.util.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Map;

/**
 *  Convenience class for creating JAXBContext instances while supplying the 
 *  corerct class loader, to work around IKVM's default class loading.
 */
public abstract class JAXBContextFactory {
    /**
     *  Equivalent to JAXBContext.newInstance
     */
    public static JAXBContext  newInstance (String packagePath) throws JAXBException {
        return (JAXBContext.newInstance (packagePath, JAXBContextFactory.class.getClassLoader ()));
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader) throws JAXBException {
        return (JAXBContext.newInstance (contextPath, classLoader));
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader, java.util.Map<java.lang.String, ?> properties) throws JAXBException {
        return (JAXBContext.newInstance(contextPath, classLoader, properties));
    }

    public static javax.xml.bind.JAXBContext newInstance(java.lang.Class... classes) throws javax.xml.bind.JAXBException {
        return (JAXBContext.newInstance(classes));
    }
}
