package deltix.util.vfs;

import java.io.IOException;

public interface VFileVisitor<T extends VFile> {
    
    public enum VFileVisitResult {
        CONTINUE, TERMINATE;
    }
    
    VFileVisitResult preVisitDirectory(T dir);

    VFileVisitResult visitFile(T file);    
    
    VFileVisitResult postVisitDirectory(T dir);
    
    VFileVisitResult visitFailed(T fileOrDir, IOException e);
}
