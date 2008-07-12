package deltix.util.jdbc;

import deltix.util.lang.Util;
import java.util.logging.*;
import java.sql.*;

/**
 *
 */
public class MSSQLServer {
    public static void          loadMS2005Driver () throws ClassNotFoundException {
        Class.forName ("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }
    
    public static Connection            openMS2005Connection (
        String                              host,
        int                                 port,
        String                              dbname,
        String                              user,
        String                              password
    )
        throws SQLException
    {
        try {
            loadMS2005Driver ();
        } catch (ClassNotFoundException cnfx) {
            Util.LOGGER.log (Level.SEVERE, "Failed to load the MS SQL Server driver", cnfx);
        }
        
        return (
            DriverManager.getConnection (
                "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbname +
                ";SelectMethod=cursor",
                user,
                password
            )
        );
    }
    
    public static void          loadMSDriver () throws ClassNotFoundException {
        Class.forName ("com.microsoft.jdbc.sqlserver.SQLServerDriver");
    }
    
    public static Connection            openMSConnection (
        String                              host,
        int                                 port,
        String                              dbname,
        String                              user,
        String                              password
    )
        throws SQLException
    {
        try {
            loadMSDriver ();
        } catch (ClassNotFoundException cnfx) {
            Util.LOGGER.log (Level.SEVERE, "Failed to load the MS SQL Server driver", cnfx);
        }
        
        return (
            DriverManager.getConnection (
                "jdbc:microsoft:sqlserver://" + host + ":" + port + ";DatabaseName=" + dbname +
                ";SelectMethod=cursor",
                user,
                password
            )
        );
    }
    
    public static void          loadInetDriver () throws ClassNotFoundException {
        Class.forName ("com.inet.tds.TdsDriver");
    }
    
    public static Connection            openInetConnection (
        String                              host,
        int                                 port,
        String                              dbname,
        String                              user,
        String                              password
    )
        throws SQLException
    {
        try {
            loadInetDriver ();
        } catch (ClassNotFoundException cnfx) {
            Util.LOGGER.log (Level.SEVERE, "Failed to load the Inet SQL Server driver", cnfx);
        }
        
        return (
            DriverManager.getConnection (
                "jdbc:inetdae7:" + host + ":" + port + "?database=" + dbname,
                user,
                password
            )
        );
    }    
}
