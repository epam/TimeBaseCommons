package deltix.util.id;

public abstract class HiLowIdentifierGenerator implements IdentifierGenerator {

    protected final int blockSize;
    protected long base;
    protected long id;

    protected HiLowIdentifierGenerator (int blockSize) {
        this.blockSize = blockSize;
        base = 0;
        id = 0;
        base = aquireNextBlock();
    }

    @Override
    public synchronized long next() {
        id++;

        if (id > blockSize) {
            base = aquireNextBlock();
            id = 0;
        }

        return base + id;
    }

    /** @return new base */
    protected abstract long aquireNextBlock();


}
