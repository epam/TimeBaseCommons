package deltix.util.id;

import java.util.concurrent.TimeUnit;



public class SimpleIdentifierGenerator implements IdentifierGenerator {

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

}
