package com.epam.deltix.util.collections;

import java.util.HashMap;
import java.util.Map;

public class TwoWayMap<T1, T2> {
	private final Map<T1, T2> firstToSecond = new HashMap<T1, T2> ( );
	private final Map<T2, T1> secondToFirst = new HashMap<T2, T1> ( );

    public TwoWayMap () {
    }

    public TwoWayMap (Map<T1,T2> prototype) {
        for (Map.Entry<T1,T2> entry : prototype.entrySet()) {
            T1 k = entry.getKey();
            T2 v = entry.getValue();

            firstToSecond.put(k, v);
            secondToFirst.put(v, k);
        }
    }

	public void put ( T1 first, T2 second ) {
		firstToSecond.put ( first,
		                    second );
		secondToFirst.put ( second,
		                    first );
	}

	public final Map<T1, T2> getFirstToSecond ( ) {
		return firstToSecond;
	}

	public final Map<T2, T1> getSecondToFirst ( ) {
		return secondToFirst;
	}

	public T2 getFirst ( T1 first ) {
		return firstToSecond.get ( first );
	}

	public T1 getSecond ( T2 second ) {
		return secondToFirst.get ( second );
	}

	public final void clear ( ) {
		firstToSecond.clear ( );
		secondToFirst.clear ( );
	}

	public String toString ( ) {
		return firstToSecond.toString ( ) + "\n" + secondToFirst.toString ( );
	}
}
