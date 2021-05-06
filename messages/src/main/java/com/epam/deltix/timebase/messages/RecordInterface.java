package com.epam.deltix.timebase.messages;

/**
 * Base read-write interface for all messages in TimeBase hierarchy/
 */
public interface RecordInterface extends RecordInfo {
    /**
     * Deep copies content from src instance to this.
     * @param src source for copy.
     */
    RecordInterface     copyFrom(RecordInfo src);

    /**
     * Reset all instance field to their default states.
     * @return this.
     */
    RecordInterface     reset();

    /**
     * Set null to all fields of this instance.
     * @return this.
     */
    RecordInterface     nullify();
}
