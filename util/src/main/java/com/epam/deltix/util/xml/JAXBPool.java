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
