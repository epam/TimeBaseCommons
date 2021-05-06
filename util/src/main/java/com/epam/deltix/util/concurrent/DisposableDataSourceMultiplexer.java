package com.epam.deltix.util.concurrent;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.lang.Disposable;

/**
 *
 */
public class DisposableDataSourceMultiplexer <T extends AsynchronousDisposableDataSource> 
    extends DataSourceMultiplexer<T>
    implements Disposable
{
    /**
     *  Closes all registered disposable data sources. All exceptions are logged 
     *  to {@link Util#logException(String, Throwable)} and ignored.
     */
    public void         close () {
        for (T ds : dataSources ())
            try {
                ds.close ();
            } catch (Throwable x) {
                Util.logException ("close () threw exception", x);
            }
    }
}
