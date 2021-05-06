package com.epam.deltix.util.text.tte;

import java.io.*;

/**
 *
 */
public abstract class Element {
    protected abstract void     expand (Values values, Writer out)
        throws IOException;
}
