package com.epam.deltix.util.codec;

import java.io.*;

public abstract class CharBinDecoder extends Writer {
	protected OutputStream		mOutputStream;
	
	protected CharBinDecoder (OutputStream os) {
		mOutputStream = os;
	}
	
	/*
	optional:
	
    public void write(int c) throws IOException {
    }
    
    mandatory:
    
    public void write (char cbuf [], int off, int len) throws IOException {
    }
    
    if necessary:
    
    public void flush() throws IOException {
     super.flush ();
     ...
    }
    */

    public void close() throws IOException {
        flush ();
    	mOutputStream.close ();
    }
    
    public void flush () throws IOException {
        mOutputStream.flush ();
    }
}
