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

import java.io.*;

/**
 *	Helps execute commands in separate processes.
 */
public class ProcessHelper {
	/**
	 *	Starts a system process and waits for completion.
	 *
	 *	@param cmd			The command to execute.
	 *	@param envp			The environment.
	 *	@param stdin		The stream from which the process will read.
	 *	@param closeStdin	Whether to close stdin at EOF.
	 *	@param stdout		The stream into which the process will write its output.
	 *	@param closeStdout	Whether to close stdout at EOF.
	 *	@param stderr		The stream into which the process will write its errors.
	 *	@param closeStderr	Whether to close stderr at EOF.
	 *	@return				The exit code of the process.
	 *
	 *	@exception IOException	
	 *						When <code>Runtime.exec ()</code> throws it.
	 *	@exception InterruptedException	
	 *						When <code>Process.waitFor ()</code> throws it.
	 */
    public static int           execAndWait (
    	String []					cmd, 
    	String []					envp,
    	InputStream					stdin,
    	boolean						closeStdin,
    	OutputStream				stdout,
    	boolean						closeStdout,
    	OutputStream				stderr,
    	boolean						closeStderr
    ) 
    	throws IOException, InterruptedException
    {
    	Process	proc = exec (cmd, envp, stdin, closeStdin, stdout, closeStdout,
    						 stderr, closeStderr);
    
    	proc.waitFor ();
    	return (proc.exitValue ());
    }
    
	/**
	 *	Starts a system process and returns asynchronously.
	 *
	 *	@param cmd			The command to execute.
	 *	@param envp			The environment.
	 *	@param stdin		The stream from which the process will read.
	 *	@param closeStdin	Whether to close stdin at EOF.
	 *	@param stdout		The stream into which the process will write its output.
	 *	@param closeStdout	Whether to close stdout at EOF.
	 *	@param stderr		The stream into which the process will write its errors.
	 *	@param closeStderr	Whether to close stderr at EOF.
	 *	@return				The process object.
	 *
	 *	@exception IOException	When <code>Runtime.exec ()</code> throws it.
	 */
    public static Process       exec (
    	String []					cmd, 
    	String []					envp,
    	InputStream					stdin,
    	boolean						closeStdin,
    	OutputStream				stdout,
    	boolean						closeStdout,
    	OutputStream				stderr,
    	boolean						closeStderr
    ) 
    	throws IOException
    {
        Process			proc = Runtime.getRuntime ().exec (cmd, envp);
        
        exec (proc, stdin, closeStdin, stdout, closeStdout, stderr, closeStderr);
        
        return (proc);
    }
    
	/**
	 *	Starts a system process and returns asynchronously.
	 *
	 *	@param proc			The process to execute.
	 *	@param stdin		The stream from which the process will read.
	 *	@param closeStdin	Whether to close stdin at EOF.
	 *	@param stdout		The stream into which the process will write its output.
	 *	@param closeStdout	Whether to close stdout at EOF.
	 *	@param stderr		The stream into which the process will write its errors.
	 *	@param closeStderr	Whether to close stderr at EOF.
	 *
	 *	@exception IOException	When <code>Runtime.exec ()</code> throws it.
	 */
    public static void          exec (
    	Process                     proc, 
    	InputStream					stdin,
    	boolean						closeStdin,
    	OutputStream				stdout,
    	boolean						closeStdout,
    	OutputStream				stderr,
    	boolean						closeStderr
    ) 
    	throws IOException
    {    	
    	InputStream		proc_stderr = proc.getErrorStream ();
    	InputStream		proc_stdout = proc.getInputStream ();
    	OutputStream	proc_stdin = proc.getOutputStream ();
    	
        final Thread stdoutThread = stdout == null ? 
    		new InputStreamSink (proc_stdout) :    	
    		new StreamPump (proc_stdout, stdout, true, closeStdout);
    	
    	final Thread stderrThread = stderr == null ?
    		new InputStreamSink (proc_stderr):
    		new StreamPump (proc_stderr, stderr, true, closeStderr);
    	
        final Thread stdinThread = stdin != null ?
    		new StreamPump (stdin, proc_stdin, closeStdin, true) :
            null;
        
        try {
            stdoutThread.start();
            stderrThread.start();
            if (stdinThread != null) {
                stdinThread.start();
                stdinThread.join();
            }
            stdoutThread.join();
            stderrThread.join();             
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }          
    }

	/**
	 *	Starts a system process, which inherits the current standard 
	 *	output streams, and waits for completion. The input stream is
	 *	currently not inherited.
	 *
	 *	@param cmd		The command to execute.
	 *	@param envp		The environment.
	 *	@return			The exit code of the process.
	 *
	 *	@exception IOException	
	 *					When <code>Runtime.exec ()</code> throws it.
	 *	@exception InterruptedException	
	 *					When <code>Process.waitFor ()</code> throws it.
	 */
    public static int           execAndWait (
    	String []					cmd, 
    	String []					envp
    ) 
    	throws IOException, InterruptedException
    {
    	return (execAndWait (cmd, envp, null, false, System.out, false,
    						 System.err, false));
    }
    
	/**
	 *	Starts a system process, which inherits the current standard 
	 *	input/output streams, and waits for completion. 
	 *
	 *	@param cmd		The command to execute.
	 *	@return			The exit code of the process.
	 *
	 *	@exception IOException	
	 *					When <code>Runtime.exec ()</code> throws it.
	 *	@exception InterruptedException	
	 *					When <code>Process.waitFor ()</code> throws it.
	 */
    public static int           execAndWait (String ... cmd) 
    	throws IOException, InterruptedException
    {
    	return (execAndWait (cmd, null));
    }        
}