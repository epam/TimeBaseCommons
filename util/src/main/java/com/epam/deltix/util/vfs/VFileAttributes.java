package com.epam.deltix.util.vfs;

public interface VFileAttributes {
    String absolutePath();
    
    String name();

    long creationTime();

    boolean isDirectory();
    
    boolean isFile();
    
    long lastModifiedTime();
    
    long size();
}
