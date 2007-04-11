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
    private CSVReader               mCSVReader;
    private int                     mLine;
    private String []               mCSVRecord = null;

    public CSVXReader () {
    }
        
    public void             open (Reader rd, String diagPrefix) 
        throws IOException 
    {
        mDiagPrefix = diagPrefix;
        mCSVReader = new CSVReader (rd);
        
        mHeaders = mCSVReader.readNext ();
        
        if (mHeaders == null)
            throw new EOFException (mDiagPrefix + "File is empty");

        mLine = 1;
    }
    
    public String []        getHeaders () {
        return (mHeaders);
    }
    
    public int              getLine () {
        return (mLine);
    }
    
    public boolean          next () throws IOException {
        mCSVRecord = mCSVReader.readNext ();
        mLine++;
        
        return (mCSVRecord != null);
    }
    
    public Object           getValue (ColumnDescriptor cd) {
        return (cd.getValue (mCSVRecord));
    }
    
    public void             setIndexFromHeaders (ColumnDescriptor cd) {
        if (!cd.findIndexFromHeaders (mHeaders))
            throw new RuntimeException (
                mDiagPrefix + "1: Header '" + cd.getHeader () + "' was not found"
            );
    }
}
