package deltix.util.text;

/**
 *
 */
class WordMatcher32 implements WordMatcher {
    private final int []          mCode;
    
    WordMatcher32 (int [] code) {
        mCode = code;
    }
    
    public boolean  match (CharSequence s) {
        final int       len = s.length ();
        int             sIdx = 0;
        int             codeIdx = 0;
        
        for (;;) {
            final int   header = mCode [codeIdx++];
            
            if (sIdx == len)
                return ((header & 0x80000000) != 0);
            
            final int   base = header & 0xFFFF;
            final int   jtl = (header >> 16) & 0x7FFF;
            final int   jump = s.charAt (sIdx++) - base;
            
            if (jump < 0 || jump >= jtl)
                return (false);
            
            codeIdx = mCode [codeIdx + jump];
            
            if (codeIdx == -1)
                return (false);
        }        
    }

}
