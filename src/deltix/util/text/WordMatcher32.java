package deltix.util.text;

/**
 *
 */
class WordMatcher32 implements WordMatcher {
    private final int []            mCode;
    
    private int                     getCodeSize (Node node) {
        final int       numBranches = node.length;
        final Node []   branches = node.branches;
        int             size = 3 + numBranches;

        for (int ii = 0; ii < numBranches; ii++) {
            Node    branch = branches [ii];

            if (branch != null)
                size += getCodeSize (branch);
        }

        return (size);
    }
    
    private int                     compile (Node node, int offset) {
        final int       numBranches = node.length;
        final Node []   branches = node.branches;
        
        mCode [offset++] = node.endOk ? 1 : 0;
        mCode [offset++] = node.base;
        mCode [offset++] = numBranches;

        int         endOffset = offset + numBranches;

        for (int ii = 0; ii < numBranches; ii++) {
            Node    branch = branches [ii];

            if (branch != null) {
                mCode [offset++] = endOffset;
                endOffset = compile (branch, endOffset);
            }
            else
                mCode [offset++] = -1;
        }

        return (endOffset);
    }

    WordMatcher32 (Node root) {
        int         size = getCodeSize (root);
        
        mCode = new int [size];
        
        compile (root, 0);
    }
    
    public boolean          matches (final byte [] bytes, int offset, int len) {
        int             codeIdx = 0;
        
        for (;;) {
            if (len == 0)
                return (mCode [codeIdx] != 0);
            
            final int   base = mCode [codeIdx + 1];
            final int   jump = bytes [offset] - base;
            
            if (jump < 0)
                return (false);
            
            final int   jtl = mCode [codeIdx + 2];
            
            if (jump >= jtl)
                return (false);
            
            offset++;
            len--;
            
            codeIdx = mCode [codeIdx + 3 + jump];
            
            if (codeIdx == -1)
                return (false);
        }        
    }

    public boolean          matches (CharSequence s) {
        return (matches (s, 0, s.length ()));
    }
    
    public boolean          matches (CharSequence s, int offset, int len) {
        int             codeIdx = 0;
        
        for (;;) {
            if (offset == 0)
                return (mCode [codeIdx] != 0);
            
            final int   base = mCode [codeIdx + 1];
            final int   jump = s.charAt (offset) - base;
            
            if (jump < 0)
                return (false);
            
            final int   jtl = mCode [codeIdx + 2];
            
            if (jump >= jtl)
                return (false);
            
            offset++;
            len--;
            
            codeIdx = mCode [codeIdx + 3 + jump];
            
            if (codeIdx == -1)
                return (false);
        }        
    }

}
