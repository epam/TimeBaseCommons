package com.epam.deltix.util.io;

import java.io.*;
import java.util.regex.*;

/**
 *
 */
public class RegexFilenameFilter implements FilenameFilter {
    private Pattern     mNamePattern;
    private Pattern     mParentPattern;
    
    public RegexFilenameFilter (String namePattern) {
        this (null, namePattern);   
    }
    
    public RegexFilenameFilter (String parentDirPattern, String namePattern) {
        if (parentDirPattern != null)
            mParentPattern = Pattern.compile (parentDirPattern);
        
        if (namePattern != null)
            mNamePattern = Pattern.compile (namePattern);                
    }
    
    public boolean      accept (File dir, String name) {
        return (
            (mNamePattern == null || mNamePattern.matcher (name).matches ()) &&
            (mParentPattern == null || mParentPattern.matcher (dir.getAbsolutePath ()).matches ())
        );
    }
}
