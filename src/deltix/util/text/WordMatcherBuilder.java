package deltix.util.text;

import deltix.util.Util;
import java.util.*;

/**
 *  Builds a decision tree for matching words from the given set.
 *  This matches about 13,600 times faster than java regular expressions.
 */
public class WordMatcherBuilder implements WordMatcher {
    private static final int        INIT_CAPACITY = 32;
    
    private static class Node {
        private boolean         endOk = false;
        private char            base;
        private short           length = 0;
        private Node []         branches = null;
        
        void            dump (String indent) {
            if (endOk)
                System.out.println (indent + "<end>");
            
            if (length > 0) {
                String      indent2 = indent + " ";

                for (int ii = 0; ii < length; ii++) {
                    Node    branch = branches [ii];
                    
                    if (branch != null) {
                        System.out.println (indent + "'" + (char) (base + ii) + "':");
                        branch.dump (indent2);
                    }
                }
            }
        }
        
        private void    appendSpace (int num) {
            int             curArrayLength = branches.length;
            int             newLength = length + num;
            
            if (curArrayLength < newLength) {
                Node []     more = 
                    new Node [Util.doubleUntilAtLeast (curArrayLength, newLength)];
                
                System.arraycopy (branches, 0, more, 0, length);
                branches = more;                
            }             
            
            length = (short) newLength;
        }
        
        private void    prependSpace (int num) {
            int             curArrayLength = branches.length;
            int             newLength = length + num;
                
            if (curArrayLength >= newLength) {
                System.arraycopy (branches, 0, branches, num, length);  
                Arrays.fill (branches, 0, num, null);
            }
            else {
                Node []     more = 
                    new Node [Util.doubleUntilAtLeast (curArrayLength, newLength)];
                
                System.arraycopy (branches, 0, more, num, length);
                branches = more;                
            }                
            
            length = (short) newLength;
        }
                
        boolean         match (byte [] bytes, int offset, int len) {
            if (len == 0) 
                return (endOk);
            
            if (length == 0)
                return (false);
            
            int     idx = bytes [offset] - base;
            
            if (idx < 0 || idx >= length)
                return (false);
            
            return (branches [idx].match (bytes, offset + 1, len - 1));
        }
        
        boolean         match (CharSequence s, int pos) {
            if (pos == s.length ()) 
                return (endOk);
            
            if (length == 0)
                return (false);
            
            int     idx = s.charAt (pos) - base;
            
            if (idx < 0 || idx >= length)
                return (false);
            
            return (branches [idx].match (s, pos + 1));
        }
        
        void            add (CharSequence s, int pos) {
            if (pos == s.length ()) {
                endOk = true;
                return;
            }
            
            char        ch = s.charAt (pos);
            int         idx;
            
            if (length == 0) {
                branches = new Node [INIT_CAPACITY];
                base = ch;
                idx = 0;
                length = 1;
            }
            else {
                idx = ch - base;
                
                if (idx >= 0) {
                    int     ext = idx + 1 - length;
                    
                    if (ext > 0)
                        appendSpace (ext);
                }
                else {
                    base += idx;
                    prependSpace (-idx);
                    idx = 0;
                }
            }
            
            Node        branch = branches [idx];
            
            if (branch == null) 
                branches [idx] = branch = new Node ();
                                
            branch.add (s, pos + 1);
        }
        
        int         getCodeSize () {
            int         size = 3 + length;
            
            for (int ii = 0; ii < length; ii++) {
                Node    branch = branches [ii];

                if (branch != null)
                    size += branch.getCodeSize ();
            }
            
            return (size);
        }
        
        int         buildCode32 (int [] code, int offset) {
            code [offset++] = endOk ? 1 : 0;
            code [offset++] = base;
            code [offset++] = length;
            
            int         endOffset = offset + length;
            
            for (int ii = 0; ii < length; ii++) {
                Node    branch = branches [ii];

                if (branch != null) {
                    code [offset++] = endOffset;
                    endOffset = branch.buildCode32 (code, endOffset);
                }
                else
                    code [offset++] = -1;
            }
            
            return (endOffset);
        }
        
        int         buildCode16 (short [] code, int offset) {
            code [offset++] = (short) (endOk ? 1 : 0);
            code [offset++] = (short) base;
            code [offset++] = length;
            
            int         endOffset = offset + length;
            
            for (int ii = 0; ii < length; ii++) {
                Node    branch = branches [ii];

                if (branch != null) {
                    code [offset++] = (short) endOffset;
                    endOffset = branch.buildCode16 (code, endOffset);
                }
                else
                    code [offset++] = -1;
            }
            
            return (endOffset);
        }
    }
    
    private Node        mRoot = new Node ();
    
    public WordMatcherBuilder () {
    }
    
    public void             add (CharSequence s) {
        mRoot.add (s, 0);
    } 
    
    public void             dump () {
        mRoot.dump ("");
    }
    
    public WordMatcher      compile () {
        return (compile (false));
    }
    
    public WordMatcher      compile (boolean speedOverSize) {
        int     size = mRoot.getCodeSize ();
        
        if (speedOverSize || size > 0xFFFF) {
            int []  code = new int [size];
            int     size2 = mRoot.buildCode32 (code, 0);

            assert size == size2;

            return (new WordMatcher32 (code));
        }
        else {
            short [] code = new short [size];
            int     size2 = mRoot.buildCode16 (code, 0);

            assert size == size2;

            return (new WordMatcher16 (code));            
        }
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          matches (CharSequence s) {
        return (mRoot.match (s, 0));
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          matches (byte [] bytes, int offset, int length) {
        return (mRoot.match (bytes, offset, length));
    }
    

}
