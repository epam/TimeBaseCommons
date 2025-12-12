package com.epam.deltix.util.vfs;

import java.io.IOException;

public interface VFileVisitor<T extends VFile> {
    
    public enum VFileVisitResult {
        CONTINUE, TERMINATE;
    }
    
    VFileVisitResult preVisitDirectory(T dir) throws IOException, InterruptedException;

    VFileVisitResult visitFile(T file) throws IOException, InterruptedException;    
    
    VFileVisitResult postVisitDirectory(T dir) throws IOException, InterruptedException;
    
    VFileVisitResult visitFailed(T fileOrDir, IOException e) throws IOException, InterruptedException;
}
