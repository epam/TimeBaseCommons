/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.collections;

import com.epam.deltix.util.memory.MemorySizeEstimator;

import java.util.Enumeration;

/**
 *	The quickest possible implementation of a bi-directional link list.
 *	Requires that all elements extend the static nested class Entry.
 *	This allows to avoid entry wrapper object creation, characteristic
 *	to java.util.LinkedList. Entries cannot be shared between two or more
 *	QuickLists. This class is not synchronized.
 */
public class QuickList <T extends QuickList.Entry> implements java.io.Serializable {
    /**
     *  Turn this on if problems are suspected in the use of this class
     */
    public static final boolean DO_ASSERTIONS = false;

	public static class BadEntryException extends RuntimeException {
		public BadEntryException (Entry e) {
			super (
				"Next and previous pointers of entry " +
				e + " are in an inconsistent state."
			);
		}
	}
	
	public static final class EntryEnumeration <T extends QuickList.Entry <T>> implements Enumeration <T> {
		private Entry		mCur;
		
		public EntryEnumeration (Entry entry) {
			mCur = entry;
		}
		
        @Override
		public boolean		hasMoreElements () {
			return (!(mCur instanceof BoundaryEntry));
		}
		
		@SuppressWarnings("unchecked") @Override
        public T		nextElement () {
			T				e = (T) mCur;
			mCur = mCur.next ();
			return (e);
		}
	}
	
	public static abstract class Entry <T extends Entry <T>> implements java.io.Serializable {
    	protected Entry       mPrevious = null;
    	protected Entry       mNext = null;
    	
    	/** 
    	 *	Unlinks the entry from the list it is in. This operation
    	 *	is illegal on entries that have previously been unlinked from
    	 *	a list, and will cause chain corruption. Entries are
    	 *	not run-time protected against double-unlinking, 
    	 *	because we are trying to make this operation as fast
    	 *	as computerly possible. 
    	 */
    	public final void	unlink () {
            if (DO_ASSERTIONS) {
                if (mNext == null || mPrevious == null)
                    throw new RuntimeException (this + " is not linked.");
            }
            
    		mNext.mPrevious = mPrevious;
    		mPrevious.mNext = mNext;
            
            if (DO_ASSERTIONS) 
                mNext = mPrevious = null;                
    	}
    	
    	/** 
    	 *	Unlinks the entry from the list it is in. 
    	 *
    	 *	@exception BadEntryException
    	 *			When the entry is in an illegal state.
    	 *
    	 *	@return Whether the entry has just been safely unlinked,
    	 *			<code>false</code> if it has been safely unlinked before.
    	 */
    	public boolean		safeUnlink () {
    		if (mPrevious == null && mNext == null)
    			return (false);
    			
    		if (mPrevious != null && mNext != null) {
    			unlink ();
    			mPrevious = null;
    			mNext = null;
    			return (true);
    		}
    		
    		throw new BadEntryException (this);
    	}
    	
    	/** 
    	 *	Returns the next entry in the list, or <code>null</code> 
    	 *	if this is the last one.
    	 */
    	@SuppressWarnings("unchecked") public final T		next () {
            if (DO_ASSERTIONS) {
                if (mNext == this)
                    throw new RuntimeException (this + ": mNext == this");
                
                if (mPrevious == this)
                    throw new RuntimeException (this + ": mPrevious == this");
            }
            
    		if (mNext instanceof BoundaryEntry)
    			return (null);
    		else
    			return ((T) mNext);
    	}
    	
    	/** 
    	 *	Returns the previous entry in the list, or <code>null</code> 
    	 *	if this is the first one.
    	 */
    	@SuppressWarnings("unchecked") public final T		previous () {
            if (DO_ASSERTIONS) {
                if (mNext == this)
                    throw new RuntimeException (this + ": mNext == this");
                
                if (mPrevious == this)
                    throw new RuntimeException (this + ": mPrevious == this");
            }
            
    		if (mPrevious instanceof BoundaryEntry)
    			return (null);
    		else
    			return ((T) mPrevious);
    	}
    	
    	/** 
    	 *	Returns the next entry in the list, or the tail
    	 *	<code>BoundaryEntry</code>, if this is the last one.
    	 */
    	public final Entry		nextOrBoundary () {
            if (DO_ASSERTIONS) {
                if (mNext == this)
                    throw new RuntimeException (this + ": mNext == this");
                
                if (mPrevious == this)
                    throw new RuntimeException (this + ": mPrevious == this");
            }
            
    		return (mNext);
    	}
    	
