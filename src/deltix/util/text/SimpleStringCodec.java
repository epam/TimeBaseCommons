package deltix.util.text;

/**
 *
 */
public class SimpleStringCodec {
    public static final SimpleStringCodec    DEFAULT_INSTANCE = 
        new SimpleStringCodec ();
    
    private final char          open;
    private final char          close;
    
    public SimpleStringCodec () { 
        this ('_');
    }
    
    public SimpleStringCodec (char openClose) {
        this (openClose, openClose);
    }

    public SimpleStringCodec (char open, char close) {
        this.open = open;
        this.close = close;
    }
        
    public boolean                  shouldEscape (char ch) {
        return (!Character.isLetterOrDigit (ch));
    }
    
    public final String             encode (String s) {
        StringBuilder   sb = new StringBuilder ();
        encode (s, 0, s.length (), sb);
        return (sb.toString ());
    }
    
    public final void               encode (
        CharSequence                    s, 
        int                             start,
        int                             length,
        StringBuilder                   out
    )
    {
        for (int ii = start; ii < length; ii++) {
            char        ch = s.charAt (ii);
            
            if (ch == open || ch == close || shouldEscape (ch)) {
                out.append (open);
                out.append ((int) ch);
                out.append (close);
            }
            else
                out.append (ch);
        }
    }
    
    public final String             decode (String s) {
        StringBuilder   sb = new StringBuilder ();
        decode (s, 0, s.length (), sb);
        return (sb.toString ());
    }
    
    public final void               decode (
        CharSequence                    s, 
        int                             start,
        int                             length,
        StringBuilder                   out
    )
    {
        for (int ii = start; ii < length; ) {
            char        ch = s.charAt (ii++);
                         
            if (ch == open) {
                int v = 0;

                for (;;) {
                    ch = s.charAt (ii++);

                    if (ch == close)
                        break;

                    if (!Character.isDigit (ch))
                        throw new RuntimeException (s.subSequence (0, length).toString ());

                    v = v * 10 + (ch - '0');
                }

                out.append ((char) v);
            }
            else
                out.append (ch);
        }
    }
}
