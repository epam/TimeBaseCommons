package com.epam.deltix.util.lang;

import java.util.*;
import java.text.*;

/**
 *	Framework class for exceptions that automatically format their
 *		LocalizedMessage by using a resource bundle. In order to use this class:
 *
 *	<ol>
 *		<li>Derive your child exception class from this class. Suppose that your
 *			exception class is <tt>deltix.gobbler.FooException</tt>.</li>
 *		<li>Define a resource bundle called
 *			<tt>deltixlab/gobbler/exceptions</tt>. The best way of doing this is
 *			to create a properties resource bundle
 *			<tt>deltixlab/gobbler/exception.properties</tt>. This bundle will
 *			contain the translations of this exception.</li>
 *		<li>Now there are two ways of referencing the individual messages.
 *			If a <tt>LocalizableException</tt> instance is constructed with no
 *			<tt>addlKey</tt> argument, then the message in the resource bundle
 *			will be keyed by the short class name fo the exception.
 *			If a <tt>LocalizableException</tt> instance is constructed with a
 *			<tt>addlKey</tt> argument, then the message in the resource bundle
 *			will be keyed by the a string starting with the short class name,
 *			followed by a ".", and then by the supplied addlKey. In all cases the
 *			addlKey starts with the exception's short class name. For instance,
 *			you can have the following messages in your bundle:
 *<pre>FooException=The foo bar named {0} is out of order.
 *FooException.unknown=The unknown foo bar is out of order.</pre>
 *			In order to reference the first and second messages respectively,
 *			you would throw the exceptions as follows:
 *<pre>throw new FooException (new Object [] { fooBarName });
 *...
 *throw new FooException ("unknown");</pre></li>
 *		<li>Note that if the exception class is renamed, all recource bundles
 *			must be updated.</li>
 *	</ol>
 */
public abstract class LocalizableException extends Exception {
    static final long serialVersionUID = -2562626847072156603L;

    public static final String		RB_NAME = "exceptions";

    private Object []                   mParameters;
    private String			mAddlKey;
    private Locale			mLocale = Locale.getDefault ();

    /**
     *  SPecial constructor that defeats the rest of the framework.
     *  Used when you need to construct a child of LocalizableException that does not
     *  have any message.
     */
    protected LocalizableException () {
        super ();
        mParameters = null;
        mAddlKey = null;
    }

    protected LocalizableException (Object [] parameters) {
        this (null, parameters);
    }

    protected LocalizableException (Object [] parameters, Exception e) {
        this (null, parameters, e);
    }

    protected LocalizableException (
    	String		addlKey,
    	Object []	parameters
    )
    {
        super ();
        mParameters = parameters;
        mAddlKey = addlKey;
    }

    protected LocalizableException (
    	String		addlKey,
    	Object []	parameters,
    	Exception	e
    )
    {
        super (e);
        mParameters = parameters;
        mAddlKey = addlKey;
    }

    public String		getMessage () {
    	return (getLocalizedMessage ());
    }

    public void			setLocale (Locale locale) {
    	mLocale = locale;
    }

    public String		getLocalizedMessage () {
    	return (getLocalizedMessage (mLocale));
    }

    /**
     *	Formats the message for the arbitrary locale.
     */
    public String		getLocalizedMessage (Locale locale) {
    	String				myClassName = getClass ().getName ();
    	int				dot = myClassName.lastIndexOf (".");
    	String				rbName;
    	String				key;

    	if (dot == -1) {
    		rbName = RB_NAME;
    		key = myClassName;
    	}
    	else {
    		int		firstCharOfShortClassName = dot + 1;
    		String	pack =
    			myClassName.substring (
    				0,
    				firstCharOfShortClassName
    			);

    		rbName = pack.replace ('.', '/') + RB_NAME;
    		key = myClassName.substring (firstCharOfShortClassName);
    	}

    	if (mAddlKey != null)
    		key = key + "." + mAddlKey;

    	ResourceBundle		rb = ResourceBundle.getBundle (rbName, locale);
    	return (MessageFormat.format (rb.getString (key), mParameters));
    }
}
