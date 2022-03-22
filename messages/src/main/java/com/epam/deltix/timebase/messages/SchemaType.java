package com.epam.deltix.timebase.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {
      ElementType.METHOD, ElementType.FIELD}
)
public @interface SchemaType {
  String encoding() default "";

  boolean isNullable() default true;

  SchemaDataType dataType() default SchemaDataType.DEFAULT;

  String minimum() default "";

  String maximum() default "";

  Class[] nestedTypes() default  {
  }
  ;
}
