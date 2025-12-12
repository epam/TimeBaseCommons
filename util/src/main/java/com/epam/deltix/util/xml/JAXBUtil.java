package com.epam.deltix.util.xml;

import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.bind.v2.model.annotation.AnnotationReader;

import java.util.HashMap;
import java.util.Map;

public class JAXBUtil {

    public static final String ANNOTATION_READER_PROPERTY = JAXBRIContext.ANNOTATION_READER;

    public static Map<String, Object> createConfig(AnnotationReader reader) {
        Map<String, Object> jaxbConfig = new HashMap<String, Object>();
        jaxbConfig.put (JAXBUtil.ANNOTATION_READER_PROPERTY, reader);
        return jaxbConfig;
    }

}