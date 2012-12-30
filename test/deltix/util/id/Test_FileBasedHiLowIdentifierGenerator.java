package deltix.util.id;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Andy
 *         Date: 5/3/11 12:48 PM
 */
/*  ##UHF.FRAMEWORK## */
public class Test_FileBasedHiLowIdentifierGenerator {

    private static final String TEST_KEY = "test";

    private FileHiLowIdentifierGenerator idgen;

    private static FileHiLowIdentifierGenerator createFileIdGenerator (int blockSize) throws IOException {
        return createFileIdGenerator(blockSize, false);
    }

    private static FileHiLowIdentifierGenerator createFileIdGenerator (int blockSize, boolean storeLastUsed) throws IOException {
        return new FileHiLowIdentifierGenerator (TEST_KEY, blockSize, storeLastUsed);
    }
    @Before
    public void init () throws InterruptedException {
        deleteFile();
    }

    @After
    public void clean () throws IOException {
        if (idgen != null)
            idgen.close();
    }

    @Test(timeout=60000)
    public void testNextBlock () throws IOException {
        final int blockSize = 5;
        idgen = createFileIdGenerator (blockSize);
        for (int i=1; i < 100; i ++)
            assertEquals (i, idgen.next());

        idgen.close();
        idgen = null;
    }

    @Test(timeout=60000)
    public void testReuse() throws IOException {
        final int blockSize = 500;

        idgen = createFileIdGenerator (blockSize, true);
        idgen.close();
        idgen = createFileIdGenerator (blockSize, true);
        idgen.close();
        idgen = createFileIdGenerator (blockSize, true);
        assertEquals (1, idgen.next());
        idgen.close();
        idgen = createFileIdGenerator (blockSize, true);
        assertEquals (2, idgen.next());
        assertEquals (3, idgen.next());
        idgen.close();
        idgen = createFileIdGenerator (blockSize, true);
        assertEquals (4, idgen.next());
        assertEquals (5, idgen.next());
        assertEquals (6, idgen.next());
        idgen.close();
        idgen = createFileIdGenerator (blockSize, true);
        assertEquals (7, idgen.next());
        assertEquals (8, idgen.next());
        assertEquals (9, idgen.next());
        assertEquals(10, idgen.next());
        idgen.close();
        idgen = null;
    }


    @Test(timeout=180000)
    public void testRandom() throws Exception {
        final int blockSize = 500;
        Set<Long> ids = new HashSet<Long>(1000);
        Random rnd = new Random (2012);
        for (int i=0; i < 1000; i++) {
            idgen = createFileIdGenerator (blockSize);
            //System.out.print('.');
            for (int j=0; j < rnd.nextInt(4*blockSize); j++) {   // crazy loop condition
                long id = idgen.next();

                assertTrue("positive id: " + id + " on iteration " + i, id > 0);
                assertTrue("unique id:" + id, ids.add(id));
            }
            idgen.close();
        }
        idgen = null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Test(timeout=60000)
    public void testBug() throws IOException {
        final int blockSize = 500;
        idgen = createFileIdGenerator (blockSize, true);
        while (idgen.next() < 425)
            ; // do nothing
        idgen.close();
        idgen = createFileIdGenerator (blockSize);
        assertEquals(426, idgen.next());
        idgen = null;
    }


    private static void deleteFile() throws InterruptedException {
        File file = FileBasedHiLowIdentifierGenerator.getSequenceFile(TEST_KEY);

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
