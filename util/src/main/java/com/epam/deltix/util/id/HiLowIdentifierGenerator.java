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
package com.epam.deltix.util.id;


public abstract class HiLowIdentifierGenerator implements IdentifierGenerator {

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
            base = acquireNextBlock(0);
            id = 0;
        }

        return base + id;
    }

    /** @return new base */
    protected abstract long acquireNextBlock(long resetNextBlock);


    @Override
    public void reset() throws UnsupportedOperationException {
        // do nothing
    }
}