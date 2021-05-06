package com.epam.deltix.util.text;

/**
 *	Edit Distance between two strings:
 *<pre>
 *d ("", "") = 0
 *d ("", s) = s.length ()
 *d (s, "") = s.length ()
 *d (s1 + ch1, s2 + ch2) =
 *    min (
 *        d (s1, s2) + (ch1 == ch2 ? 0 : 1),
 *        d (s1 + ch1, s2) + 1,
 *        d (s1, s2 + ch2) + 1
 *    )
 *</pre>
 */
public class EditDistance {
    public static CharSequence  bestMatch (CharSequence test, CharSequence ... references) {
        if (references == null)
            return (null);
        
        int             ed = Integer.MAX_VALUE;
        CharSequence    best = null;

        for (CharSequence cs : references) {
            int         cur = editDistance (test, cs);

            if (cur < ed) {
                ed = cur;
                best= cs;
            }
        }

        return (best);
    }

    public static int           editDistance (CharSequence s1, CharSequence s2) {
    	int			l1 = s1.length ();
    	int			l2 = s2.length ();
		int [][]	mtx = new int [l1 + 1][l2 + 1];

		mtx [0][0] = 0;

		for (int j = 1; j <= l2; j++)
    		mtx [0][j] = mtx [0][j - 1] + 1;

		for (int i = 1; i <= l1; i++) {
    		mtx [i][0] = mtx [i - 1][0] + 1;

    		for (int j = 1; j <= l2; j++) {
    			int		diag = mtx [i - 1][j - 1];

        		if (s1.charAt (i - 1) != s2.charAt (j - 1))
        			diag = diag + 1;

        		mtx [i][j] =
        			Math.min (
        				diag,
                		Math.min (
                			mtx [i - 1][j] + 1,
                            mtx [i][j - 1] + 1
                        )
                    );
    		}
    	}

		return mtx [l1][l2];
    }

    public static void main (String [] args) {
    	String		s1 = args [0];
    	String		s2 = args [1];

    	System.out.println (
    		"d ('" + s1 + "', '" + s2 + "') =     " +
    		editDistance (s1, s2)
    	);

    }
}
