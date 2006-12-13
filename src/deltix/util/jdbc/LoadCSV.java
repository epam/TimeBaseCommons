package deltix.util.jdbc;

import java.io.*;
import java.sql.*;

import au.com.bytecode.opencsv.CSVReader;
import deltix.custom.statestreet.fxa.utils.Common;
import deltix.util.Util;

/**
 *
 */
public class LoadCSV {
    private int                     mBatchSize = 1000;
    private boolean                 mAutoCommit = true;
    private Connection              mOutputConnection;
    
    public void                 setAutoCommit (boolean flag) {
        mAutoCommit = flag;
    }
    
    public void                 setBatchSize (int size) {
        mBatchSize = size;
    }
    
    public void                 setOutputConnection (Connection outConn) {
        mOutputConnection = outConn;
    }
    
    public void                 load (File f) throws IOException, SQLException  {
        String                      tableName = f.getName ();        
        int                         dot = tableName.indexOf ('.');
        
        if (dot >= 0)
            tableName = tableName.substring (0, dot);
            
        FileReader                  rd = new FileReader (f);
        
        try {
            load (tableName, rd);
        } finally {
            Util.close (rd);
        }
    }
    
    public void                 load (
        String                      tableName,
        Reader                      rd        
    )
        throws IOException, SQLException
    {
        CSVReader                   csv = new CSVReader (rd);
        String []                   headers = csv.readNext ();
        
        if (headers == null)
            throw new EOFException ("No headers in CSV file");
        
        StringBuilder               createSql = new StringBuilder ();
        StringBuilder               insertSql = new StringBuilder ();
        
        createSql.append ("CREATE TABLE \"");
        createSql.append (tableName);
        createSql.append ("\" (");        
        
        insertSql.append ("INSERT INTO \"");
        insertSql.append (tableName);
        insertSql.append ("\" (");
        
        for (int ii = 0; ii < headers.length; ii++) {
            String      col = headers [ii];
            
            if (ii > 0) {
                createSql.append (", ");
                insertSql.append (",");
            }
            
            createSql.append ("\"");
            createSql.append (col);
            createSql.append ("\" VARCHAR2 (2000)");
            
            insertSql.append ("\"");
            insertSql.append (col);
            insertSql.append ("\"");
        }
        
        createSql.append (")");
        
        insertSql.append (") VALUES (");
        
        for (int ii = 0; ii < headers.length; ii++) {
            if (ii > 0)
                insertSql.append (",");
            
            insertSql.append ("?");
        }
        
        insertSql.append (")");
        
        JDBCUtils.exec (mOutputConnection, createSql.toString ());
               
        PreparedStatement       ps = 
            mOutputConnection.prepareStatement (insertSql.toString ());
        
        try {
            int                 batchCount = 0;
            String []           line;
            
            while ((line = csv.readNext ()) != null) {
                for (int col = 0; col < headers.length; col++)
                    ps.setString (col + 1, line [col]);
                
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
                "Usage: csvload <host> <port> <sid> <user> <password> <csv file> ..."
            );
            return;
        }
        
        Connection              outConn = null;
                
        try {
            outConn = 
                ORACLE.openThinConnection (
                    args [0], 
                    Integer.parseInt (args [1]),
                    args [2],
                    args [3],
                    args [4]
                );
            
            LoadCSV        loader = new LoadCSV ();
            
            loader.setOutputConnection (outConn);
            
            for (int ii = 5; ii < args.length; ii++)
                loader.load (new File (args [ii]));
            
            outConn.close ();
            
        } finally {            
            JDBCUtils.close (outConn);
            
        }
    }    
}
