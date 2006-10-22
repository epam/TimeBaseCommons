package deltix.util.jdbc;

import deltix.util.io.*;
import java.sql.*;

/**
 *
 */
public class ScriptExecutionEnvironment {
    private Connection                          mConnection = null;
    private String []                           mParameterValues = null;
    private ScriptExecutionLogger               mLogger = null;
    private FilenameResolver                    mScriptFinder;
        
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


}
