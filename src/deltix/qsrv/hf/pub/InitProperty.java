package deltix.qsrv.hf.pub;

import java.lang.annotation.*;

/**
 *  Binds an Algorithm property to a property found in the 
 *  init.properties resource.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface InitProperty {    
}
