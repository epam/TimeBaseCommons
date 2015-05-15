package deltix.util.lang;

public interface CookieContainer {    
    /**
     * Allows user to store an object.
     * @param object object to store
     */
    void setCookie(Object object);
    
    /**
     * Returns user object stored by {@link #setCookie(Object)}
     * @return user object
     */
    Object getCookie();
}
