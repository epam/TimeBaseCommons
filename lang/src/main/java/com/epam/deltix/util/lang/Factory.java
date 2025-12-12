package com.epam.deltix.util.lang;

/**
 *
 */
@FunctionalInterface
public interface Factory <T> {
     T        create ();
}
