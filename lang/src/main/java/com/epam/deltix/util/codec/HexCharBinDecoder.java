package com.epam.deltix.util.codec;

import java.io.*;

public class HexCharBinDecoder extends CharBinDecoder {
	int					mUpper4Bits;
	boolean				mUpperBitsSet = false;
	
	public HexCharBinDecoder (OutputStream os) {
		super (os);
	}
	
	private void	writeDigit (int n) throws IOException {
		if (mUpperBitsSet) {
			mOutputStream.write ((mUpper4Bits << 4) + n);
			mUpperBitsSet = false;
		}
		else {
			mUpper4Bits = n;
			mUpperBitsSet = true;
		}
	}
	
    public void 	write (int c) throws IOException {
    	if (c >= '0' && c <= '9')
    		writeDigit (c - '0');
    	else if (c >= 'a' && c <= 'f')
    		writeDigit (c - ('a' - 10));
    	else if (c >= 'A' && c <= 'F')
    		writeDigit (c - ('A' - 10));
    }
    
    public void write (char cbuf [], int off, int len) throws IOException {
    	int	limit = off + len;
    	
    	for (int ii = off; ii < limit; ii++)
    		write (cbuf [ii]);
    }    

    public static byte []	decode (CharSequence s) {
    	try {
			ByteArrayOutputStream	baos = new ByteArrayOutputStream ();
			HexCharBinDecoder       dec = new HexCharBinDecoder (baos);
            int                     length = s.length ();
            
            for (int ii = 0; ii < length; ii++)
                dec.write (s.charAt (ii));
            
			return (baos.toByteArray ());
		} catch (IOException iox) {
    		//	IO Exception should not be thrown when
    		//	writing to a ByteArrayOutputStream ()
			throw new RuntimeException ();
		}
    }
}
