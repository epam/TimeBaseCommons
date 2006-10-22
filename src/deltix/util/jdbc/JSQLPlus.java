package deltix.util.jdbc;

import java.io.*;
import java.sql.*;
import java.util.*;

import deltix.util.io.NonCachingSearchPathResolver;


/**
 *  Provides functionality to ORACLE's SQL*Plus interpreter, with many restrictions and
 *  a few enhancements.
 */
public class JSQLPlus {
    private static ScriptExecutionLogger   LOGGER = 
        new ScriptExecutionLogger () {
            public void logCommand (String cmd) {
                System.out.println ("=========================");
                System.out.println (new java.util.Date () + ":");
                System.out.println (cmd);
                System.out.println ("=========================");
                System.out.println ();
            }        
        };
        
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

        Connection  conn = null; //tmp
            //DriverManager.getConnection (connect, user, pwd);

        ScriptExecutionEnvironment  env = new ScriptExecutionEnvironment ();
        
        env.setConnection (conn);
        env.setLogger (LOGGER);
        env.setScriptFinder (
            new NonCachingSearchPathResolver (
                new File [] {
                    file.getParentFile ()
                }
            )
        );
        String []	paramValues = new String [args.length - 4];
        System.arraycopy (args, 4, paramValues, 0, paramValues.length);

        env.setParameterValues (paramValues);
        
        Script		script = new Script (env);
        
        script.read (file);

        script.execute ();
        
        if (conn != null)
            conn.close ();
    }
}
