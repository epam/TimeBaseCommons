package deltix.data.stream;

import deltix.util.Disposable;

/**
 *  Something that consumes a message
 */
public interface MessageChannel <T> extends Disposable {
    public void         send (T msg);    
}
