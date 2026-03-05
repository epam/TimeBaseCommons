/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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