    	/** 
    	 *	Returns the previous entry in the list, or the head
    	 *	<code>BoundaryEntry</code>, if this is the first one.
    	 */
    	public final Entry		previousOrBoundary () {
            if (DO_ASSERTIONS) {
                if (mNext == this)
                    throw new RuntimeException (this + ": mNext == this");
                
                if (mPrevious == this)
                    throw new RuntimeException (this + ": mPrevious == this");
            }
            
    		return (mPrevious);
    	}
	}

	/**
	 *	Used to bound the entry chain. Each QuickList contains one
	 *	BoundaryEntry in the beginning, and one BoundaryEntry in the end
	 *	of the chain.
	 */
	public static final class BoundaryEntry extends Entry {
        @Override
    	public boolean		safeUnlink () {
    		throw new IllegalArgumentException (
    			"Cannot unlink a BoundaryEntry."
    		);
    	}
	}
	
	private final BoundaryEntry		mHead;
	private final BoundaryEntry		mTail;
	
    public static final int            MEMORY_SIZE =
        3 * MemorySizeEstimator.OBJECT_OVERHEAD +
        3 * MemorySizeEstimator.SIZE_OF_POINTER;
        
	/**
	 *	Creates an empty list.
	 */
	public QuickList () {
		mHead = new BoundaryEntry ();
		mTail = new BoundaryEntry ();
		
		mHead.mNext = mTail;
		mTail.mPrevious = mHead;
	}
	
	public final boolean		isEmpty () {
		return (mHead.mNext == mTail);
	}
	
	/**
	 *	Returns the head instance of BoundaryEntry.
	 */
	public final BoundaryEntry	getHeadBoundaryEntry () {
		return (mHead);
	}
	
	/**
	 *	Returns the tail instance of BoundaryEntry.
	 */
	public final BoundaryEntry	getTailBoundaryEntry () {
		return (mTail);
	}
	
	/**
	 *	Returns the first entry without unlinking it
	 *	from the list, or <code>null</code> if the list is empty.
	 */
	@SuppressWarnings("unchecked") public final T        getFirst () {
		Entry			first = mHead.mNext;
		
		return (first == mTail ? null : (T) first);
	}
	
	/**
	 *	Returns the first entry without unlinking it
	 *	from the list, or the tail boundary entry if the list is empty.
	 */
	public final Entry	getFirstOrTail () {
		return (mHead.mNext);
	}
	
	/**
	 *	Returns the last entry without unlinking it
	 *	from the list, or <code>null</code> if the list is empty.
	 */
	@SuppressWarnings("unchecked") public final T        getLast () {
		Entry			last = mTail.mPrevious;
		
		return (last == mHead ? null : (T) last);
	}
	
	/**
	 *	Returns the last entry without unlinking it
	 *	from the list, or the head boundary entry if the list is empty.
	 */
	public final Entry	getLastOrHead () {
		return (mTail.mPrevious);
	}
	
	/**
	 *	Unsafely unlinks all entries (i.e. the unlinked entries have
	 *	no knowledge of what just happened and continue to point to their
	 *	former neighbors).
	 */
	public final void		clear () {
        if (DO_ASSERTIONS) {
            while (mHead.mNext != mTail)
                mHead.mNext.unlink ();
        }
        else
            unlinkBetweenExclusive (mHead, mTail);
	}
	
	/**
	 *	Unsafely unlinks all entries between, but excluding, 
	 *	<code>previous</code> and <code>next</code>
	 *	(i.e. the unlinked entries have
	 *	no knowledge of what just happened and continue to point to their
	 *	former neighbors). No checks are made that 
	 *	<code>previous</code> and <code>next</code> are, indeed, in
	 *	the same list and that they are there in the correct order.
	 */
	public static void	unlinkBetweenExclusive (
		Entry				previous, 
		Entry				next
	)
	{
        if (DO_ASSERTIONS) {
            previous.mNext.mPrevious = null;
            next.mPrevious.mNext = null;
        }
        
		previous.mNext = next;
		next.mPrevious = previous;
	}
	
	/**
	 *	Links a chain of entries between two specified entries.
	 *	If there have been any entries between the two specified entries,
	 *	they are unsafely unlinked hereby (i.e. the unlinked entries have
	 *	no knowledge of what just happened and continue to point to their
	 *	former neighbors).
	 */
	public static void		linkChainBetween (
		Entry			previous, 
		Entry			firstInChain, 
		Entry			lastInChain,
		Entry			next 
	) 
	{
        if (DO_ASSERTIONS) {
            if (previous.mNext != next) {
                previous.mNext.mPrevious = null;
                next.mPrevious.mNext = null;
            }
            
            if (firstInChain.mPrevious != null)
                throw new RuntimeException (
                    firstInChain + " was not properly unlinked: mPrevious == " + 
                    firstInChain.mPrevious
                );

            if (lastInChain.mNext != null)
                throw new RuntimeException (
                    lastInChain + " was not properly unlinked: mNext == " + 
                    lastInChain.mNext
                );
        }
        
		previous.mNext = firstInChain;
		firstInChain.mPrevious = previous;
		lastInChain.mNext = next;
		next.mPrevious = lastInChain;
	}
	
