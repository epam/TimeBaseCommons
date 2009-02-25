package deltix.util.id;


public abstract class HiLowIdentifierGenerator implements ResettableIdentifierGenerator {

    protected final int blockSize;
    protected final long startId; 
    protected final String key;
    private long base;
    private long id;

    
    /**
     * @param key Unique key for this generator instance.
     * @param blockSize number of
     */
    protected HiLowIdentifierGenerator (String key, int blockSize, long startId) {
        this.key = key;
        this.blockSize = blockSize;
        this.startId = startId;
        
        base = 0;
        id = blockSize;
    }

    @Override
    public synchronized long next() {
        id++;

        if (id >= blockSize) {
            base = aquireNextBlock(0);
            id = 0;
        }

        return base + id;
    }

    /** @return new base */
    protected abstract long aquireNextBlock(long resetNextBlock);


    @Override
    public void setNext (long nextId) {
    	id = 0;
    	aquireNextBlock(nextId);
    }
}
