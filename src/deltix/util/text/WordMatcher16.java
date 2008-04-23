package deltix.util.text;

/**
 *
 */
class WordMatcher16 implements WordMatcher {
    private final short []          mCode;
    
    WordMatcher16 (short [] code) {
        mCode = code;
    }
    
    public boolean  matches (final byte [] bytes, int offset, int len) {
        int             codeIdx = 0;
        
        for (;;) {
            if (len == 0)
                return (mCode [codeIdx] != 0);
            
            final int   base = mCode [codeIdx + 1] & 0xFFFF;
            final int   jump = bytes [offset] - base;
            
            if (jump < 0)
                return (false);
            
            final int   jtl = mCode [codeIdx + 2] & 0xFFFF;
            
            if (jump >= jtl)
                return (false);
            
            offset++;
            len--;
            
            codeIdx = mCode [codeIdx + 3 + jump] & 0xFFFF;
            
            if (codeIdx == -1)
                return (false);
        }        
    }

    public boolean  matches (CharSequence s) {
        final int       len = s.length ();
        int             sIdx = 0;
        int             codeIdx = 0;
        
        for (;;) {
            if (sIdx == len)
                return (mCode [codeIdx] != 0);
            
            final int   base = mCode [codeIdx + 1] & 0xFFFF;
            final int   jump = s.charAt (sIdx) - base;
            
            if (jump < 0)
                return (false);
            
            final int   jtl = mCode [codeIdx + 2] & 0xFFFF;
            
            if (jump >= jtl)
                return (false);
            
            sIdx++;
            
            codeIdx = mCode [codeIdx + 3 + jump] & 0xFFFF;
            
            if (codeIdx == -1)
                return (false);
        }        
    }

}
