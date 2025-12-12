package com.epam.deltix.util.annotations;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Marks field/parameter/local-variable/method-return-type of integer type which should be considered as duration.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Duration {

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}