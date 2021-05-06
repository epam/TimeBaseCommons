package com.epam.deltix.util.event;

import java.lang.reflect.Array;

import com.epam.deltix.util.lang.Util;

public class ListenerSupport<L> {
    private final Class<L> componentType;
    private volatile L[] listeners;

    @SafeVarargs
    @SuppressWarnings("varargs")
    public ListenerSupport(Class<L> componentType, L... listeners) {
        this.componentType = componentType;
        if (listeners != null && listeners.length > 0) {
            this.listeners = newArray(componentType, listeners.length);
            System.arraycopy(listeners, 0, this.listeners, 0, listeners.length);
        }
    }

    /** @return true if listener has been added */
    public synchronized boolean addIfAbsent(L listener) throws NullPointerException {
        return addIfAbsent(listener, false);
    }

    /** @return true if listener has been added */
    public synchronized boolean addIfAbsent(L listener, boolean insertFirst) throws NullPointerException {
        if (listener == null)
            throw new NullPointerException("Listener could not be null");
        if (Util.contains(listeners, listener))
            return false;
        listeners = arrayadd(componentType, listeners, listener, insertFirst);
        return true;
    }

    public synchronized void addListener(L listener) throws NullPointerException {
        if (listener == null)
            throw new NullPointerException("Listener could not be null");
        listeners = arrayadd(componentType, listeners, listener, false);
    }

    public synchronized void removeListener(L listener) {
        if (listener == null)
            return;
        listeners = Util.arraydel(listeners, listener);
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
    private static <T> T[] newArray(Class<T> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    private static <T> T[] arrayadd(Class<T> compType, T[] arr, T newItem, boolean insertFirst) {
        T[] newarr;
        if (arr == null || arr.length <= 0) {
            newarr = newArray(compType, 1);
            newarr[newarr.length - 1] = newItem;
        } else {
            newarr = newArray(compType, arr.length + 1);
            if (insertFirst) {
                System.arraycopy(arr, 0, newarr, 1, arr.length);
                newarr[0] = newItem;
            } else {
                System.arraycopy(arr, 0, newarr, 0, arr.length);
                newarr[newarr.length - 1] = newItem;
            }
        }
        return newarr;
    }
    
    ///////////////////////// HELPER INTERFACES ////////////////////////
    
    public static interface Notifier<L> {
        void notify(L listener);
    }
}
