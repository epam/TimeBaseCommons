package deltix.util.annotations;

import java.lang.annotation.*;

/**
 * Marks field/parameter/local-variable/method-return-type of byte type which should be considered as boolean type.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
public @interface Bool {
}
