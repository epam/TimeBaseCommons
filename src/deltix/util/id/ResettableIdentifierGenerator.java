package deltix.util.id;

public interface ResettableIdentifierGenerator extends IdentifierGenerator {
	void setNext (long nextId);
	
	void markUsed (long id);
}
