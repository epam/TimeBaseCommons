package deltix.util.lang;

import java.lang.annotation.*;

/**
 * Serves as directive for Deltix Dependency Analyzer that annotated class has dependency on resources listed by values parameter.
 *
 * For example:
 * <pre>
 * @Depends ("../jaxb.index")
 *
 * or
 *
 * @Depends ( {
 *   "deltix/qsrv/hf/framework/spring/spring-client-config.xml",
 *   "deltix/qsrv/hf/framework/spring/spring-common-config.xml"
 * })
 * </pre>
 *
 * @See DependsClass
 */
@Documented
@Retention (RetentionPolicy.CLASS)
@Target (ElementType.TYPE)
public @interface Depends {
    public String []     value ();
}
