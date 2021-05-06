package com.epam.deltix.util.bcel;

import java.io.*;
import java.util.*;
import java.lang.annotation.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.*;

public class AnnotationsAttribute extends Attribute
{
	private static final String TYPE = "_type";

	private Annotation [] annotations;

	public AnnotationsAttribute (byte tag, int name_index, int length, ConstantPool constant_pool, Annotation[] annotations)
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
