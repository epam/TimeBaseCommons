package deltix.util.vfs;

import java.io.IOException;

public class SimpleVFileVisitor<T extends VFile> implements VFileVisitor<T> {

    @Override
    public VFileVisitResult preVisitDirectory(T dir) {
        return VFileVisitResult.CONTINUE;
    }

    @Override
    public VFileVisitResult visitFile(T file) {
        return VFileVisitResult.CONTINUE;
    }

    @Override
    public VFileVisitResult postVisitDirectory(T dir) {
        return VFileVisitResult.CONTINUE;
    }

    @Override
    public VFileVisitResult visitFailed(T fileOrDir, IOException e) {
        return VFileVisitResult.CONTINUE;
    }
            
}
