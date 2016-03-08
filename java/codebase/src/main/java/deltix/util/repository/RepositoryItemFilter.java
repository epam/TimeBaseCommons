package deltix.util.repository;

public interface RepositoryItemFilter<T> {
    boolean accepted(T item);
}
