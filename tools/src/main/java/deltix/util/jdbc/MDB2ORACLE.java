package deltix.util.jdbc;

import java.io.*;
import java.sql.*;

/**
 *
 */
public class MDB2ORACLE {
    private int                     mBatchSize = 1000;
    private boolean                 mAutoCommit = true;
    private Connection              mOutputConnection;
    private Connection              mInputConnection;
    
    public void                 setAutoCommit (boolean flag) {
        mAutoCommit = flag;
    }
    
    public void                 setBatchSize (int size) {
        mBatchSize = size;
    }
    
    public void                 setOutputConnection (Connection outConn) {
        mOutputConnection = outConn;
    }
    
    public void                 setInputConnection (Connection outConn) {
        mInputConnection = outConn;
    }
    
    public void                 loadTable (
        String                      tableName
    )
        throws SQLException
    {
        loadTable (tableName, tableName);
    }
    
    public void                 loadTable (
        String                      accessName,
        String                      oracleName
    )
        throws SQLException
    {
        Statement                   stmt = null;
        PreparedStatement           ps = null;
        
        try {
            stmt = mInputConnection.createStatement ();
            ResultSet                   rs = stmt.executeQuery ("SELECT * FROM [" + accessName + "]");
            ResultSetMetaData           md = rs.getMetaData ();
            int                         numColumns = md.getColumnCount ();
            
            System.out.println (numColumns + " Columns");
            
            boolean []                  okObjectTrf = new boolean [numColumns + 1];
            StringBuilder               createSql = new StringBuilder ();
            StringBuilder               insertSql = new StringBuilder ();

            createSql.append ("CREATE TABLE \"");
            createSql.append (oracleName);
            createSql.append ("\" (");        

            insertSql.append ("INSERT INTO \"");
            insertSql.append (oracleName);
            insertSql.append ("\" (");

            for (int ii = 1; ii <= numColumns; ii++) {
                String      col = md.getColumnName (ii);
                int         prec = md.getPrecision (ii);
                String      cname = md.getColumnClassName (ii);

                //System.out.println (col + ", " + typename + ", " + prec + ", " + cname);

                if (ii > 1) {
                    createSql.append (", ");
                    insertSql.append (",");
                }

                createSql.append ("\"");
                createSql.append (col);
                createSql.append ("\" ");
                                
                if (cname.equals ("java.lang.String")) {
                    createSql.append ("VARCHAR (" + prec + ")");
                    okObjectTrf [ii] = true;
                }
                else if (cname.equals ("java.lang.Float") || cname.equals ("java.lang.Double")) {
                    createSql.append ("FLOAT");
                    okObjectTrf [ii] = true;
                }
                else if (cname.equals ("java.sql.Timestamp")) {
                    createSql.append ("VARCHAR (32)");  // do not mess with time conversion now
                    okObjectTrf [ii] = false;
                }
                else {
                    System.out.println ("Defaulting " + col + " type for class " + cname);
                    createSql.append ("VARCHAR (2000)");
                    okObjectTrf [ii] = false;
                }              

                insertSql.append ("\"");
                insertSql.append (col);
                insertSql.append ("\"");           
            }

            createSql.append (")");

            insertSql.append (") VALUES (");

            for (int ii = 1; ii <= numColumns; ii++) {
                if (ii > 1)
                    insertSql.append (",");

                insertSql.append ("?");
            }

            insertSql.append (")");

            String              createSqlStr = createSql.toString ();

            try {
                JDBCUtils.exec (mOutputConnection, "DROP TABLE \"" + oracleName + "\" CASCADE CONSTRAINTS");
            } catch (SQLException x) {
                // ignore
            }

            JDBCUtils.exec (mOutputConnection, createSqlStr);

            System.out.println (insertSql.toString ());
            
            ps = mOutputConnection.prepareStatement (insertSql.toString ());

            int                 batchCount = 0;
            String []           line;
            
            while (rs.next ()) {                
                for (int ii = 1; ii <= numColumns; ii++) {
                    String      cname = md.getColumnClassName (ii);
                    Object      val = rs.getObject (ii);
                        
                    if (val == null)
                        ps.setNull (ii, okObjectTrf [ii] ? md.getColumnType (ii) : Types.VARCHAR);
                    else if (okObjectTrf [ii])
                        ps.setObject (ii, val);
                    else
                        ps.setString (ii, val.toString ());
                }
                
                ps.addBatch ();
                batchCount++;
                
                if (batchCount == mBatchSize) {
                    ps.executeBatch ();                    
                    batchCount = 0;
                    
                    if (mAutoCommit)
                        mOutputConnection.commit ();
                }
            }

            if (batchCount != 0) {
                ps.executeBatch ();

                if (mAutoCommit)
                    mOutputConnection.commit ();
            }
        } finally {
            JDBCUtils.close (ps);
        }
    }
    
    public static void main (String [] args) throws Exception {
        if (args.length < 6) {
            System.out.println (
                "Usage: mdb2oracle <host> <port> <sid> <user> <password> <mdb file> <table> ..."
            );
            return;
        }
        
        Connection              outConn = null;
        Connection              inConn = null;
      
        try {
            outConn = 
                ORACLE.openThinConnection (
                    args [0], 
                    Integer.parseInt (args [1]),
                    args [2],
                    args [3],
                    args [4]
                );
            
            inConn = AccessConnectionFactory.open (new File (args [5]));
            
            MDB2ORACLE        loader = new MDB2ORACLE ();
            
            loader.setOutputConnection (outConn);
            loader.setInputConnection (inConn);
            
            for (int ii = 6; ii < args.length; ii++) {
                String          arg = args [ii];
                int             idx = arg.indexOf (':');
                String          accessName = idx == -1 ? arg : arg.substring (0, idx);
                String          oracleName = idx == -1 ? arg : arg.substring (idx + 1);
                                
                loader.loadTable (accessName, oracleName);
            }
            
            outConn.close ();
            inConn.close ();
        } finally {            
            JDBCUtils.close (outConn);
            JDBCUtils.close (inConn);
        }
    }    
}
