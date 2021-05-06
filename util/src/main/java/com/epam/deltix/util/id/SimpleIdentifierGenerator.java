package com.epam.deltix.util.id;

import java.util.concurrent.TimeUnit;



public class SimpleIdentifierGenerator implements ResettableIdentifierGenerator {

	private long nextID;

    public SimpleIdentifierGenerator () {
        this( System.currentTimeMillis() % TimeUnit.DAYS.toMillis(1));
    }

    public SimpleIdentifierGenerator(long base) {
	    this.nextID = base;
	}

	@Override
	public synchronized long next() {
		return nextID++;
	}


    @Override
    public synchronized void setNext (long nextId) {
    	if (nextID < nextId) {
    		this.nextID = nextId;
    	}
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

}
