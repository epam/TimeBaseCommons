package deltix.qsrv.hf.pub;

/**
 * Base read-only interface for all messages in TimeBase hierarchy/
 */
public interface RecordInfo {

    /**
     * Creates copy of this instance.
     * @return copy.
     */
    RecordInfo clone();
}
