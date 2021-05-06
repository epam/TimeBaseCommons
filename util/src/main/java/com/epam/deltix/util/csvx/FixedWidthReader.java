package com.epam.deltix.util.csvx;

import java.io.*;
import java.util.*;

import com.epam.deltix.util.collections.*;
import com.epam.deltix.util.collections.generated.*;
import com.epam.deltix.util.io.*;
import com.epam.deltix.util.lang.*;

public class FixedWidthReader extends TextReader {

	public FixedWidthReader ( Reader rd,
	                          int[] positions,
	                          boolean closeReader,
	                          String diagPrefix ) {
		mReader = rd;
		mCloseReader = closeReader;
		mDiagPrefix = diagPrefix;
		initIndexArrays ( positions );
	}

	public FixedWidthReader ( InputStream is,
	                          int[] positions,
	                          String diagPrefix ) {
		this ( is,
		        positions,
		        true,
		        diagPrefix );
	}

	public FixedWidthReader ( InputStream is,
	                          int[] positions,
	                          boolean closeReader,
	                          String diagPrefix ) {
		mInputStream = new ByteCountingInputStream ( is );
		mReader = new InputStreamReader ( mInputStream );
		mCloseReader = closeReader;
		mDiagPrefix = diagPrefix;
		initIndexArrays ( positions );
	}

	public FixedWidthReader ( InputStream is,
	                          int[] positions ) {

		initIndexArrays ( positions );

		mInputStream = new ByteCountingInputStream ( is );
		mReader = new InputStreamReader ( mInputStream );
	}

	public FixedWidthReader ( File f,
	                          int[] positions )
	                                           throws IOException {
		this ( new BufferedInputStream ( new FileInputStream ( f ) ),
		        positions );
	}

	private void initIndexArrays ( int[] positions ) {

		mInclStartIndexes = new IntegerArrayList ( );
		mExclEndIndexes = new IntegerArrayList ( );

		int start = 0;
		if (positions != null) {
			Arrays.sort ( positions );
			for (int pos : positions) {
				mInclStartIndexes.add ( start );
				mExclEndIndexes.add ( pos );
				start = pos;
			}
		}
		mInclStartIndexes.add ( start );
	}

	@Override
	public void close ( ) {
		if (mReader != null) {
			if (mCloseReader)
				Util.close ( mReader );

			mReader = null;
			mBuffer = null;
			mStockCharSequence = null;
		}
	}

	@Override
	public boolean nextLine ( )
	                           throws IOException {
		if (mEOF)
			return (false);

		mBuffer.setLength ( 0 );

		for (;;) {
			int ch = mReader.read ( );

			if (mLastCharWasCR && ch == 10)
				continue;
			mLastCharWasCR = ch == 13;

			switch (ch) {
				case -1:
					mEOF = true;
					return (true);

				case 10:
				case 13:
					mLineNumber++;
					return (true);

				default:
					mBuffer.append ( (char) ch );
				break;
			}
		}
	}

	@Override
	public CharSequence getCell ( int idx,
	                              boolean trim ) {
		if (idx >= mInclStartIndexes.size ( ))
			return (EmptyCharSequence.INSTANCE);
		try {
			int start = mInclStartIndexes.getIntegerNoRangeCheck ( idx );

			int lastIndex = mBuffer.length ( );
			lastIndex = lastIndex >= 0 ? lastIndex : 0;

			final int lastExclEndIndex = mExclEndIndexes.size ( ) - 1;

			start = start >= lastIndex ? lastIndex : start;
			mStockCharSequence.start = start;

			int end = idx > lastExclEndIndex ? lastIndex : mExclEndIndexes.getIntegerNoRangeCheck ( idx );
			end = end > lastIndex ? lastIndex : end;
			mStockCharSequence.end = end;

			if (trim)
				mStockCharSequence.trimWhitespace ( );
		} catch (Exception e) {
			e.printStackTrace ( );
		}

		return (mStockCharSequence);
	}

	public static void main ( String[] args )
	                                         throws IOException {

		final int length = args.length;
		int[] positions = new int[length - 1];
		for (int index = 1; index < length; index++) {
			positions[index - 1] = Integer.parseInt ( args[index] );
		}

		FixedWidthReader reader = new FixedWidthReader ( new File ( args[0] ),
		                                                 positions );

		reader.readHeaders ( true );
		for (String header : reader.getHeaders ( )) {
			System.out.print ( header + "  " );
		}
		System.out.println ( );

		while (reader.nextLine ( )) {
			System.out.println ( "Line " + reader.getLineNumber ( ) );

			int num = reader.getNumCells ( );

			for (int ii = 0; ii < num; ii++)
				System.out.println ( "Cell #" + ii + ": >>" + reader.getCell ( ii,
				                                                               true ) + "<<" );
		}

		reader.close ( );
	}

}
