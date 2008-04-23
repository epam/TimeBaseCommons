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
    
    @Test
    public void     testInterpreter () {
        for (String s : TEST)
            assertTrue (wm.match (s));
        
        for (String s : TEST)
            assertFalse (wm.match (s + "*"));
    }    
    
    @Test
    public void     testCompiler () throws Exception {        
        WordMatcher          code = wm.compile ();
        
        for (String s : TEST)
            assertTrue (code.match (s));
        
        for (String s : TEST)
            assertFalse (code.match (s + "*"));
    }
}
