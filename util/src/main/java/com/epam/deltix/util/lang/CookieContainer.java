package com.epam.deltix.util.lang;

public interface CookieContainer<T> {    
    /**
     * Allows user to store an object.
     * @param cookie object to store
     */
    void setCookie(T cookie);
    
    /**
     * Returns user object stored by {setCookie(T)}
     * @return user object
     */
    T getCookie();
}
