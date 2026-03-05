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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


/**
 * HiLowIdentifierGenerator that uses file storage in given directory to persist last allocated block.
 * This implementation doesn't keep file storage open, allowing concurrent access from multiple processes.
 *
 * @see FileHiLowIdentifierGenerator
 */
public final class SharedFileHiLowIdentifierGenerator extends FileBasedHiLowIdentifierGenerator {

    @SuppressWarnings("unused")
    public SharedFileHiLowIdentifierGenerator (File dir, String key, int blockSize) {
        this(dir, key, blockSize, 1);
    }
    
    public SharedFileHiLowIdentifierGenerator (File dir, String key, int blockSize, long startId) {
        super(dir, key, blockSize, startId);
    }

    @Override
    protected long acquireNextBlock(long resetNextBlock) {
        RandomAccessFile raf = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {

            try {
                raf = new RandomAccessFile(file, "rw");
                channel = raf.getChannel();
                lock = channel.lock();

                final long nextBlock;
                if (resetNextBlock != 0) {
                	nextBlock = resetNextBlock;                	
                } else {
	                if (file.length() == 0) {
	                    nextBlock = startId;
	                } else {
	                	String lastBlock = raf.readLine();
	                    nextBlock = Long.parseLong(lastBlock) + blockSize;
	                }
                }

                raf.seek(0L);
                raf.write(Long.toString(nextBlock).getBytes());

                return nextBlock;



            } catch (IOException e) {
                throw new RuntimeException ("Error accessing sequence storage file: " + e.getMessage(), e);
            } finally {
                    if (lock != null)
                        lock.release();
                    if (channel != null)
                        channel.close();
                    if (raf != null)
                        raf.close();
            }

        } catch (IOException e) {
            throw new RuntimeException ("Error accessing sequence storage file: " + e.getMessage(), e);
        }

    }
    
}