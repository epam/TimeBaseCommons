/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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