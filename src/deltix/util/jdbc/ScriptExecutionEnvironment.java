package deltix.util.jdbc;

import deltix.util.io.*;
import java.sql.*;

/**
 *
 */
public class ScriptExecutionEnvironment {
    private Connection                          mConnection;
    private String []                           mParameterValues;
    private ScriptExecutionLogger               mLogger;
    private FilenameResolver                    mScriptFinder;
    
    public ScriptExecutionEnvironment (
        Connection                              conn,
        ScriptExecutionLogger                   logger,
        FilenameResolver                        scriptFinder,
        String ...                              params
    ) 
    {
        mConnection = conn;
        mLogger = logger;
        mScriptFinder = scriptFinder;
        mParameterValues = params;
    }
    
    public ScriptExecutionEnvironment (ScriptExecutionEnvironment copy) {
        this (
            copy.getConnection (),
            copy.getLogger (),
            copy.getScriptFinder (),
            copy.getParameterValues ()
        );
    }
    
    public ScriptExecutionEnvironment () {
        this (null, null, ClassLoaderFilenameResolver.STD_CLASSPATH_RESOLVER);
    }
    
    /**
     *  Returns the DB connection, or null
     */
    public Connection                           getConnection () {
        return (mConnection);
    }

    /**
     *  See the documentation for {@link #getConnection}
     */
    public void                                 setConnection (Connection value) {
        mConnection = value;
    }

    /**
     *  Returns the <i>parameterValues</i> property.
     */
    public String []                            getParameterValues () {
        return (mParameterValues);
    }

    /**
     *  Returns the <i>parameterValues</i> property.
     *	Occurrences of <b>&amp;1</b> in the script
     *	are substituted with <code>parameterValues [0]</code>,
     *	etc. If the script does not use parameters,
     *	this argument can be null. Parameters with numbers
     *	greater than <code>paramValues.length + 1</code>
     *	will not be replaced at all, and will most
     *	likely cause a SQL error.
     */
    public void                                 setParameterValues (String [] value) {
        int             num = value.length;

        if (num > 9)
            throw new IllegalArgumentException (
                "Only &1 .. &9 (nine parameters) are supported."
            );
        
        mParameterValues = value;
    }

    /**
     *  Returns the <i>logger</i> property.
     */
    public ScriptExecutionLogger                getLogger () {
        return (mLogger);
    }

    /**
     *  See the documentation for {@link #getLogger}
     */
    public void                                 setLogger (ScriptExecutionLogger value) {
        mLogger = value;
    }

    /**
     *  Returns the <i>scriptFinder</i> property.
     */
    public FilenameResolver                     getScriptFinder () {
        return (mScriptFinder);
    }

    /**
     *  See the documentation for {@link #getScriptFinder}
     */
    public void                                 setScriptFinder (FilenameResolver value) {
        mScriptFinder = value;
    }

    public String                               substituteParameters (
        String                                      in
    )
    {
        if (mParameterValues == null) 
            return (in);
        
        StringBuffer    sb = new StringBuffer ();
        int             pos = 0;
        int             len = in.length ();

        for (;;) {
            int         idx = in.indexOf ("&", pos);

            if (idx == -1)
                break;

            int         idx1 = idx + 1;

            if (idx1 == len)
                break;

            char        ch = in.charAt (idx1);
            int         pidx = ch - '1';

            if (pidx >= 0 && pidx < mParameterValues.length) {
                sb.append (in, pos, idx);
                sb.append (mParameterValues [pidx]);
                pos = idx + 2;
            }
            else {
                sb.append (in, pos, idx1);
                pos = idx1;
            }
        }

        sb.append (in, pos, len);
        return (sb.toString ());
    }
}
