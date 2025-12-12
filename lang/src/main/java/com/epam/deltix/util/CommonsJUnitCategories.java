package com.epam.deltix.util;

/**
 * @deprecated Use JUnit 5 tags instead ({@link org.junit.jupiter.api.Tag}).
 * <a href="https://junit.org/junit5/docs/current/user-guide/#writing-tests-tagging-and-filtering">Tagging and Filtering</a>
 */
@Deprecated
public final class CommonsJUnitCategories {
    public interface Unit extends All {}

    public interface Utils extends Unit {}

    public interface UHFUtils extends Unit {}

    public interface Stress extends All {}

    public interface All {}
}

