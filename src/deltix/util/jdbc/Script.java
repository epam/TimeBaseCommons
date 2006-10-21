package deltix.util.jdbc;

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

    private List <ScriptStatement>          mStatements = new ArrayList <ScriptStatement> ();
    private Connection                      mConnection = null;
    private Statement						mStockStatement = null;
    private String []                       mParamValues = null;
    private PrintWriter                     mLogger = null;
    private FilenameResolver                mScriptFinder = null;
    private ScriptExecutionEnvironment      mEnv = 
        new ScriptExecutionEnvironment () {
            public Statement        getStockStatement () {
                return (mStockStatement);
            }

            public String[]         getParameterValues () {
                return (mParamValues);
            }

            public PrintWriter      getLogger () {
                return (mLogger);
            }

            public Connection       getConnection () {
                return (mConnection);
            }            
        };
        
    /**
     *	Constructs an empty script.
     */
    public Script () {
    }

    /**
     *  Returns the <i>connection</i> property.
     */
    public Connection                       getConnection () {
        return (mConnection);
    }

    /**
     *  Assigns the <i>connection</i> property.
     */
    public void                             setConnection (Connection value)
        throws SQLException
    {
        mConnection = value;
    }

    /**
     *  Returns the <i>paramValues</i> property.
     *	Occurrences of <b>&amp;1</b> in the script
     *	are substituted with <code>paramValues [0]</code>,
     *	etc. If the script does not use parameters,
     *	this argument can be null. Parameters with numbers
     *	greater than <code>paramValues.length + 1</code>
     *	will not be replaced at all, and will most
     *	likely cause a SQL error.
     */
    public String []                        getParameterValues () {
        return (mParamValues);
    }

    /**
     *  Assigns the <i>paramValues</i> property.
     *	Occurrences of <b>&amp;1</b> in the script
     *	are substituted with <code>paramValues [0]</code>,
     *	etc. If the script does not use parameters,
     *	this argument can be null. Parameters with numbers
     *	greater than <code>paramValues.length + 1</code>
     *	will not be replaced at all, and will most
     *	likely cause a SQL error.
     *
     *	@exception IllegalArgumentException
     *						When more than 9 parameters are specified.
     */
    public void                             setParameterValues (String [] value) {
        int             num = value.length;

        if (num > 9)
            throw new IllegalArgumentException (
                "Only &1 .. &9 (nine parameters) are supported."
            );
        
        mParamValues = value;
    }

    /**
     *  Returns the <i>logger</i> property. If not null,
     *	each statement is logged to this logger before execution.
     */
    public PrintWriter                      getLogger () {
        return (mLogger);
    }

    /**
     *  Assigns the <i>logger</i> property. If not null,
     *	each statement is logged to this logger before execution.
     */
    public void                             setLogger (PrintWriter value) {
        mLogger = value;
    }

    /**
     *  Returns the <i>scriptFinder</i> property, used to resolve
     *	script inclusion.
     */
    public FilenameResolver                 getScriptFinder () {
        return (mScriptFinder);
    }

    /**
     *  Assigns the <i>scriptFinder</i> property, used to resolve
     *	script inclusion.
     */
    public void                             setScriptFinder (
        FilenameResolver						value
    )
    {
        mScriptFinder = value;
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

            String		test =
                l.trim ().toLowerCase ().replace ('\t', ' ');
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

            if (state == OUTSIDE) {
                //
                //	Test for prompt statement
                //
                if (test.startsWith ("prompt")) {
                    mStatements.add (
                        new PromptStatement (test.substring (6).trim ())
                    );

                    continue;
                }
                //
                //	Test for script execute. We support only @-statements
                //	that lie entirely on one line.
                //
                if (test.charAt (0) == '@') {
                    int		testLength = test.length ();
                    int		first =
                        test.charAt (1) == '@' ? 2 : 1;

                    int		last =
                        test.charAt (testLength - 1) == ';' ?
                            testLength - 1 : testLength;

                    mStatements.add (
                        new AtStatement (
                            test.substring (first, last).trim (),
                            mScriptFinder
                        )
                    );

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

        InputStream		is = mScriptFinder.open (relPath);

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
        throws SQLException, InterruptedException, IOException
    {
        mStockStatement = mConnection.createStatement ();

        try {
            for (ScriptStatement s : mStatements)
                s.execute (mEnv);
        } finally {
            JDBCUtils.close (mStockStatement);
        }
    }

    /**
     *	Usage: &lt;user&gt; &lt;password&gt; &lt;connectString&gt;
     *			&lt;script&gt; [ &lt;param1&gt; ... ]
     */
    public static void main (String [] args) throws Exception {
        String		user = args [0];
        String		pwd = args [1];
        String		connect = args [2];
        File		file = new File (args [3]);

        oracle.jdbc.driver.OracleDriver.class.getName ();

        Connection  conn =
            DriverManager.getConnection (connect, user, pwd);

        Script		script = new Script ();

        script.setConnection (conn);
        script.setLogger (new PrintWriter (System.out));
        script.setScriptFinder (
            new NonCachingSearchPathResolver (
                new File [] {
                    file.getParentFile ()
                }
            )
        );
        script.read (file);

        String []	paramValues = new String [args.length - 4];
        System.arraycopy (args, 4, paramValues, 0, paramValues.length);

        script.setParameterValues (paramValues);

        script.execute ();
        conn.close ();
    }
}
