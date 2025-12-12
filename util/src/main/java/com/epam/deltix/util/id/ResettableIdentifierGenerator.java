package com.epam.deltix.util.id;

public interface ResettableIdentifierGenerator extends IdentifierGenerator {
	void setNext (long id);
}
