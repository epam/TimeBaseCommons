package deltix.util.id;

public interface ResettableIdentifierGenerator extends IdentifierGenerator {
	void setNext (long id);
	void markUsed (long id);
}
