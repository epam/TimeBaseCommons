package deltix.util.id;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Andy
 *         Date: 5/3/11 12:48 PM
 */
/*  ##UHF.FRAMEWORK## */
public class Test_FileBasedHiLowIdentifierGenerator {

    private static final String TEST_KEY = "test";

    FileHiLowIdentifierGenerator idgen;
    @Before
    public void init () {
        deleteFile();
    }

    @After
    public void clean () throws IOException {
        if (idgen != null)
            idgen.close();
    }
    
    @Test
    public void testNextBlock () throws IOException {
        final int blockSize = 5;
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        for (int i=1; i < 100; i ++)
            assertEquals (i, idgen.next());

        idgen.close();
        idgen = null;
    }

    @Test
    public void testReuse() throws IOException {
        final int blockSize = 500;

        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        idgen.close();
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        idgen.close();
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        assertEquals (1, idgen.next());
        idgen.close();
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        assertEquals (2, idgen.next());
        assertEquals (3, idgen.next());
        idgen.close();
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        assertEquals (4, idgen.next());
        assertEquals (5, idgen.next());
        assertEquals (6, idgen.next());
        idgen.close();
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        assertEquals (7, idgen.next());
        assertEquals (8, idgen.next());
        assertEquals (9, idgen.next());
        assertEquals(10, idgen.next());
        idgen.close();
        idgen = null;
    }


    @Test
    public void testRandom() throws Exception {
        final int blockSize = 500;
        Set<Long> ids = new HashSet<Long>(1000);
        Random rnd = new Random (2012);
        for (int i=0; i < 100; i++) {
            idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);

            long id=0;
            for (int j=0; j < rnd.nextInt(4*blockSize); j++) {   // crazy loop condition
                id = idgen.next();

                assertTrue("positive id: " + id + " on iteration " + i, id > 0);
                assertTrue("unique id:" + id, ids.add(id));
            }
            idgen.close();
        }
        idgen = null;
    }

    @Test
    public void testBug() throws IOException {
        final int blockSize = 500;
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        while (idgen.next() < 425);
        idgen.close();
        idgen = new FileHiLowIdentifierGenerator (TEST_KEY, blockSize);
        assertEquals(426, idgen.next());
        idgen = null;

    }


    private static void deleteFile() {
        FileBasedHiLowIdentifierGenerator.getSequenceFile(TEST_KEY).delete();
    }

}
