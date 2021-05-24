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
package com.epam.deltix.util.io;

import com.epam.deltix.util.progress.ProgressIndicator;
import com.epam.deltix.util.lang.Util;
import java.io.*;


/**
 *	Pumps the content of an InputStream into an OutputStream. 
 */
public class StreamPump extends Thread {
    private InputStream         mInputStream;
    private OutputStream		mOutputStream;
    private boolean				mCloseInput;
    private boolean				mCloseOutput;
    
    /**
     *	Constructs a StreamPump.
     *
     *	@param is			The input stream to pump from.
     *	@param os			The output stream to pump into.
     *	@param closeInput	Whether to close the input stream when it ends.
     *	@param closeOutput	Whether to close the output stream when 
     *						the input stream ends.
     */
    public StreamPump (
    	InputStream 			is, 
    	OutputStream			os, 
    	boolean 				closeInput,
    	boolean					closeOutput
    ) 
    {
        mInputStream = is;
        mOutputStream = os;
        mCloseInput = closeInput;
        mCloseOutput = closeOutput;
    }

    /**
     *	Constructs a StreamPump which will close both streams at EOF.
     *
     *	@param is		The input stream to empty.
     */
    public StreamPump (InputStream is, OutputStream os) {
        this (is, os, true, true);
    }

    public static long     pump (InputStream is, OutputStream os) 
    	throws IOException, InterruptedException
    {
    	return (pump (is, os, 4096));
    }
    
    public static long     pump (InputStream is, OutputStream os, int bufSize) 
    	throws IOException, InterruptedException
    {
        return (pump (is, os, -1, bufSize, null, 0, 0));
    }
    
    public static long     pump (
        InputStream         is, 
        OutputStream        os, 
        long                maxBytes,
        int                 bufSize,
        ProgressIndicator   listener,
        double              workDonePrior,
        double              workPerByte
    ) 
    	throws IOException, InterruptedException
    {
        if (maxBytes <= 0)
            maxBytes = Long.MAX_VALUE;
        
        byte []     buf = new byte [bufSize];
        double      workDone = workDonePrior;
        long        bytesDone = 0;
        
        for (;;) {
            int     numToRead = buf.length;
            
            if (numToRead > maxBytes)
                numToRead = (int) maxBytes;
            
            int		n = is.read (buf, 0, numToRead);
            	
            if (n < 0)
            	break;
            		
            if (n > 0)
            	os.write (buf, 0, n);
            	
            if (Thread.interrupted ())
            	throw new InterruptedException ();
            
            if (listener != null) {
                workDone += n * workPerByte;
                listener.setWorkDone (workDone);
            }
            
            bytesDone += n;
            
            if (bytesDone >= maxBytes) 
                break;
        }
        
        return (bytesDone);
    }
    
    public void     run () {
        try {
            pump (mInputStream, mOutputStream);            
        } catch (Exception x) {
        	x.printStackTrace ();
        } finally {
            if (mCloseInput)
            	Util.close (mInputStream); 
            
            if (mCloseOutput)
            	Util.close (mOutputStream);
        }
    }
}