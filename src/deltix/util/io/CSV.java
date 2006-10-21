package deltix.util.io;

import java.io.*;

/**
 *
 */
public class CSV {
    public static void     printCell (String unescapedText, Writer wr) throws IOException {
        int             len = unescapedText.length ();
        
        if (len == 0)
            return;
        
        boolean         needEscape = false;
        
        search: for (int ii = 0; ii < len; ii++) {
            switch (unescapedText.charAt (ii)) {
                case '"':
                case ',':
                    needEscape = true;
                    break search;
            }
        }
        
        if (needEscape) 
            wr.write ('"');
        
        for (int ii = 0; ii < len; ii++) {
            char        ch = unescapedText.charAt (ii);
            
            if (ch == '"') 
                wr.write ('"');
                    
            wr.write (ch);
        }
        
        if (needEscape) 
            wr.write ('"');
    }
}
