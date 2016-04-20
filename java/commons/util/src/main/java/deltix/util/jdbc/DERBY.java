package deltix.util.jdbc;

import java.util.*;
import java.util.logging.*;
import java.text.*;
import java.sql.*;
import java.io.*;

import deltix.util.lang.Util;

public class DERBY {
    public static void          loadDriver () throws ClassNotFoundException {
        Class.forName ("org.apache.derby.jdbc.EmbeddedDriver"); // Support for embedded mode only at this time.
    }
    
    static {
        try {
            loadDriver ();
        } catch (ClassNotFoundException cnfx) {
            Util.LOGGER.log (Level.SEVERE, "Failed to load the Apache DERBY JDBC driver", cnfx);
        }
    }
}
