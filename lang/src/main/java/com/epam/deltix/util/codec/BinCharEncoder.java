package com.epam.deltix.util.codec;

import java.io.*;

public abstract class BinCharEncoder extends OutputStream {
	protected Writer		mWriter;
	
	protected BinCharEncoder (Writer wr) {
		mWriter = wr;
	}
	
	/*
    public void write (int b) throws IOException {
    }
    
    if necessary:
    
    public void flush() throws IOException {
     super.flush ();
     ...
    }
    */

    public void close () throws IOException {
        flush ();
    	mWriter.close ();
    }
    
    public void flush () throws IOException {
        mWriter.flush ();
    }
}
