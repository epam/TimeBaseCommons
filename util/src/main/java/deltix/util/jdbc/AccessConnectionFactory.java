package deltix.util.jdbc;

import deltix.util.lang.Util;
import java.sql.*;
import java.io.*;

/**
 *
 */
public class AccessConnectionFactory {
    static {        
        try {
            Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (Throwable x) {
            Util.logException ("Failed to load the ODBC/JDBC driver", x);
            System.exit (1);
        }
    }
    
    public static Connection        open (File f) throws SQLException {        
        return ( 
            DriverManager.getConnection (
                "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + f.getPath () +
                ";READONLY=true}"
            )
        );
    }
}
