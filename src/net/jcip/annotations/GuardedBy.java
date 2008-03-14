package net.jcip.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The field or method to which this annotation is applied can only
 * be accessed when holding a particular lock, which may be a
 * built-in (synchronization) lock, or may be an explicit
 * java.util.concurrent.Lock.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GuardedBy {
  String value();
}