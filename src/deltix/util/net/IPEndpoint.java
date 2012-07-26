package deltix.util.net;

/**
 *
 */
public final class IPEndpoint {
    private final String                host;
    private final int                   port;
    private final IPType                protocol;
    
    public IPEndpoint (
        String                          host, 
        int                             port, 
        IPType                          protocol
    )
    {
        if (host == null || host.isEmpty ())
            throw new IllegalArgumentException ("Empty or null host");
        
        if (port < 1 || port > 65535)
            throw new IllegalArgumentException ("Illegal port: " + port);
        
        if (protocol == null)
            throw new IllegalArgumentException ("Null protocol");
        
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    public String               getHost () {
        return host;
    }

    public int                  getPort () {
        return port;
    }

    public IPType               getProtocol () {
        return protocol;
    }

    @Override
    public boolean              equals (Object obj) {
        if (this == obj)
            return (true);
        
        if (!(obj instanceof IPEndpoint))
            return (false);
                    
        final IPEndpoint    that = (IPEndpoint) obj;
        
        return (
            this.host.equals (that.host) && 
            this.port == that.port &&
            this.protocol == that.protocol
        );        
    }

    @Override
    public int                  hashCode () {
        int     hash = 7;
        hash = 31 * hash + this.host.hashCode ();
        hash = 31 * hash + this.port;
        hash = 31 * hash + this.protocol.hashCode ();
        
        return hash;
    }
    
    @Override
    public String               toString () {
        return (protocol + ":" + host + ":" + port);
    }
}
