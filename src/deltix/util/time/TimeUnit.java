package deltix.util.time;

/**
 *
 */
public enum TimeUnit {
    MILLISECOND,
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    QUARTER,
    YEAR;
    
    /**
     *  Returns the size of this time unit in milliseconds.
     *  @exception UnsupportedOperationException     If the unit is variable size.
     */
    public long         getSizeInMilliseconds () {
        switch (this) {
            case MILLISECOND:   return (1);
            case SECOND:        return (1000);
            case MINUTE:        return (60000);
            case HOUR:          return (3600000);
            case DAY:           return (86400000);
            case WEEK:          return (604800000);
            default:            
                throw new UnsupportedOperationException (
                    this + " is a variable size unit"
                );
        }
    }
    
    /**
     *  Returns the size of this time unit in milliseconds.
     *  @exception UnsupportedOperationException     If the unit is variable size.
     */
    public int              getSizeInMonths () {
        switch (this) {
            case MONTH:         return (1);
            case QUARTER:       return (3);
            case YEAR:          return (12);
            default:            
                throw new UnsupportedOperationException (
                    this + " is not a month-based unit"
                );
        }
    }
    
    /**
     *  Returns whether this is a variable-size unit.
     */
    public boolean          isFixedSize () {
        return (!isVariableSize ());
    }
    
    /**
     *  Returns whether this is a variable-size unit.
     */
    public boolean          isVariableSize () {
        switch (this) {
            case MONTH:
            case QUARTER:
            case YEAR:
                return (true);
            default:            
                return (false);
        }
    }
    
    /**
     *  Returns the largest unit by which the specified size is divisible.
     *  @param size     Interval size in milliseconds.
     *  @return         The largest time unit by which the size is divisible.
     */
    public static TimeUnit  getUnitForMonths (int numMonths) {
        if (numMonths % 12 == 0)
            return (YEAR);
        
        if (numMonths % 3 == 0)
            return (QUARTER);
        
        return (MONTH);
    }
    
    /**
     *  Returns the largest unit by which the specified size is divisible.
     *  @param size     Interval size in milliseconds.
     *  @return         The largest time unit by which the size is divisible.
     */
    public static TimeUnit  getUnitForMilliseconds (long size) {
        TimeUnit []     v = values ();
        
        for (int ii = WEEK.ordinal (); ii > 0; ii--) {
            TimeUnit    tu = v [ii];
            
            if (size % tu.getSizeInMilliseconds () == 0)
                return (tu);
        }
                
        return (MILLISECOND);
    }
    
    /**
     *  Returns the uppercase suffix used to represent the interval in "short" form.
     */
    public char             getSuffix () {
        switch (this) {
            case MILLISECOND:   return ('X');
            case SECOND:        return ('S');
            case MINUTE:        return ('I');
            case HOUR:          return ('H');
            case DAY:           return ('D');
            case WEEK:          return ('W');
            case MONTH:         return ('M');
            case QUARTER:       return ('Q');
            case YEAR:          return ('Y');
            default:            
                throw new UnsupportedOperationException (
                    this + " is a variable size unit"
                );
        }
    }
    
    public static TimeUnit  fromSuffix (char suffix) {
        switch (suffix) {
            case 'X':   return MILLISECOND;
            case 'S':   return SECOND;
            case 'I':   return MINUTE;
            case 'H':   return HOUR;
            case 'D':   return DAY;
            case 'W':   return WEEK;
            case 'M':   return MONTH;
            case 'Q':   return QUARTER;
            case 'Y':   return YEAR;
            default:    
                throw new IllegalArgumentException (suffix + " is illegal.");
        }
    }
}
