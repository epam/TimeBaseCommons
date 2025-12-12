package com.epam.deltix.util.text;

/**
 * Immutable, thread-safe codec for strings
 */
public class SimpleStringCodec {
    public static final SimpleStringCodec    DEFAULT_INSTANCE = 
        new SimpleStringCodec ();

    private final boolean       escapeUppercase;
    private final char          open;
    private final char          close;
    
    public SimpleStringCodec () { 
        this ('_');
    }
    
    public SimpleStringCodec (char openClose) {
        this (openClose, openClose, true);
    }

    public SimpleStringCodec (char open, char close, boolean escapeUppercase) {
        this.open = open;
        this.close = close;
        this.escapeUppercase = escapeUppercase;
    }
        
    public boolean                  shouldEscape (char ch) {
        return (!Character.isLetterOrDigit (ch));
    }
    
    public final String             encode (String s) {
        return encode2(s);
    }

    public final String             encode2 (CharSequence s) {
        if (s == null) return null;
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
        for (int i = start; i < length; i++) {
            char        ch = s.charAt (i);

            if (ch == open || shouldEscape (ch)) {
                out.append (open);
                out.append((int)ch);
                out.append (close);
            }
            else if (escapeUppercase && Character.isUpperCase(ch)) {
                out.append(open);
                out.append(close);
                //
                for (;i < length;) {
                    char next = s.charAt (i);
                    if (next != open && !shouldEscape (next) && Character.isUpperCase (next)) {
                        out.append(Character.toLowerCase(next));
                        i++;
                    }
                    else {
                        i--;
                        break;
                    }
                }
                out.append(close);
            }
            else
                out.append (ch);
        }
    }
    
    public final String             decode (String s) {
        if (s == null) return null;
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
        for (int i = start; i < length; ) {
            char        ch = s.charAt (i++);
                         
            if (ch == open) {
                int v = 0; 

                if (s.charAt (i) == close) {
                    i++;
                    for (;;) {
                        ch = s.charAt (i++);

                        if (ch == close)
                            break;

                        out.append (Character.toUpperCase(ch));
                    }
                }
                else {
                    for (;;) {
                        ch = s.charAt (i++);

                        if (ch == close)
                            break;

                        if (!Character.isDigit (ch))
                            throw new RuntimeException (s.subSequence (0, length).toString ());

                        v = v * 10 + (ch - '0');
                    }

                    out.append ((char) v);
                }
            }
            else
                out.append (ch);
        }
    }
}
