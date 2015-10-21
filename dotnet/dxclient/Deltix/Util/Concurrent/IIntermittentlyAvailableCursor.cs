using System;


namespace Deltix.Util.Concurrent
{
	[System.Obsolete("Not used")]
    public enum NextResult
    {
        OK,
        END_OF_CURSOR,
        UNAVAILABLE
    }

	[System.Obsolete("Not used")]
    public interface IIntermittentlyAvailableCursor
    {
        /**
     *  Returns <code>true</code> if the last call to <code>next ()</code> returned <code>false</code>.
     *  Returns <code>false</code> if <code>next ()</code> has not been called yet.
     *  This method is legal to call any number of times at any
     *  point in the cursor's lifecycle.
     */
        Boolean IsAtEnd();
        /**
     *  Moves on to the next data element. This method blocks until 
     *  the next element becomes available, or until the cursor is
     *  determined to be at the end of the sequence. this method is illegal to
     *  call if <code>isAtEnd ()</code> returns <code>true</code>.
     *  
     *  @return     <code>false</code> if at the end of the cursor.
     */
        Boolean Next();
        NextResult NextIfAvailable();
    }
}