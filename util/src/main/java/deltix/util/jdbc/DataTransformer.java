package deltix.util.jdbc;

import deltix.util.progress.*;
import java.sql.*;

/**
 *  Caller must eventually call {@link #close} to free resources.
 */
public class DataTransformer {
    private Connection              mOutputConnection;
    private FieldTrf []             mFieldTrfs;
    private String                  mTableName;
    private boolean                 mMerge;
    private int                     mNumOutColumns;
    private Object [][]             mBuffer;
    private int                     mBufferCount;
    private PreparedStatement       mStatement;
    private int                     mNumProcessed;
    private ProgressIndicator       mProgress = null;
    
    public static FieldTrf []       buildDirectMapping (ResultSetMetaData md) 
        throws SQLException
    {
        int                 columnCount = md.getColumnCount ();
        FieldTrf []         ret = new FieldTrf [columnCount];

        for (int col = 0; col < columnCount; col++) 
            ret [col] = new IdentityFieldTrf (md.getColumnName (col + 1));
        
        return (ret);
    }
    
    public DataTransformer (
        Connection                  outConn,
        FieldTrf []                 fieldTrfs,
        String                      outTableName,
        int                         batchSize,
        boolean                     merge
    ) 
        throws SQLException
    {         
        mOutputConnection = outConn;
        mFieldTrfs = fieldTrfs;
        mTableName = outTableName;
        mMerge = merge;
        
        mNumOutColumns = 0;
        
        for (FieldTrf field : mFieldTrfs) 
            mNumOutColumns += field.getNumOutColumns ();
        
        mBuffer = new Object [batchSize][mNumOutColumns];
        mBufferCount = 0;
                
        StringBuffer            sql = new StringBuffer ();
        int                     numFields = mFieldTrfs.length;
        
        if (mMerge) {
            sql.append ("MERGE INTO \"");
            sql.append (mTableName);
            sql.append ("\" USING (SELECT ");
            
            int                     numValues = 0;
            int                     pkStart = -1;
            FieldTrf                pk = null;
            
            for (int fieldIdx = 0; fieldIdx < numFields; fieldIdx++) {
                FieldTrf    field = mFieldTrfs [fieldIdx];            
                
                if (fieldIdx == numFields - 1) {
                    pkStart = numValues;
                    pk = field;
                }
                
                int         numCols = field.getNumOutColumns ();
                
                for (int ii = 0; ii < numCols; ii++) {
                    String  outName = field.getOutColumnName (ii);
                    String  expr = field.getOutExpression (ii);

                    if (numValues > 0) 
                        sql.append (",");
                    
                    sql.append (expr);
                    sql.append (" \"__");
                    sql.append (numValues);
                    sql.append ("\"");
                    
                    numValues++;
                }
            }
            
            sql.append (" FROM DUAL) ON (");
            
            for (int ii = pkStart; ii < numValues; ii++) {
                if (ii > pkStart) 
                    sql.append (" AND ");
                
                sql.append ("\"__");
                sql.append (ii);
                sql.append ("\"=\"");
                sql.append (pk.getOutColumnName (ii - pkStart));
                sql.append ("\"");
            }
            
            sql.append (") WHEN MATCHED THEN UPDATE SET ");
            
            numValues = 0;
            
            for (int fieldIdx = 0; fieldIdx < numFields - 1; fieldIdx++) {
                FieldTrf    field = mFieldTrfs [fieldIdx];
                int         numCols = field.getNumOutColumns ();
                
                for (int ii = 0; ii < numCols; ii++) {
                    if (numValues > 0) 
                        sql.append (",");
                    
                    sql.append ("\"");
                    sql.append (field.getOutColumnName (ii));
                    sql.append ("\"=\"__");
                    sql.append (numValues);
                    sql.append ("\"");
                    
                    numValues++;
                }
            }
            
            sql.append (" WHEN NOT MATCHED THEN INSERT (");
            
            numValues = 0;
            
            for (int fieldIdx = 0; fieldIdx < numFields; fieldIdx++) {
                FieldTrf    field = mFieldTrfs [fieldIdx];            
                int         numCols = field.getNumOutColumns ();
                
                for (int ii = 0; ii < numCols; ii++) {
                    if (numValues > 0) 
                        sql.append (",");
                
                    sql.append ("\"");
                    sql.append (field.getOutColumnName (ii));
                    sql.append ("\"");
                    
                    numValues++;
                }
            }
            
            sql.append (") VALUES (");
            
            for (int ii = 0; ii < numValues; ii++) {
                if (ii > 0) 
                    sql.append (",");
                
                sql.append ("\"__");
                sql.append (ii);
                sql.append ("\"");
            }
            
            sql.append (")");
        }
        else {
            sql.append ("INSERT INTO \"");
            sql.append (mTableName);
            sql.append ("\" (");

            int             numValues = 0;
            
            for (int fieldIdx = 0; fieldIdx < numFields; fieldIdx++) {
                FieldTrf    field = mFieldTrfs [fieldIdx];            
                int         numCols = field.getNumOutColumns ();
                
                for (int ii = 0; ii < numCols; ii++) {
                    if (numValues > 0) 
                        sql.append (",");
                
                    sql.append ("\"");
                    sql.append (field.getOutColumnName (ii));
                    sql.append ("\"");
                    
                    numValues++;
                }
            }
            
            sql.append (") VALUES (");
            
            numValues = 0;
            
            for (int fieldIdx = 0; fieldIdx < numFields; fieldIdx++) {
                FieldTrf    field = mFieldTrfs [fieldIdx];            
                int         numCols = field.getNumOutColumns ();
                
                for (int ii = 0; ii < numCols; ii++) {
                    if (numValues > 0) 
                        sql.append (",");

                    sql.append (field.getOutExpression (ii));  
                    
                    numValues++;
                }
            }
            
            sql.append (")");          
        }
        
        mStatement = mOutputConnection.prepareStatement (sql.toString ());
    }
    
    public void             setProgressIndicator (ProgressIndicator p) {
        mProgress = p;
    }
    
    public void             close () throws SQLException {
        if (mStatement != null) {
            mStatement.close ();
            mStatement = null;
        }
        mProgress = null;
        mOutputConnection = null;
        mBuffer = null;
        mFieldTrfs = null;
    }
    
    private void            flush () 
        throws SQLException
    {
        int                 numInBatch = 0;
        
        for (int ii = 0; ii < mBufferCount; ii++) {
            Object []       recBuffer = mBuffer [ii];

            for (int jj = 0; jj < recBuffer.length; jj++) 
                mStatement.setObject (jj + 1, recBuffer [jj]);

            mNumProcessed++;
            numInBatch++;
            mStatement.addBatch ();
        }
        
        if (numInBatch > 0)
            mStatement.executeBatch ();        
        
        mBufferCount = 0;
                
        if (mProgress != null)
            mProgress.incrementWorkDone (numInBatch);        
    }
    
    public void             transformAll (ResultSet in) 
        throws SQLException
    {
        ColumnMap               inMap = new ColumnMap ();
        
        inMap.init (in.getMetaData ());        
        
        for (FieldTrf field : mFieldTrfs) 
            field.init (inMap);
            
        while (in.next ()) {
            if (mBufferCount == mBuffer.length)
                flush ();
            
            Object []       recBuffer = mBuffer [mBufferCount];
            int             offset = 0;
            
            for (FieldTrf ft : mFieldTrfs) {
                ft.transform (in, recBuffer, offset);
                offset += ft.getNumOutColumns ();
            }
            
            mBufferCount++;
        }
        
        if (mBufferCount != 0) 
            flush ();        
    }
}
