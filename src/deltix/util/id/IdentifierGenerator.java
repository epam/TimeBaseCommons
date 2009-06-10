package deltix.util.id;

/** Thread-safe generator of unique integer numbers */
public interface IdentifierGenerator {
	/** @returns next valid order id. This method is thread safe */
    long next();
}
