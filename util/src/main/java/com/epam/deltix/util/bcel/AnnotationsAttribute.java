package com.epam.deltix.util.bcel;

import java.util.*;

import org.apache.bcel.classfile.*;

public class AnnotationsAttribute extends Attribute
{
	private static final String TYPE = "_type";

	private com.epam.deltix.util.bcel.Annotation[] annotations;

	public AnnotationsAttribute (byte tag, int name_index, int length, ConstantPool constant_pool, com.epam.deltix.util.bcel.Annotation[] annotations)
	{
		super(tag, name_index, length, constant_pool);
		this.annotations = annotations;
	}

	public Annotation[] getAnnotations ()
	{
		return annotations;
	}

	public void accept (Visitor v)
	{
		// do nothing
	}

	public Attribute copy (ConstantPool cp)
	{
		return this;
	}

    @Override
	public String toString ()
	{
		return Arrays.asList(annotations).toString();
	}
}
