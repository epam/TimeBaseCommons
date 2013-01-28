package deltix.util.vfs;

import java.io.IOException;

public interface VFileSystem<T extends VFile> {
        
    T getRoot() throws IOException;

    void unmount() throws IOException;
    
}
