package com.epam.deltix.util.annotations;

import java.lang.annotation.*;

/**
 * Marks field/parameter/local-variable/method-return-type of int type which should be considered as time-of-day type.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TimeOfDay {
}
