package com.epam.deltix.timebase.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {
      ElementType.TYPE}
)
public @interface SchemaGuid {
  String value();
}
