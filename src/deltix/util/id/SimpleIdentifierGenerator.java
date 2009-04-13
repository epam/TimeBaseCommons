package deltix.util.id;

import java.util.concurrent.TimeUnit;



public class SimpleIdentifierGenerator implements ResettableIdentifierGenerator {

	private long nextID = 1;

	private final int base;

    public SimpleIdentifierGenerator () {
        this((int) (System.currentTimeMillis() % TimeUnit.DAYS.toMillis(1)));
    }

    public SimpleIdentifierGenerator(int base) {
	    this.base = base;
	}

	@Override
	public synchronized long next() {
		return base + (nextID++);
	}

	@Override
	public synchronized void setNext(long nextID) {
		this.nextID = nextID - base;
	}

    @Override
    public synchronized void markUsed (long usedId) {
    	long nextId =  base + nextID;
    	if (nextId <= usedId) {
    		setNext (usedId + 1);
    	}
    }

}
