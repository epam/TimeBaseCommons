package deltix.util.jdbc;

import java.io.*;
import java.sql.*;
import java.util.*;

import deltix.util.progress.MsgProgressIndicator;
import static deltix.util.jdbc.JDBCUtils.RB;

/**
 *
 */
public class ORACLE2MDB {
    private int                     mBatchSize = 1000;
    private Connection              mOutputConnection;
    private Connection              mInputConnection;
    
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
        throws SQLException, InterruptedException
    {
        loadTable (tableName, null, null);
    }
    
    public void                 loadTable (
        String                      tableName,
        MsgProgressIndicator        progress,
        String                      condition,
        Object ...                  params
    )
        throws SQLException, InterruptedException
    {
        if (Thread.interrupted ())
            throw new InterruptedException ();
            
        PreparedStatement           sourceStmt = null;
        PreparedStatement           destStmt = null;
        String                      addSql = 
            condition == null ? "" : " WHERE " + condition;
        
        try {
            if (progress != null) {
                progress.setTotalWork (Double.NaN);
                
                progress.message (
                    String.format (RB.getString ("countingRecords"), tableName)
                );
                
                progress.setTotalWork (
                    JDBCUtils.queryInt (
                        mInputConnection, 
                        "SELECT COUNT (*) FROM \"" + tableName + "\"" + addSql,
                        params
                    )
                );
            }
            
            sourceStmt = 
                JDBCUtils.prepareStatement (
                    mInputConnection,
                    "SELECT * FROM \"" + tableName + "\"" + addSql,
                    params
                );
            
            ResultSet                   rs = sourceStmt.executeQuery ();
            ResultSetMetaData           md = rs.getMetaData ();
            int                         numColumns = md.getColumnCount ();
            
            boolean []                  okObjectTrf = new boolean [numColumns + 1];
            StringBuilder               createSql = new StringBuilder ();
            StringBuilder               insertSql = new StringBuilder ();

            createSql.append ("CREATE TABLE \"");
            createSql.append (tableName);
            createSql.append ("\" (");        

            insertSql.append ("INSERT INTO \"");
            insertSql.append (tableName);
            insertSql.append ("\" (");

            for (int ii = 1; ii <= numColumns; ii++) {
                String      col = md.getColumnName (ii);
                int         prec = md.getPrecision (ii);
                String      cname = md.getColumnClassName (ii);

                //System.out.println (col + " (" + prec + "): " + cname);

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
                else if (cname.equals ("java.math.BigDecimal")) {
                    createSql.append ("FLOAT");
                    okObjectTrf [ii] = true;
                }
                else if (cname.equals ("java.sql.Timestamp")) {
                    createSql.append ("VARCHAR (32)");  // do not mess with time conversion now
                    okObjectTrf [ii] = false;
                }
                else {
                    //System.out.println ("Defaulting " + col + " type for class " + cname);
                    createSql.append ("VARCHAR (255)");
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

            if (progress != null) 
                progress.message (
                    String.format (RB.getString ("rebuildingTable"), tableName)
                );
                
            if (Thread.interrupted ())
                throw new InterruptedException ();
            
            try {
                JDBCUtils.exec (mOutputConnection, "DROP TABLE [" + tableName + "]");
            } catch (SQLException x) {
                // ignore
            }
            
            if (Thread.interrupted ())
                throw new InterruptedException ();
            
            JDBCUtils.exec (mOutputConnection, createSqlStr);

            //System.out.println (insertSql.toString ());
            
            if (progress != null) 
                progress.message (
                    String.format (RB.getString ("exportingTable"), tableName)
                );
            
            destStmt = mOutputConnection.prepareStatement (insertSql.toString ());

            int                 batchCount = 0;
            String []           line;
            
            while (rs.next ()) {                
                if (Thread.interrupted ())
                    throw new InterruptedException ();
            
                for (int ii = 1; ii <= numColumns; ii++) {
                    String      cname = md.getColumnClassName (ii);
                    Object      val = rs.getObject (ii);
                        
                    if (val == null)
                        destStmt.setNull (ii, okObjectTrf [ii] ? md.getColumnType (ii) : Types.VARCHAR);
                    else if (okObjectTrf [ii])
                        destStmt.setObject (ii, val);
                    else
                        destStmt.setString (ii, val.toString ());
                }
                
                destStmt.addBatch ();
                batchCount++;
                
                if (progress != null) 
                    progress.incrementWorkDone (1);

                if (batchCount == mBatchSize) {
                    destStmt.executeBatch ();                    
                    batchCount = 0;
                }
            }

            if (Thread.interrupted ())
                throw new InterruptedException ();
            
            if (batchCount != 0) {
                destStmt.executeBatch ();
            }
        } finally {
            JDBCUtils.close (destStmt);
            JDBCUtils.close (sourceStmt);
        }
    }
    
    public static void main (String [] args) throws Exception {
        if (args.length < 6) {
            System.out.println (
                "Usage: oracle2mdb <host> <port> <sid> <user> <password> <mdb file> <table> ..."
            );
            return;
        }
        
        Connection              outConn = null;
        Connection              inConn = null;
      
        try {
            inConn = 
                ORACLE.openThinConnection (
                    args [0], 
                    Integer.parseInt (args [1]),
                    args [2],
                    args [3],
                    args [4]
                );
            
            outConn = AccessConnectionFactory.open (new File (args [5]));
            
            ORACLE2MDB        loader = new ORACLE2MDB ();
            
            loader.setOutputConnection (outConn);
            loader.setInputConnection (inConn);
            
            for (int ii = 6; ii < args.length; ii++)
                loader.loadTable (args [ii]);
            
            outConn.close ();
            inConn.close ();
        } finally {            
            JDBCUtils.close (outConn);
            JDBCUtils.close (inConn);
        }
    }    
}
