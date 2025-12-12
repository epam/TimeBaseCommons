package com.epam.deltix.util.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Map;

/**
 *  Convenience class for creating JAXBContext instances while supplying the
 *  correct class loader, to work around IKVM's default class loading.
 */
public abstract class JAXBContextFactory {
    private static final ClassLoader     CL = JAXBContextFactory.class.getClassLoader ();

    /**
     *  Equivalent to JAXBContext.newInstance
     */
    public static JAXBContext  newInstance (String packagePath) throws JAXBException {
        return (JAXBContext.newInstance (packagePath, CL));
    }

    public static JAXBContext newInstance(String contextPath, ClassLoader classLoader) throws JAXBException {
        return (JAXBContext.newInstance (contextPath, classLoader));
    }

    public static JAXBContext newInstance(String contextPath, Map<String, ?> properties) throws JAXBException {
        return (JAXBContext.newInstance(contextPath, CL, properties));
    }

    public static Unmarshaller      createStdUnmarshaller (JAXBContext context)
        throws JAXBException
    {
        Unmarshaller    unmarshaller = context.createUnmarshaller ();

        unmarshaller.setEventHandler (AbortingValidationEventHandler.INSTANCE);

        return (unmarshaller);
    }

    public static Marshaller        createStdMarshaller (JAXBContext context)
        throws JAXBException
    {
        Marshaller      marshaller = context.createMarshaller ();

        marshaller.setProperty (Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty (Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        return (marshaller);
    }
}
