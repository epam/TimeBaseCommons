package deltix.util.id;

public abstract class HiLowIdentifierGenerator implements IdentifierGenerator {

    private final int blockSize;
    private int base;
    private int id;

    protected HiLowIdentifierGenerator (int blockSize) {
        this.blockSize = blockSize;

        base = aquireNextBlock(base, blockSize);
        id = 0;
    }

    @Override
    public synchronized int next() {
        id++;

        if (id > blockSize) {
            base = aquireNextBlock(base, blockSize);
            id = 0;
        }

        return base + id;
    }

    /** @return new base */
    protected abstract int aquireNextBlock(int base, int blockSize);


}
