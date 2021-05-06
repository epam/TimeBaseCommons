package com.epam.deltix.util.codec;

import java.io.*;

public class HexBinCharEncoder extends BinCharEncoder {
	public static final char []	UPPER_CASE_VOCABULARY = {
		'0', '1', '2', '3', '4', '5', '6', '7', 
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	};
	
	public static final char []	LOWER_CASE_VOCABULARY = {
		'0', '1', '2', '3', '4', '5', '6', '7', 
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	
	private char []				mVocabulary;
	private boolean				mInsertSpace;
	private int					mNumPerLine;
	private String				mPrefix;
	private String				mPostfix;
	private int					mNumInThisLine = 0;
	
    /**
     *  @param wr       	The Writer to write to
     *  @param insertSpace  Whether to insert spaces bertween each byte
     *	@param upperCase	Whether to use upper-case letters
     *  @param numPerLine   If it more 0, a line separator will be inserted
     *                      after this many bytes.
     *	@param prefix		The characters to write at the beginning
     *							of each line
     *	@param postfix		The characters to write at the end
     *							of each line
     */
    public HexBinCharEncoder (
    	Writer			wr, 
    	boolean 		insertSpace, 
    	boolean			upperCase,
    	int 			numPerLine,
    	String			prefix,
    	String			postfix
    ) 
    {
        super (wr);
        mVocabulary = 
        	upperCase ? UPPER_CASE_VOCABULARY : LOWER_CASE_VOCABULARY;
        
        mNumPerLine = numPerLine > 0 ? numPerLine : -1;
        mInsertSpace = insertSpace;
        mPrefix = prefix;
        mPostfix = postfix;
    }
    
    /**
     *  @param wr       	The Writer to write to
     *  @param insertSpace  Whether to insert spaces bertween each byte
     *	@param upperCase	Whether to use upper-case letters
     *  @param numPerLine   If it more 0, a line separator will be inserted
     *                      after this many bytes.
     */
    public HexBinCharEncoder (
    	Writer			wr, 
    	boolean 		insertSpace, 
    	boolean			upperCase,
    	int 			numPerLine
    ) 
    {
        this (wr, insertSpace, upperCase, numPerLine, "", "");
    }
    
    /**
     *	Constructs a HexBinCharEncoder that writes upper-case letters
     *	in a single line
     *
     *  @param wr       	The Writer to write to
     *  @param insertSpace  Whether to insert spaces bertween each byte
     */
    public HexBinCharEncoder (
    	Writer			wr, 
    	boolean 		insertSpace
    ) 
    {
        this (wr, insertSpace, true, 0);
    }
    
    /**
     *	Constructs a HexBinCharEncoder that writes upper-case letters
     *	separated by spaces in a single line
     *
     *  @param wr       	The Writer to write to
     */
    public HexBinCharEncoder (Writer wr) {
        this (wr, true, true, 0);
    }
    
    public void write (int b) throws IOException {
    	if (mNumPerLine > 0) {
    		if (mNumInThisLine == 0) {
    			mWriter.write (mPrefix);
    		}
    		else if (mNumInThisLine == mNumPerLine) {
    			mWriter.write (mPostfix);
    			mWriter.write ('\n');
    			mNumInThisLine = 0;
    			mWriter.write (mPrefix);
    		}
    	}
    	
    	if (mInsertSpace && mNumInThisLine > 0) {
    		mWriter.write (' ');
    	}

    	mWriter.write (mVocabulary [(b >>> 4) & 0xF]);
    	mWriter.write (mVocabulary [b & 0xF]);
    	
    	if (mInsertSpace || mNumPerLine > 0)
    		mNumInThisLine++;
    }
    
    /**
     *	Convenience method to encode a single array of bytes.
     *
     *  @param bytes       	Bytes to encode
     *  @param insertSpace  Whether to insert spaces between each byte
     *	@param upperCase	Whether to use upper-case letters
     *  @param numPerLine   If it more 0, a line separator will be inserted
     *                      after this many bytes.
     *	@return				The encoded string.
     */
    public static String		encode (
    	byte [] 					bytes,
    	boolean 					insertSpace, 
    	boolean						upperCase,
    	int 						numPerLine
    ) 
    {
        return (encode (bytes, 0, bytes.length, insertSpace, upperCase, numPerLine));
    }
    
    /**
     *	Convenience method to encode a single array of bytes.
     *
     *  @param bytes       	Bytes to encode
     *  @param offset       Start offset
     *  @param length       Number of bytes to encode
     *  @param insertSpace  Whether to insert spaces between each byte
     *	@param upperCase	Whether to use upper-case letters
     *  @param numPerLine   If it more 0, a line separator will be inserted
     *                      after this many bytes.
     *	@return				The encoded string.
     */
    public static String		encode (
    	byte [] 					bytes,
        int                         offset,
        int                         length,
    	boolean 					insertSpace, 
    	boolean						upperCase,
    	int 						numPerLine
    ) 
    {
    	try {
    		StringWriter		swr = new StringWriter ();
	    	
    		HexBinCharEncoder	enc =	
				new HexBinCharEncoder (
					swr, 
					insertSpace, 
					upperCase, 
					numPerLine
				);
				
    		enc.write (bytes, offset, length);
	    	
    		return (swr.toString ());  
    	} catch (IOException iox) {
    		//	IO Exception should not be thrown when
    		//	writing to a StringWriter ()
    		throw new RuntimeException (iox);
    	}
    }
    
    public static void	main (String [] args) throws Exception {
    	byte []		b = { 
    		(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, 
    		(byte) 5, (byte) 6, (byte) 7, (byte) 100, (byte) 101, 
    		(byte) 102, (byte) 103, (byte) 104, (byte) 200,
    		(byte) 201, (byte) 202, (byte) 203, (byte) 204, (byte) 205
    	};
    	
    	Writer		wr = new OutputStreamWriter (System.out);
    	
    	new HexBinCharEncoder (wr, true, true, 4).write (b);
    	
    	wr.flush ();
    	
    	System.out.println ();
    	    	
    	new HexBinCharEncoder (wr, false, false, 4).write (b);
    	
    	wr.flush ();
    	
    	System.out.println ();
    	    	
    	new HexBinCharEncoder (wr).write (b);
    	wr.flush ();
    	
    }
}
