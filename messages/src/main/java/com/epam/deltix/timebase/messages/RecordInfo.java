package com.epam.deltix.timebase.messages;

/**
 * Base read-only interface for all messages in TimeBase hierarchy
 */
public interface RecordInfo {

    /**
     * Creates copy of this instance.
     * @return copy.
     */
    RecordInfo clone();

    /**
     * Fill-in string builder with string representation of an Object.
     * @return StringBuilder instance that was passed into parameter.
     */
    StringBuilder toString(StringBuilder str);
}
