package deltix.util.concurrent;

/**
 * A resource that changes its state between available and unavailable.
 * Usage (assuming "resource" implements IntermittentlyAvailableResource):
 *<pre>
 *resource.setAvailabilityListener (
 *    new AvailabilityListener &lt;...&gt; () {
 *        public void             maybeAvailable (... resource) {
 *            synchronized (myLock) {
 *              availablePool.add (resource);
 *            }
 *        }
 *    }
 *);
 *...
 *synchronized (myLock) {
 *    try {
 *        resource.criticalOperation ();
 *    } catch (UnavailableResourceException x) {
 *        availablePool.remove (resource);
 *    }
 *}
 *</pre>
 * 
 * Note that it is absolutely critical to synchronize the <tt>maybeAvailable</tt>
 * callback on the same monitor as the critical operation call. This ensures
 * that, while UnavailableResourceException is being handled and the resource is
 * removed from the available pool, an opposite call to maybeAvailable cannot be
 * made.
 */

public interface IntermittentlyAvailableResource <T> {    
    /**
     *  Installs the (only) availability listener.
     *
     *  @param lnr  The listener to be notified when the
     *              resource may have become available after the critical operation
     *              threw an UnavailableResourceException.
     */
    public void         setAvailabilityListener (AvailabilityListener <? super T> lnr);
}
