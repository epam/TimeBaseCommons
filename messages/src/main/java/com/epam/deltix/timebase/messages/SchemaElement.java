package com.epam.deltix.timebase.messages;

import java.lang.String;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {
      ElementType.TYPE, ElementType.METHOD, ElementType.FIELD}
)
public @interface SchemaElement {
  String name() default "";

  String title() default "";

  String description() default "";
}
