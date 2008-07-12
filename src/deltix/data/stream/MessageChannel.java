package deltix.data.stream;

import deltix.util.lang.Disposable;

/**
 *  Object which consumes messages.
 */
public interface MessageChannel <T> extends Disposable {
    /**
     *  This method is invoked to send a message to the object.
     * 
     *  @param msg  A temporary buffer with the message.
     *              By convention, the message is only valid for the duration 
     *              of this call.
     */
    public void         send (T msg);    
}
