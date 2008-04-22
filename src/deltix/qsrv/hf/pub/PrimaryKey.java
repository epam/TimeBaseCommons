package deltix.qsrv.hf.pub;

import java.lang.annotation.*;

/**
 *  Tags a field as part of the message record's primary key.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface PrimaryKey {   
}
