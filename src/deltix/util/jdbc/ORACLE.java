package deltix.util.jdbc;

import java.util.*;
import java.util.logging.*;
import java.text.*;
import java.sql.*;
import java.io.*;

import deltix.util.Util;

/** @deprecated */
public class ORACLE {
    public static void          loadDriver () throws ClassNotFoundException {
        Class.forName ("oracle.jdbc.driver.OracleDriver");
    }
    
    static {
        try {
            loadDriver ();
        } catch (ClassNotFoundException cnfx) {
            Util.LOGGER.log (Level.SEVERE, "Failed to load the ORACLE JDBC driver", cnfx);
        }
    }

    public static void			test (Connection conn) throws SQLException {
        Statement	stmt = conn.createStatement ();

        try {
            ResultSet	rs = stmt.executeQuery ("SELECT 1 FROM DUAL");

            if (!rs.next ())
                throw new SQLException ("Selected 0 rows from DUAL (???)");

            rs.close ();
        } finally {
            JDBCUtils.close (stmt);
        }
    }

    private static final int	OUTSIDE = 1;
    private static final int	PLSQL = 2;
    private static final int	DDL = 3;

    public static List <String>     readSqlFile (LineNumberReader lnrd)
        throws IOException
    {
        ArrayList <String>		queries = new ArrayList <String> ();
        int                     state = OUTSIDE;
        StringBuffer            sb = new StringBuffer ();

        for (;;) {
            String		l = lnrd.readLine();

            if (l == null)
                break;

            l = l.trim ();

            if (l.startsWith ("--") || l.length () == 0)
                continue;

            if (state == OUTSIDE) {
                String		low = l.toLowerCase ();

                if (low.startsWith ("create") && low.indexOf ("package") != -1)
                    state = PLSQL;
                else
                    state = DDL;
            }

            if (state == PLSQL) {
                if (l.equals ("/"))
                    state = OUTSIDE;
                else {
                    sb.append (l);
                    sb.append (' ');
                }
            } else {
                if (l.endsWith (";")) {
                    sb.append (l.substring (0, l.length () - 1));
                    state = OUTSIDE;
                }
                else {
                    sb.append (l);
                    sb.append (' ');
                }
            }

            if (state == OUTSIDE) {
                queries.add (sb.toString ());
                sb.setLength (0);
            }
        }

        return (queries);
    }

    public static List <String>     readSqlFile (File f)
        throws IOException
    {
        FileReader				frd = new FileReader (f);

        try {
            return (readSqlFile (new LineNumberReader (frd)));
        } finally {
            frd.close ();
        }
    }


    public static List <String>     getTableNames (Connection conn)
        throws SQLException
    {
        return (JDBCUtils.queryStrings (conn, "SELECT TABLE_NAME FROM USER_TABLES"));
    }

    public static List <String> 	getViewNames (Connection conn)
        throws SQLException
    {
        return (JDBCUtils.queryStrings (conn, "SELECT VIEW_NAME FROM USER_VIEWS"));
    }

    public static List <String>     getSequenceNames (Connection conn)
        throws SQLException
    {
        return (JDBCUtils.queryStrings (conn, "SELECT SEQUENCE_NAME FROM USER_SEQUENCES"));
    }

    public static void				dropObjects (
        Connection						conn,
        String							templateSql,
        List							objects
    )
        throws SQLException
    {
        Statement	stmt = conn.createStatement ();

        try {
            for (Object obj : objects)
                stmt.execute (
                    MessageFormat.format (templateSql, new Object [] { obj })
                );
        } finally {
            JDBCUtils.close (stmt);
        }
    }

    public static void				dropTables (Connection conn, List tables)
        throws SQLException
    {
        dropObjects (conn, "DROP TABLE {0} CASCADE CONSTRAINTS", tables);
    }

    public static void				dropViews (Connection conn, List views)
        throws SQLException
    {
        dropObjects (conn, "DROP VIEW {0}", views);
    }

    public static void				dropSequences (Connection conn, List sequences)
        throws SQLException
    {
        dropObjects (conn, "DROP SEQUENCE {0}", sequences);
    }


}
