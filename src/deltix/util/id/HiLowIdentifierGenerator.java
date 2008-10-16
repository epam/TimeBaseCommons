package deltix.util.id;

public abstract class HiLowIdentifierGenerator implements IdentifierGenerator {

    protected final int blockSize;
    protected final String key;
    protected long base;
    protected long id;

    /**
     * @param key Unique key for this generator instance.
     * @param blockSize number of
     */
    protected HiLowIdentifierGenerator (String key, int blockSize) {
        this.key = key;
        this.blockSize = blockSize;
        base = 0;
        id = blockSize;
        base = 0;
    }

    @Override
    public synchronized long next() {
        id++;

        if (id >= blockSize) {
            base = aquireNextBlock();
            id = 0;
        }

        return base + id;
    }

    /** @return new base */
    protected abstract long aquireNextBlock();


}
