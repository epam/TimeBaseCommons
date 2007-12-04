package deltix.util.csvx;

import au.com.bytecode.opencsv.*;
import java.util.*;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.logging.*;

import deltix.qsrv.provider.pub.*;
import deltix.qsrv.qql.comp.*;
import deltix.util.Util;
import deltix.qsrv.pub.*;
import deltix.qsrv.qql.runtime.*;
import deltix.qsrv.impl.util.*;

/**
 *
 */
public class CSVXReader {
    private String                  mDiagPrefix;
    private String []               mHeaders;
    private CSVParser               mCSVReader;

    public CSVXReader () {
    }
        
    public void             open (Reader rd, String diagPrefix) 
        throws IOException 
    {
        mDiagPrefix = diagPrefix;
        mCSVReader = new CSVParser (rd);
        
        if (!mCSVReader.nextLine ())
            throw new EOFException (mDiagPrefix + "File is empty");

        int     numHeaders = mCSVReader.getNumCells ();
        
        mHeaders = new String [numHeaders];
        
        for (int ii = 0; ii < numHeaders; ii++)
            mHeaders [ii] = mCSVReader.getCell (ii).toString ();
    }
    
    public String []        getHeaders () {
        return (mHeaders);
    }
    
    public int              getLine () {
        return (mCSVReader.getLineNumber ());
    }
    
    public boolean          next () throws IOException {
        return (mCSVReader.nextLine ());
    }
    
    public Object           getValue (ColumnDescriptor cd) {
        int     idx = cd.getCSVIdx ();
        int     curNumCells = mCSVReader.getNumCells ();
        
        if (idx >= curNumCells)
            return (null);
        
        return (cd.getValue (mCSVReader.getCell (idx)));
    }
    
    public void             setIndexFromHeaders (ColumnDescriptor ... cds) {
        for (ColumnDescriptor cd : cds)
            if (!cd.findIndexFromHeaders (mHeaders))
                throw new RuntimeException (
                    mDiagPrefix + "1: Header '" + cd.getHeader () + "' was not found"
                );
    }
}
