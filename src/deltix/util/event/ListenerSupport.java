package deltix.util.event;

import java.lang.reflect.Array;

import deltix.util.lang.Util;

public class ListenerSupport<L> {
    private final Class<L> componentType;
    private volatile L[] listeners;

    @SuppressWarnings("unchecked")
    public ListenerSupport(Class<L> componentType, L ... listeners) {
        this.componentType = componentType;
        if (listeners != null && listeners.length > 0) {
            this.listeners = (L[]) newArray(componentType, listeners.length);
            System.arraycopy(listeners, 0, this.listeners, 0, listeners.length);
        }
    }

    public synchronized void addListener(L listener) throws NullPointerException {
        if (listener == null)
            throw new NullPointerException("Listener could not be null");
        listeners = arrayadd(componentType, listeners, listener);
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

    @SuppressWarnings("unchecked")
    private static <T> T[] newArray(Class<? super T> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] arrayadd(Class<? super T> compType, T[] arr, T newItem) {
        T[] newarr;
        if (arr == null || arr.length <= 0) {
            newarr = (T[]) newArray(compType, 1);
        } else {
            newarr = (T[]) newArray(compType, arr.length + 1);
            System.arraycopy(arr, 0, newarr, 0, arr.length);
        }
        newarr[newarr.length - 1] = newItem;
        return newarr;
    }
    
    ///////////////////////// HELPER INTERFACES ////////////////////////
    
    public static interface Notifier<L> {
        void notify(L listener);
    }
}
