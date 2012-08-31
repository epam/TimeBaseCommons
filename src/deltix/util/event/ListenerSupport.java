package deltix.util.event;

import deltix.util.lang.Util;

public class ListenerSupport<L> {
    private volatile L[] listeners;

    public ListenerSupport() {
    }

    public ListenerSupport(L[] listeners) {
        this.listeners = listeners.clone();
    }

    public synchronized void addListener(L listener) throws NullPointerException {
        if (listener == null)
            throw new NullPointerException("Listener could not be null");
        listeners = Util.arrayadd(listeners, listener);
    }

    @SuppressWarnings("unchecked")
    public synchronized void removeListener(L listener) {
        if (listener == null)
            return;
        listeners = (L[]) Util.arraydel(listeners, listener);
    }

    public synchronized void removeAllListeners() {
        listeners = null;
    }

    public boolean isEmpty() {
        L[] current = listeners;
        return current == null || current.length <= 0;
    }

    public L[] getListeners() {
        return listeners;
    }
    
    public void notifyAll(Notifier<? super L> notifier) {
        L[] current = listeners;
        if (current == null)
            return;
        for (int i = 0; i < current.length; i++)
            notifier.notify(current[i]);
    }
    
    ///////////////////////// HELPER INTERFACES ////////////////////////
    
    public static interface Notifier<L> {
        void notify(L listener);
    }
}
