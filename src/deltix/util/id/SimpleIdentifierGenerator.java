package deltix.util.id;

import java.util.concurrent.TimeUnit;



public class SimpleIdentifierGenerator implements IdentifierGenerator {

	private long nextID = 1;

	private final int base;
	public SimpleIdentifierGenerator () {
	    base = (int) (System.currentTimeMillis() % TimeUnit.DAYS.toMillis(1));
	}

	@Override
	public synchronized long next() {
		return base + (nextID++);
	}

}
