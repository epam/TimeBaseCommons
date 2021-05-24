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

import java.util.*;
import javax.xml.bind.*;

/**
 *  Nobody knows if pooling marshallers and/or unmarshallers ever 
 *  improves performance.
 */
public class JAXBPool {
    private JAXBContext                 mContext;
    private Stack <Marshaller>          mFreeMarshallers = new Stack <Marshaller> ();
    private Stack <Unmarshaller>        mFreeUnmarshallers = new Stack <Unmarshaller> ();
    private ValidationEventHandler      mEventHandler = null;
    
    public JAXBPool (JAXBContext context) {
        mContext = context;
    }
    
    public void                 setEventHandler (ValidationEventHandler handler) {
        mEventHandler = handler;
    }
     
    public Marshaller           checkOutMarshaller () 
        throws JAXBException
    {
        try {
            return (mFreeMarshallers.pop ());
        } catch (EmptyStackException x) {
            return (mContext.createMarshaller ());
        }
    }
    
    public void                 checkInMarshaller (Marshaller m) {
        mFreeMarshallers.push (m);
    }
    
    public Unmarshaller         checkOutUnmarshaller () 
        throws JAXBException
    {
        try {
            return (mFreeUnmarshallers.pop ());
        } catch (EmptyStackException x) {
            Unmarshaller    u = mContext.createUnmarshaller ();
            u.setEventHandler (mEventHandler);
            return (u);
        }
    }
    
    public void                 checkInUnmarshaller (Unmarshaller u) {
        mFreeUnmarshallers.push (u);
    }
}