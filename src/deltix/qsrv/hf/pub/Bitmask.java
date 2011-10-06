package deltix.qsrv.hf.pub;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Marks an enumerated class a being a BITMASK in TimeBase.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE })
public @interface Bitmask {

}
