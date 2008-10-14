package deltix.util.id;

/** Thread-safe generator of unique integer numbers */
public interface IdentifierGenerator {
	long next();
}
