package deltix.util.concurrent;

/**
 *  A resource that changes its state between available and unavailable.
 */
public interface IntermittentlyAvailableResource {
    public void         setAvailabilityListener (AvailabilityListener lnr);
}
