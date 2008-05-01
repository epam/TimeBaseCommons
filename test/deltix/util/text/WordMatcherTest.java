package deltix.util.text;

import deltix.util.io.IOUtil;
import java.io.*;
import java.util.Enumeration;
import org.junit.*;
import static org.junit.Assert.*;

public class WordMatcherTest {
    private WordMatcherBuilder                  wm;
    private String []                           words;
    
    @Before
    public void     setUp () throws Exception {
        wm = new WordMatcherBuilder ();
        
        words = IOUtil.readLinesFromClassPath ("deltix/util/text/tickers.txt");
        
        for (String s : words) 
            wm.add (s);        
    }
    
    private void    check (WordMatcher m, CharSequence s, boolean expectedResult) {
        byte []         bytes = new byte [s.length ()];
        
        for (int ii = 0; ii < bytes.length; ii++)
            bytes [ii] = (byte) s.charAt (ii);
        
        assertEquals ("CS match failed for '" + s + "'", expectedResult, m.matches (s));
        assertEquals ("byte match failed for '" + s + "'", expectedResult, m.matches (bytes, 0, bytes.length));
    }
    
    private void    testSuite (WordMatcher m) {
        check (m, "AESK", false);
        check (m, "CSD", false);
        
        for (String s : words)
            check (m, s, true);
        
        for (String s : words)
            check (m, s + "*", false);
        
        int     count = 0;
        
        for (Enumeration <CharSequence> e = m.vocabulary (); e.hasMoreElements (); ) {
            check (m, e.nextElement (), true);
            count++;
        }
        
        assertEquals (words.length, count);
    }
    
    @Test
    public void     testInterpreter () {
        testSuite (wm);
    }    
    
    @Test
    public void     testCompiler () throws Exception {        
        testSuite (wm.compile ());
    }
    
    @Test
    public void     testCompiler32 () throws Exception {        
        testSuite (wm.compile32 ());
    }
}
