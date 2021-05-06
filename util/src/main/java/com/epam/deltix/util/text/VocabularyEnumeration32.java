package com.epam.deltix.util.text;

import java.util.Enumeration;

class VocabularyEnumeration32 
    implements Enumeration<CharSequence> 
{
    private final StringBuilder mStringBuilder;
    private final int []        mCode;
    private int []              mPath;
    private int []              mPositions;
    private int                 mDepth;

    VocabularyEnumeration32 (int [] code, int maxLength) {
        mCode = code;
        mStringBuilder = new StringBuilder (maxLength);
        mPath = new int [maxLength + 1];
        mPositions = new int [maxLength + 1];
        
        mPath [0] = 0;
        mPositions [0] = -2;
        mDepth = 0;

        findNext ();
    }

    public boolean          hasMoreElements () {
        return mDepth >= 0;
    }

    public CharSequence     nextElement () {
        mStringBuilder.setLength (0);
        
        for (int ii = 0; ii < mDepth; ii++)
            mStringBuilder.append ((char) (mCode [mPath [ii] + 1] + mPositions [ii]));
        
        findNext ();
        
        return (mStringBuilder);
    }

    private void            findNext () {
        for (;;) {
            int         node = mPath [mDepth];
            int         pos = ++mPositions [mDepth];

            if (pos == -1) {
                if (mCode [node] == 1)
                    return;     // Got one
            }
            else if (pos == mCode [node + 2]) { // Up
                mDepth--;

                if (mDepth == -1)
                    break;                
            }
            else {                              //  Down
                int     downNode = mCode [node + pos + 3]; 
                
                if (downNode == -1)
                    continue;
                
                mDepth++;
                mPath [mDepth] = downNode;
                mPositions [mDepth] = -2;
            }
        }
    }
}
