package deltix.util.vsocket;

import deltix.util.lang.Disposable;
import deltix.util.lang.DisposableListener;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

/**
 *  A virtual socket.
 */
public interface VSChannel extends Disposable {

    public int                  getLocalId ();

    public int                  getRemoteId ();

    public String               getRemoteAddress();

    public String               getRemoteApplication();

    public String               getClientId();

    public VSOutputStream       getOutputStream ();

    public DataOutputStream     getDataOutputStream ();

    public InputStream          getInputStream ();

    public DataInputStream      getDataInputStream ();

    public VSChannelState       getState();

    public boolean              setAutoflush(boolean value);

    public boolean              isAutoflush();
    
    public void                 close(boolean terminate);

    public void                 setAvailabilityListener (Runnable lnr);
    
    public Runnable             getAvailabilityListener ();

    public boolean              getNoDelay();

    public void                 setNoDelay(boolean value);
    
    public String               encode(String value);

    public String               decode(String value);

    void                        addDisposableListener(DisposableListener<VSChannel> listener);

    void                        removeDisposableListener(DisposableListener<VSChannel> listener);


    /**
     * @return value previously set by {@link #setTag(String)}
     *
     * @apiNote experimental
     */
    @Nullable
    String getTag();

    /**
     * Sets an arbitrary tag that can be used for debugging purposes. It is not sent to the remote side.
     *
     * @apiNote experimental
     */
    void setTag(@Nullable String tag);
}
