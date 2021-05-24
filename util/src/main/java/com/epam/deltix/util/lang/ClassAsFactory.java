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
package com.epam.deltix.util.lang;

import java.lang.reflect.Constructor;

/**
 *  Adapts Class to Factory.
 */
public class ClassAsFactory <C> implements Factory <C> {
    protected final Constructor <? extends C>     cons;
    protected final Object []           args;

    @SuppressWarnings ("unchecked")
    private static Class <?> [] getTypes (Object [] args) {
        int                         numArgs = args.length;
        Class <?> []                paramTypes = new Class [numArgs];

        for (int ii = 0; ii < numArgs; ii++)
            paramTypes [ii] = args [ii].getClass ();

        return (paramTypes);
    }

    public ClassAsFactory (Class <? extends C> cls, Object ... args)
        throws NoSuchMethodException
    {
        this (cls, getTypes (args), args);
    }

    public ClassAsFactory (Class <? extends C> cls, Class <?> [] argTypes, Object [] args)
        throws NoSuchMethodException
    {
        cons = cls.getConstructor (argTypes);
        cons.setAccessible (true);

        this.args = args;
    }

    public C                    create () {
        try {
            return (cons.newInstance (args));
        } catch (Exception x) {
            throw new RuntimeException (x);
        }
    }   
}