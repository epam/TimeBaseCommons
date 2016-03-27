package deltix.util.swing.tree;

import java.util.*;

import com.jidesoft.filter.*;

public interface FilterableNode<F> {

    void addFilters (final List<Filter<F>> filters);

    /**
     * Adds a filter.
     * 
     * @param filter
     */
    void addFilter (final Filter<F> filter);

    /**
     * Removes the filters. The filter must be added using
     * {@link #addFilter(Filter)}.
     * 
     * @param filter
     */
    void removeFilter (final Filter<F> filter);

    /**
     * Removes all filters.
     */
    void clearFilters ();

    /**
     * Gets the filters.
     * 
     * @return the filters.
     */
    Filter<F>[] getFilters ();

    void reload ();

}
