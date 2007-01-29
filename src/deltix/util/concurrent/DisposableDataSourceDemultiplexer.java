package deltix.util.concurrent;

import java.util.logging.Level;

import deltix.util.*;

/**
 *
 */
public class DisposableDataSourceDemultiplexer <T extends AsynchronousDisposableDataSource> 
    extends DataSourceDemultiplexer <T>
    implements Disposable
{
    /**
     *  Closes all registered disposable data sources. All exceptions are logged 
     *  to {@link Util#LOGGER} and ignored.
     */
    public void         close () {
        for (T ds : dataSources ())
            try {
                ds.close ();
            } catch (Throwable x) {
                Util.LOGGER.log (Level.SEVERE, "close () threw a " + x, x);
            }
    }
}
