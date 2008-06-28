package deltix.util.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

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
}
