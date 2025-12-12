package com.epam.deltix.util.lang;

import java.lang.annotation.*;

/**
 * Serves as directive for Deltix Dependency Analyzer that annotated class has dependency on resources listed by values parameter.
 *
 * For example:
 * <pre>
 * &#64;Depends ("../jaxb.index")
 *
 * or
 *
 * &#64;Depends ( {
 *   "deltix/qsrv/hf/framework/spring/spring-client-config.xml",
 *   "deltix/qsrv/hf/framework/spring/spring-common-config.xml"
 * })
 * </pre>
 *
 * @see DependsOnClass
 */
@Documented
@Retention (RetentionPolicy.CLASS)
@Target (ElementType.TYPE)
public @interface Depends {
    public String []     value ();
}
