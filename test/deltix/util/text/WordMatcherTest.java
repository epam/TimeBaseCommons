package deltix.util.text;

import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class WordMatcherTest {
    private WordMatcherBuilder                 wm;
    
    private String []       TEST = {
        "",
        "asd",
        "ipwoer",
        "xyz",
        "as",
        "asdsx",
        "bb$uu",
        "aa"
    };
    
    @Before
    public void     setUp () throws IOException {
        wm = new WordMatcherBuilder ();
        
        for (String s : TEST)
            wm.add (s);
    }
    
    private void    check (WordMatcher m, String s, boolean expectedResult) {
        byte []         bytes = s.getBytes ();
        
        assertEquals (expectedResult, m.matches (s));
        assertEquals (expectedResult, m.matches (bytes, 0, bytes.length));
    }
    
    private void    testSuite (WordMatcher m) {
        for (String s : TEST)
            check (m, s, true);
        
        for (String s : TEST)
            check (m, s + "*", false);
    }  
    
    @Test
    public void     testInterpreter () {
        testSuite (wm);
    }    
    
    @Test
    public void     testCompiler () throws Exception {        
        WordMatcher          code = wm.compile ();
        
        testSuite (code);
    }
}
