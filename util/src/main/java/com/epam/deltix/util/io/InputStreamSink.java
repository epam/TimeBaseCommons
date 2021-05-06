package com.epam.deltix.util.io;

import java.io.InputStream;

/**
 *	Caps an InputStream. This kind of Thread reads the
 *	specified InputStream until it hits the EOF, completely
 *	discarding the contents. Useful to attach this to the
 *	InputStreams of a Process, so the process does not block
 *	forever when it fills the buffers.
 */
public class InputStreamSink extends Thread {
    private InputStream         mInputStream;
    private boolean				mClose;
    
    /**
     *	Constructs an InputStreamSink.
     *
     *	@param is		The input stream to empty.
     *	@param close	Whether to close the stream when it ends.
     */
    public InputStreamSink (InputStream is, boolean close) {
        mInputStream = is;
        mClose = close;
    }

    /**
     *	Constructs an InputStreamSink which will close the stream at EOF.
     *
     *	@param is		The input stream to empty.
     */
    public InputStreamSink (InputStream is) {
        this (is, true);
    }

    public void     run () {
        try {
            byte []     buf = new byte [512];
            
            while (mInputStream.read (buf) >= 0);
            
            if (mClose)
            	mInputStream.close ();
        } catch (Exception x) {
        	x.printStackTrace ();
        }
    }
}
