package deltix.qsrv.hf.pub;

import java.lang.annotation.*;

/**
 * Tags a field as part of the message record's primary key (record identity).
 * This tag can be applied to more than one field of single class (composite primary key).
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface PrimaryKey {   
}
