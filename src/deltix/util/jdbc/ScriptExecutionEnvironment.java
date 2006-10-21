package deltix.util.jdbc;

import java.sql.*;
import java.io.*;

/**
 *
 */
public interface ScriptExecutionEnvironment {
    public Connection           getConnection ();
    
    public String []            getParameterValues ();
    
    public PrintWriter          getLogger ();
    
    public Statement            getStockStatement ();
}
