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
