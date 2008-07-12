package deltix.util.lang;

/**
 *
 */
public interface GrowthPolicy {
    public long          computeLength (long curLength, long minLength);
}
