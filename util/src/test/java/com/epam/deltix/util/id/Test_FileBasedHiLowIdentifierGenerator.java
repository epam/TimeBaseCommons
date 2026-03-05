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

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.experimental.categories.Category;

/**
 * @author Andy
 *         Date: 5/3/11 12:48 PM
 */
public class Test_FileBasedHiLowIdentifierGenerator {

    private FileHiLowIdentifierGenerator idgen;

    @After
    public void clean () throws IOException {
        if (idgen != null)
            idgen.close();
    }

    @Test(timeout=60000)
    public void testNextBlock () throws Exception {
        final int blockSize = 5;
        idgen = createFileIdGenerator ("testNextBlock", blockSize);
        for (int i=1; i < 100; i ++)
            assertEquals (i, idgen.next());

    }

    @Test(timeout=60000)
    public void testReuse() throws Exception {
        final int blockSize = 500;

        idgen = createFileIdGenerator ("testReuse", blockSize, true);
        idgen.close();
        idgen = createFileIdGenerator ("testReuse", blockSize, true);
        idgen.close();
        idgen = createFileIdGenerator ("testReuse", blockSize, true);
        assertEquals (1, idgen.next());
        idgen.close();
        idgen = createFileIdGenerator ("testReuse", blockSize, true);
        assertEquals(2, idgen.next());
        assertEquals (3, idgen.next());
        idgen.close();
        idgen = createFileIdGenerator ("testReuse", blockSize, true);
        assertEquals (4, idgen.next());
        assertEquals(5, idgen.next());
        assertEquals (6, idgen.next());
        idgen.close();
        idgen = createFileIdGenerator ("testReuse", blockSize, true);
        assertEquals (7, idgen.next());
        assertEquals (8, idgen.next());
        assertEquals(9, idgen.next());
        assertEquals(10, idgen.next());
    }


    @Test(timeout=180000)
    public void testRandom() throws Exception {
        final int blockSize = 500;
        Set<Long> ids = new HashSet<Long>(1000);
        Random rnd = new Random (2012);
        for (int i=0; i < 1000; i++) {
            idgen = createFileIdGenerator ("testRandom", blockSize);
            //System.out.print('.');
            for (int j=0; j < rnd.nextInt(4*blockSize); j++) {   // crazy loop condition
                long id = idgen.next();

                assertTrue("positive id: " + id + " on iteration " + i, id > 0);
                assertTrue("unique id:" + id, ids.add(id));
            }
            idgen.close();
            idgen = null;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Test(timeout=60000)
    public void testBug() throws Exception {
        final int blockSize = 500;
        idgen = createFileIdGenerator ("testBug", blockSize, true);
        while (idgen.next() < 425)
            ; // do nothing
        idgen.close();
        idgen = createFileIdGenerator ("testBug", blockSize);
        assertEquals(426, idgen.next());
    }

    /// Internal stuff

    private FileHiLowIdentifierGenerator createFileIdGenerator (String fileKey, int blockSize) throws IOException, InterruptedException {
        return createFileIdGenerator(fileKey, blockSize, false);
    }

    private FileHiLowIdentifierGenerator createFileIdGenerator (String fileKey, int blockSize, boolean storeLastUsed) throws IOException, InterruptedException {
        if (usedFileKeys.add(fileKey))
            deleteFile(fileKey);

        return new FileHiLowIdentifierGenerator (new File(System.getProperty("java.io.tmpdir")), fileKey, blockSize, storeLastUsed);
    }

    private Set<String> usedFileKeys = new HashSet<>();

    private static void deleteFile(String fileKey) throws InterruptedException {
        File file = new File(new File(System.getProperty("java.io.tmpdir")), "sequence-"+fileKey+".id");

        int numberOfAttempts = 5;
        while (file.exists()) {
            if (file.delete())
                break;
            if (--numberOfAttempts == 0)
                fail("Can't delete the ID file used in the test: " + file.getAbsolutePath() );
            Thread.sleep(250);
        }
    }


}