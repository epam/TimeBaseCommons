package deltix.util.jdbc;

import deltix.util.lang.Util;
import java.util.*;
import java.text.*;
import java.sql.*;
import java.io.*;

import deltix.util.*;
import deltix.util.io.*;
import deltix.util.lang.*;

/**
 *	Mimics the script execution behavior of SQL*Plus, with some limitations
 *  and extensions.
 */
public class Script {
    private static final int	OUTSIDE = 1;
    private static final int	PLSQL = 2;
    private static final int	DDL = 3;
    private static final int	COMMENT = 4;

    private List <ScriptStatement>          mStatements = 
        new ArrayList <ScriptStatement> ();
    private ScriptExecutionEnvironment      mEnv;
        
    /**
     *	Constructs an empty script.
     */
    public Script (ScriptExecutionEnvironment env) {
        mEnv = env;
    }

    /**
     *	Reads the script from a Reader.
     *	@exception IOException				When various IO errors occur.
     */
    public void					read (Reader rd) throws IOException {
        mStatements.clear ();

        LineNumberReader	lnrd = new LineNumberReader (rd);
        int					state = OUTSIDE;
        StringBuffer		sb = new StringBuffer ();

        for (;;) {
            String		l = lnrd.readLine();
            //System.out.println (">>> " + state + " " + l + "<<<");

            if (l == null)
                break;

            String      lineLower = l.toLowerCase ();
            String		test = lineLower.trim ().replace ('\t', ' ');
            //
            //	Test for empty line.
            //
            if (test.length () == 0)
                continue;
            //
            //	Test for comments. We support only the entire line
            //	being a comment (starts with --).
            //
            if (test.startsWith ("--"))
                continue;

            if (state == OUTSIDE && test.startsWith ("/*")) 
                state = COMMENT;
            
            if (state == COMMENT) {
                int     end = test.indexOf ("*/");
                    
                if (end != -1) {
                    state = OUTSIDE;
                    
                    test = test.substring (end + 2).trim ();
                    
                    if (test.length () == 0)
                        continue;
                }
                else
                    continue;
            }
            
            if (state == OUTSIDE) {                
                //
                //	Test for exit statement. 
                //
                if (test.startsWith ("exit")) {
                    break;
                }
                //
                //	Test for prompt statement
                //
                if (test.startsWith ("prompt")) {
                    int     pos = lineLower.indexOf ("prompt");
                    
                    mStatements.add (
                        new PromptStatement (l.substring (pos + 6).trim ())
                    );

                    continue;
                }
                //
                //	Test for script execute. We support only @-statements
                //	that lie entirely on one line.
                //
                if (test.charAt (0) == '@') {
                    String  lineTrimmed = l.trim ();
                    int		lineLength = lineTrimmed.length ();
                    
                    int		first =
                        lineTrimmed.charAt (1) == '@' ? 2 : 1;

                    int		last =
                        lineTrimmed.charAt (lineLength - 1) == ';' ?
                            lineLength - 1 : lineLength;

                    Script      subScript = new Script (mEnv);
                    
                    // Hack : discard parameters
                    String          s = lineTrimmed.substring (first, last).trim ();
                    StringTokenizer stk = new StringTokenizer (s);
                    String          fname = stk.nextToken ();
                    
                    subScript.read (fname);
        
                    mStatements.add (new AtStatement (subScript));

                    continue;
                }
                //
                //	Test for EXIT
                //
                if (test.startsWith ("exit"))
                    break;

                //
                //	Test for COMMIT
                //
                if (test.startsWith ("commit")) {
                    mStatements.add (new CommitStatement ());
                    continue;
                }

                //
                //	Test for CALL
                //
                if (test.startsWith ("call")) {
                    StringTokenizer	stk = new StringTokenizer (l);

                    stk.nextToken ();	// call

                    if (!stk.hasMoreTokens ())
                        throw new IOException (
                            "Class name expected after 'call'"
                        );

                    String		className = stk.nextToken ();

                    mStatements.add (new JavaCallStatement (className));
                    continue;
                }

                //
                //	Test for WHENEVER
                //
                if (test.startsWith ("whenever ")) {
                    //	For now, ignore...
                    continue;
                }

                //
                //	Test for SET
                //
                if (test.startsWith ("set ")) {
                    //	For now, ignore...
                    continue;
                }
                
                //
                //	Test for PL/SQL. We support only blocks whose first line is:
                //		create ... [function|procedure|package|type] ...
                //	or
                //		define ...
                //	or
                //		begin ...
                //
                if (
                    test.startsWith ("create ") &&
                    (
                        test.indexOf (" package ") != -1 ||
                        test.indexOf (" function ") != -1 ||
                        test.indexOf (" type ") != -1 ||
                        test.indexOf (" trigger ") != -1 ||
                        test.indexOf (" procedure ") != -1
                    ) ||
                    test.startsWith ("create or replace ") &&
                    (
                        test.indexOf (" package ") != -1 ||
                        test.indexOf (" function ") != -1 ||
                        test.indexOf (" type ") != -1 ||
                        test.indexOf (" trigger ") != -1 ||
                        test.indexOf (" procedure ") != -1
                    ) ||
                    test.startsWith ("define") ||
                    test.startsWith ("declare") ||
                    test.startsWith ("begin")
                )
                    state = PLSQL;
                else
                    state = DDL;
            }

            if (state == PLSQL) {
                if (test.equals ("/"))
                    state = OUTSIDE;
                else {
                    sb.append (l);
                    sb.append ('\n');
                }
            } else {
                if (test.endsWith (";")) {
                    int		endIdx = l.lastIndexOf (";");
                    sb.append (l.substring (0, endIdx));
                    state = OUTSIDE;
                }
                else {
                    sb.append (l);
                    sb.append ('\n');
                }
            }

            if (state == OUTSIDE) {
                String	sql = sb.toString ();
                //System.out.println (">>> [" + sql + "]");

                mStatements.add (new SQLScriptStatement (sql));
                sb.setLength (0);
            }
        }
    }

    /**
     *	Reads the script from a File.
     *	@exception IOException				When various IO errors occur.
     */
    public void					read (File f) throws IOException {
        FileReader			frd = new FileReader (f);

        try {
            read (frd);
        } finally {
            Util.close (frd);
        }
    }

    /**
     *	Reads the script from an InputStream.
     *	@exception IOException				When various IO errors occur.
     */
    public void					read (InputStream is) throws IOException {
        InputStreamReader	isrd = new InputStreamReader (is);

        try {
            read (isrd);
        } finally {
            Util.close (isrd);
        }
    }

    /**
     *	Uses the Script's own ScriptFinder to open the file, then read it.
     *
     *	@exception FileNotFoundException	When the script was not found.
     *	@exception IOException				When various IO errors occur.
     */
    public void					read (String relPath) throws IOException {
        if (relPath.lastIndexOf (".") == -1)
            relPath += ".sql";

        InputStream		is = mEnv.getScriptFinder ().open (relPath);

        if (is == null)
            throw new FileNotFoundException (relPath);

        try {
            read (is);
        } finally {
            Util.close (is);
        }
    }

    /**
     *	Executes the script.
     */
    public void				execute ()
        throws SQLException, InterruptedException
    {
        for (ScriptStatement s : mStatements) {
            if (Thread.interrupted ())
                throw new InterruptedException ();
            
            s.execute (mEnv);
        }
    }


}
