package com.epam.deltix.timebase.messages;

import java.lang.String;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {
      ElementType.METHOD, ElementType.FIELD}
)
public @interface SchemaStaticType {
  String value();

  SchemaDataType dataType() default SchemaDataType.DEFAULT;
}
