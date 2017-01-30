package deltix.qsrv.hf.pub;

/**
 * Created by VavilauA on 7/7/2016.
 */
public interface RecordInfo {

    /**
     * Deep copies content of this instance to destination instance.
     * @param dst destination for copy.
     */
    void copyTo(RecordInterface dst);

    /**
     * Creates copy of this instance.
     * @return copy.
     */
    RecordInterface clone();
}
