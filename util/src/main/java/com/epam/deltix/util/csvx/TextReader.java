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
package com.epam.deltix.util.csvx;

import java.io.*;
import java.util.regex.*;

import com.epam.deltix.util.collections.*;
import com.epam.deltix.util.collections.generated.*;
import com.epam.deltix.util.io.*;
import com.epam.deltix.util.lang.*;
import com.epam.deltix.util.text.*;

public abstract class TextReader implements Disposable {

	protected String[]                mHeaders;
	protected ByteCountingInputStream mInputStream       = null;
	protected Reader                  mReader;
	protected boolean                 mCloseReader;
	protected String                  mDiagPrefix;

	protected boolean                 mEOF               = false;
	protected int                     mLineNumber        = 1;
	protected StringBuilder           mBuffer            = new StringBuilder ( );
	protected IntegerArrayList        mInclStartIndexes  = new IntegerArrayList ( );
	protected IntegerArrayList        mExclEndIndexes    = new IntegerArrayList ( );
	protected boolean                 mLastCharWasCR     = false;
	protected CharSubSequence         mStockCharSequence = new CharSubSequence ( mBuffer );

	public void readHeaders ( )
	                           throws IOException {
		readHeaders ( false );
	}

	public void readHeaders ( boolean trim )
	                                        throws IOException {
		if (!nextLine ( ))
			throw new EOFException ( mDiagPrefix + "No headers" );

		int numHeaders = getNumCells ( );

		mHeaders = new String[numHeaders];

		for (int ii = 0; ii < numHeaders; ii++)
			mHeaders[ii] = getCell ( ii,
			                         trim ).toString ( );
	}

	public void close ( ) {
		if (mReader != null) {
			if (mCloseReader)
				Util.close ( mReader );

			mReader = null;
			mInclStartIndexes = null;
			mExclEndIndexes = null;
			mBuffer = null;
			mStockCharSequence = null;
		}
	}

	public CharSequence getBuffer ( ) {
		return (mBuffer);
	}

	public abstract boolean nextLine ( )
	                                    throws IOException;

	public String[] getHeaders ( ) {
		return (mHeaders);
	}

	public int getHeaderIndex ( String hdr ) {
		if (mHeaders == null)
			throw new IllegalStateException ( "readHeaders () has not been called" );

		return (Util.indexOf ( mHeaders,
		                       hdr ));
	}

	public int getHeaderIndexEx ( String hdr )
	                                          throws IOException {
		int idx = getHeaderIndex ( hdr );

		if (idx < 0)
			throw new IOException ( "Required header '" + hdr + "' is missing." );

		return (idx);
	}

	public int getNumCells ( ) {
		return (mInclStartIndexes.size ( ));
	}

	public int getLineNumber ( ) {
		return (mLineNumber);
	}

	public boolean getCloseReader ( ) {
		return mCloseReader;
	}

	public void setCloseReader ( boolean closeReader ) {
		this.mCloseReader = closeReader;
	}

	public String getDiagPrefix ( ) {
		return mDiagPrefix;
	}

	public void setDiagPrefix ( String diagPrefix ) {
		this.mDiagPrefix = diagPrefix;
	}

	public String getDiagPrefixWithLineNumber ( ) {
		return (mDiagPrefix + mLineNumber + ": ");
	}

	public boolean cellMatches ( int idx,
	                             Matcher regex ) {
		regex.reset ( getCell ( idx ) );
		return (regex.matches ( ));
	}

	public boolean cellMatches ( int idx,
	                             Pattern regex ) {
		return (regex.matcher ( getCell ( idx ) ).matches ( ));
	}

	public boolean cellContains ( int idx,
	                              String text ) {
		return (cellContains ( idx,
		                       text,
		                       false ));
	}

	public boolean cellContains ( int idx,
	                              String text,
	                              boolean trimCell ) {
		return (Util.equals ( text,
		                      getCell ( idx,
		                                trimCell ) ));
	}

	public CharSequence getCell ( int idx ) {
		return (getCell ( idx,
		                  false ));
	}

	public CharSequence getCell ( int idx,
	                              boolean trim ) {
		if (idx < 0 || idx >= mInclStartIndexes.size ( ))
			return (EmptyCharSequence.INSTANCE);

		mStockCharSequence.start = mInclStartIndexes.getIntegerNoRangeCheck ( idx );
		mStockCharSequence.end = mExclEndIndexes.getIntegerNoRangeCheck ( idx );

		if (trim)
			mStockCharSequence.trimWhitespace ( );

		return (mStockCharSequence);
	}

	public void getCell (int idx, CharSubSequence out) {
		if (idx < 0 || idx >= mInclStartIndexes.size ( ))
			out.set (null, 0, 0);
        else
            out.set (
                mBuffer, 
                mInclStartIndexes.getIntegerNoRangeCheck ( idx ),
                mExclEndIndexes.getIntegerNoRangeCheck ( idx )
            );
	}

	public String getString ( int idx,
	                          boolean trim ) {
		try{
		return (getCell ( idx,
		                  trim ).toString ( ));
		}
		catch(Throwable x){
			return "";
		}
	}

	public String getString ( int idx ) {
		return (getString ( idx,
		                    false ));
	}

	public String getSubstring ( int idx,
	                             int begin,
	                             int end ) {
		return (getString ( idx ).substring ( begin,
		                                      end ));
	}

	public double getDouble ( int idx ) {
		return (CharSequenceParser.parseDouble ( getCell ( idx,
		                                                   true ) ));
	}

    public double getDoubleOrNaN ( int idx ) {
        return (getDoubleOrNaN (idx, true));
    }
    
    public double getDoubleOrNaN ( int idx, boolean trim ) {  
        return (getDoubleOrDefault (idx, trim, Double.NaN));
    }
    
    public double getDoubleOrDefault ( int idx, boolean trim, double defval ) {        
        CharSequence cell = getCell ( idx,  trim );
        
        if (cell.length() == 0)
            return defval;
        
        return (CharSequenceParser.parseDouble ( cell ));
    }

	public double getDoubleEx ( int idx ) {
		return (Double.parseDouble ( getString ( idx,
		                                         true ) ));
	}

	public float getFloat ( int idx ) {
		return (CharSequenceParser.parseFloat ( getCell ( idx,
		                                                  true ) ));
	}

	public int getInt ( int idx ) {
		return (CharSequenceParser.parseInt ( getCell ( idx,
		                                                true ) ));
	}

	public int getIntOrDefault ( int idx, boolean trim, int defval ) {
        CharSequence cell = getCell ( idx,  trim );
        
        if (cell.length() == 0)
            return defval;
        
        return (CharSequenceParser.parseInt ( cell ));         
	}

	public long getLong ( int idx ) {
		return (CharSequenceParser.parseLong ( getCell ( idx,
		                                                 true ) ));
	}

	public long getLongOrDefault ( int idx, boolean trim, long defval ) {
        CharSequence cell = getCell ( idx,  trim );
        
        if (cell.length() == 0)
            return defval;
        
        return (CharSequenceParser.parseLong ( cell ));         
	}

	public void setIndexFromHeaders ( ColumnDescriptor... cds ) {
		for (ColumnDescriptor cd : cds)
			if (!cd.hasConstantValue ( ) && !cd.findIndexFromHeaders ( mHeaders ))
				throw new RuntimeException ( mDiagPrefix + "1: Header '" + cd.getHeader ( ) + "' was not found" );
	}

}