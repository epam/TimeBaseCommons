package deltix.util.text;

import deltix.util.Util;
import java.util.*;

/**
 *  Builds a decision tree for matching words from the given set.
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
            int         size = 1 + length;
            
            for (int ii = 0; ii < length; ii++) {
                Node    branch = branches [ii];

                if (branch != null)
                    size += branch.getCodeSize ();
            }
            
            return (size);
        }
        
        int         buildCode (int [] code, int offset) {
            int         header = 
                base | (length << 16);
            
            if (endOk)
                header |= 0x80000000;
            
            code [offset++] = header;
            
            int         endOffset = offset + length;
            
            for (int ii = 0; ii < length; ii++) {
                Node    branch = branches [ii];

                if (branch != null) {
                    code [offset + ii] = endOffset;
                    endOffset = branch.buildCode (code, endOffset);
                }
                else
                    code [offset + ii] = -1;
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
        int     size = mRoot.getCodeSize ();
        int []  code = new int [size];
        int     size2 = mRoot.buildCode (code, 0);
        
        assert size == size2;
        
        return (new WordMatcher32 (code));
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          match (CharSequence s) {
        return (mRoot.match (s, 0));
    }
    
    /*
    public static void      main (String [] args) throws Exception {
        WordMatcherBuilder     wm = new WordMatcherBuilder ();
        
        BufferedReader  rd = 
            new BufferedReader (new FileReader (Home.get () + "/src/deltix/custom/forthill/algorithms/option-master.rpt"));
        
        OptionMaster    om = new OptionMaster (rd);
        
        rd.close ();
        
        for (OptionMaster.Record r : om.records ()) {
            wm.add (r.root);
        }
        
        WordMatcher         code = wm.compile ();
        
        System.out.println (code.match ("DBD"));
        System.out.println (code.match ("DIA"));
        System.out.println (wm.match ("DBD"));
        System.out.println (wm.match ("DIA"));
        //wm.printJavaMethod (new PrintWriter (System.out, true), "", "public", "test");       
    }
     */
}
