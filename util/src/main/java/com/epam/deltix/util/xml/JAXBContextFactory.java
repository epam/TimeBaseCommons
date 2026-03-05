/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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