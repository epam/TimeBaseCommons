package deltix.util.collections;

import deltix.util.Util;
import java.io.PrintWriter;
import java.util.*;

/**
 *  Builds a decision tree for matching words from the given set.
 */
public class WordMatcher {
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
        
        void            printJava (
            PrintWriter         pwr,
            String              indent,
            String              varName,
            int                 pos
        )
        {
            pwr.printf ("%sif (%s_length == %d) return (%b);\n", indent, varName, pos, endOk);
            
            if (length > 0) {
                String          indent2 = indent + "  ";
                
                pwr.printf ("%sswitch (%s.charAt (%d) - %d) {\n", indent, varName, pos, (int) base);

                for (int ii = 0; ii < length; ii++) {
                    Node    branch = branches [ii];
                    
                    if (branch == null)
                        continue;
                        
                    pwr.printf ("%s case %d: {\n", indent, ii);
                    branch.printJava (pwr, indent2, varName, pos + 1);
                    pwr.printf ("%s }\n", indent);
                }

                pwr.printf ("%s}\n", indent);
            }
            
            pwr.printf ("%sreturn false;\n", indent);
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
    }
    
    private Node        mRoot = new Node ();
    
    public WordMatcher () {
    }
    
    public void         add (CharSequence s) {
        mRoot.add (s, 0);
    } 
    
    public void         dump () {
        mRoot.dump ("");
    }
    
    public void         printJavaMethod (
        PrintWriter         pwr,
        String              indent,
        String              access,
        String              name
    )
    {
        pwr.printf ("%s%s static boolean %s (CharSequence cs) {\n", indent, access, name);
        printJava (pwr, indent + " ", "cs");
        pwr.printf ("%s}\n", indent);
    }
    
    public void         printJava (
        PrintWriter         pwr,
        String              indent,
        String              varName
    )
    {
        pwr.printf ("%sfinal int %s_length = %<s.length ();\n", indent, varName);
        mRoot.printJava (pwr, indent, varName, 0);
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean      match (CharSequence s) {
        return (mRoot.match (s, 0));
    }
    
    public static void main (String [] args) {
        WordMatcher     wm = new WordMatcher ();
        
        wm.add ("XY");
        wm.add ("ABC");
        wm.add ("B");
        wm.add ("ABD");
        
        wm.printJavaMethod (new PrintWriter (System.out, true), "", "public", "test");       
    }
}
