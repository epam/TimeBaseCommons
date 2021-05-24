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

import com.sun.xml.bind.api.JAXBRIContext;
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
        jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, new JAXBStackTraceSuppressor());

        return JAXBContextFactory.newInstance (packPath, jaxbConfig);            
    }


}