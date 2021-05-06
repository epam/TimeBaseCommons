package com.epam.deltix.util.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Serves as directive for Deltix Dependency Analyzer that annotated class has dependency on classes listed by values parameter.
 *
 * Sometimes third-party library may use yet another third-party via reflection. This directive allows to reflect such hidden dependencies.
 *
 * For example:
 * <pre>
 * &#64;DependsOnClass (org.apache.xbean.spring.context.ClassPathXmlApplicationContext.class)
 * </pre>
 *
 * @see Depends
 */
@Documented
@Retention (RetentionPolicy.CLASS)
@Target (ElementType.TYPE)
public @interface DependsOnClass {
    Class<?> [] value();
}