	/**
	 *	Links an entire other list between two specified entries.
	 *	The other list is made invalid by this call and should not be used
	 *	unless clear'ed beforehand.
	 *	If there have been any entries between the two specified entries,
	 *	they are unsafely unlinked hereby (i.e. the unlinked entries have
	 *	no knowledge of what just happened and continue to point to their
	 *	former neighbors).
	 */
	public static void		linkBetween (
		Entry			previous, 
		QuickList		l,
		Entry			next 
	) 
	{
		if (!l.isEmpty ()) 
			linkChainBetween (previous, l.mHead.mNext, l.mTail.mPrevious, next);
	}
	
	/**
	 *	Links a chain of entries at the head of the list.
	 */
	public final void		linkChainFirst (Entry firstInChain, Entry lastInChain) {
		linkChainBetween (mHead, firstInChain, lastInChain, mHead.mNext);
	}
	
	/**
	 *	Links a chain of entries at the tail of the list.
	 */
	public final void		linkChainLast (Entry firstInChain, Entry lastInChain) {
		linkChainBetween (mTail.mPrevious, firstInChain, lastInChain, mTail);
	}
	
	/**
	 *	Links a chain of entries after the specified one.
	 */
	public static void	linkChainAfter (Entry previous, Entry firstInChain, Entry lastInChain) {
		linkChainBetween (previous, firstInChain, lastInChain, previous.mNext);
	}
	
	/**
	 *	Links a chain of entries before the specified one.
	 */
	public static void	linkChainBefore (Entry firstInChain, Entry lastInChain, Entry next) {
		linkChainBetween (next.mPrevious, firstInChain, lastInChain, next);
	}
	
	/**
	 *	Links the entry at the head of the list.
	 */
	public final void		linkFirst (T e) {
		linkChainFirst (e, e);
	}
	
	/**
	 *	Links the entry at the tail of the list.
	 */
	public final void		linkLast (T e) {
		linkChainLast (e, e);
	}
	
	/**
	 *	Links the entry after the specified one.
	 */
	public static void	linkAfter (Entry previous, Entry e) {
		linkChainAfter (previous, e, e);
	}
	
	/**
	 *	Links the entry before the specified one.
	 */
	public static void	linkBefore (Entry e, Entry next) {
		linkChainBefore (e, e, next);
	}
	
	/**
	 *	Links an entire other list at the head of the list.
	 *	The other list is made invalid by this call and should not be used
	 *	unless clear'ed beforehand.
	 */
	public final void		linkFirst (QuickList l) {
		if (!l.isEmpty ())
			linkChainFirst (l.mHead.mNext, l.mTail.mPrevious);
	}
	
	/**
	 *	Links an entire other list at the tail of the list.
	 *	The other list is made invalid by this call and should not be used
	 *	unless clear'ed beforehand.
	 */
	public final void		linkLast (QuickList l) {
		if (!l.isEmpty ())
			linkChainLast (l.mHead.mNext, l.mTail.mPrevious);
	}
	
	/**
	 *	Links an entire other list after the specified entry.
	 *	The other list is made invalid by this call and should not be used
	 *	unless clear'ed beforehand. The specified entry should be part of another
	 *	list, which by the virtue of calling this method is inserted into.
	 */
	public static void		linkAfter (Entry previous, QuickList l) {
		if (!l.isEmpty ()) 
			linkChainAfter (previous, l.mHead.mNext, l.mTail.mPrevious);
	}
	
	/**
	 *	Links an entire other list before the specified entry.
	 *	The other list is made invalid by this call and should not be used
	 *	unless clear'ed beforehand. The specified entry should be part of another
	 *	list, which by the virtue of calling this method is inserted into.
	 */
	public static void		linkBefore (QuickList l, Entry next) {
		if (!l.isEmpty ()) 
			linkChainBefore (l.mHead.mNext, l.mTail.mPrevious, next);
	}
	
	/**
	 *	Returns an enumeration of all entries.
	 */
	@SuppressWarnings("unchecked") public final Enumeration <T>		entries () {
		return (new EntryEnumeration (mHead.mNext));
	}

	public boolean contains (Entry entry) {
		Entry e = getFirst();
		while (e != null) {
			if (e == entry)
				return true;
			e = e.next();
		}
		return false;
	}

    @SuppressWarnings("unchecked")
    public final int              size() {
        if (isEmpty())
            return 0;
        T first = getFirst();
        int count = 0;
        while (first != null) {
            count++;
            first = (T) first.next();
        }

        return count;
    }
}