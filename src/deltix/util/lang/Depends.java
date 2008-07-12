package deltix.util.lang;

import java.lang.annotation.*;

@Documented
@Retention (RetentionPolicy.CLASS)
@Target (ElementType.TYPE)
public @interface Depends {    
    public String []     value ();
}
