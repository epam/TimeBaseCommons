package deltix.qsrv.hf.pub;

import java.lang.annotation.*;

/**
 *  Defines a user-visible title for a record field.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface Unique {   
}
