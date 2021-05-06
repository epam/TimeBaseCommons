package com.epam.deltix.util.vfs;

import java.io.IOException;

public class SimpleVFileVisitor<T extends VFile> implements VFileVisitor<T> {

    @Override
    public VFileVisitResult preVisitDirectory(T dir) throws IOException, InterruptedException {
        checkNotInterrupted();
        return VFileVisitResult.CONTINUE;
    }

    @Override
    public VFileVisitResult visitFile(T file) throws IOException, InterruptedException {
        checkNotInterrupted();
        return VFileVisitResult.CONTINUE;
    }

    @Override
    public VFileVisitResult postVisitDirectory(T dir) throws IOException, InterruptedException {
        checkNotInterrupted();
        return VFileVisitResult.CONTINUE;
    }

    @Override
    public VFileVisitResult visitFailed(T fileOrDir, IOException e) throws IOException, InterruptedException {
        checkNotInterrupted();
        return VFileVisitResult.CONTINUE;
    }
      
    protected void checkNotInterrupted() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }        
    }
}
