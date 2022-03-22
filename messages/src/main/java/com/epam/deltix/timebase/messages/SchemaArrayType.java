package com.epam.deltix.timebase.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {
      ElementType.METHOD, ElementType.FIELD}
)
public @interface SchemaArrayType {
  boolean isNullable() default true;

  boolean isElementNullable() default true;

  String elementEncoding() default "";

  SchemaDataType elementDataType() default SchemaDataType.DEFAULT;

  String elementMinimum() default "";

  String elementMaximum() default "";

  Class[] elementTypes() default  {
  }
  ;
}
