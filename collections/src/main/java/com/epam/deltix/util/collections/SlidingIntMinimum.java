package com.epam.deltix.util.collections;

/**
 *
 */
public final class SlidingIntMinimum extends SlidingExtremeBase {
    private final int []    minima;
    private int             currentValue;
        
    public SlidingIntMinimum (int windowWidth) {
        super (windowWidth);
        
        minima = new int [windowWidth];
    }

    @Override
    protected boolean   isCurrentValueNoLessExtremeThanAt (int idx) {
        return (currentValue <= minima [idx]);
    }
    
    public int          getMinimum () {
        return (minima [getIndexOfExtreme ()]);
    }
    
    public int          offer (int value) {
        currentValue = value;
        
        minima [onOffer ()] = value;
        
        return (getMinimum ());
    }

    @Override
    protected String    printValueAt (int idx) {
        return (String.valueOf (minima [idx]));
    }        
}
