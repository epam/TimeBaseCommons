package deltix.util.swing.tree;

import java.util.*;

import com.jidesoft.filter.*;

public abstract class FilterState<F> implements FilterableNode<F> {

    protected final List<Filter<F>> _filters = new ArrayList<Filter<F>> ();

    public abstract boolean isFiltered (final Object o);

    /**
     * Adds a list of filters.
     *
     * @param filters
     */
    @Override
    public void addFilters (final List<Filter<F>> filters) {
        for (final Filter<F> filter : filters) {
            _filters.add (filter);
        }
    }

    /**
     * Adds a filter.
     *
     * @param filter
     */
    @Override
    public void addFilter (final Filter<F> filter) {
        _filters.add (filter);
    }

    /**
     * Removes the filters. The filter must be added using
     * {@link #addFilter(Filter)}.
     *
     * @param filter
     */
    @Override
    public void removeFilter (final Filter<F> filter) {
        _filters.remove (filter);
    }

    /**
     * Removes all filters.
     */
    @Override
    public void clearFilters () {
        _filters.clear ();
    }

    /**
     * Gets the filters.
     *
     * @return the filters.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Filter<F>[] getFilters () {
        final List<Filter<F>> filters = internalGetFilters ();
        if (filters != null) {
            return filters.toArray (new Filter[filters.size ()]);
        } else {
            return new Filter[0];
        }
    }

    @Override
    public void reload () {
    }

    /**
     * Gets the acutal list that contains all the filters. It is used internally
     * as the value returns from this method is mutable.
     *
     * @return the list of filters.
     */
    private List<Filter<F>> internalGetFilters () {
        return _filters;
    }

}
