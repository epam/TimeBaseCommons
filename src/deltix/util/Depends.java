package deltix.util;

import java.lang.annotation.*;

@Documented
@Retention (RetentionPolicy.CLASS)
@Target (ElementType.TYPE)
public @interface Depends {    
    public String []     value ();
}
