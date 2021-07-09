package deltix.util.annotations;

import java.lang.annotation.*;

/**
 * Marks field/parameter/local-variable/method-return-type of long type which should be considered as timestamp milliseconds type.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
public @interface TimestampMs {
}
