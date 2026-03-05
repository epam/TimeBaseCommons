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
package com.epam.deltix.util.net;

import java.io.*;
import java.net.MalformedURLException;

/**
 *
 */
public abstract class Resource {
    public static Resource          create (String url)
        throws MalformedURLException 
    {
        File    local = new File (url);

        if (local.exists ())
            return (new FileResource (local));

        return (new NetResource (url));
    }

    // Used by connector plugins
    public static Resource          create2 (String url)
            throws IOException
    {
        if (url.startsWith("http:") || url.startsWith("https:"))
            return new NetResource(url);

        final File f = new File(url);
        if (f.exists())
            return new FileResource(f);
        else
            throw new FileNotFoundException(url);
    }

    public abstract long            getSize () throws IOException;

    public abstract long            getLastModified () throws IOException;

    public abstract InputStream     openStream () throws IOException;

    /**
     * @param timeout specifies timeout value, in milliseconds.A timeout of zero is
     * interpreted as an infinite timeout.
     */
    public abstract InputStream     openStream (int timeout) throws IOException;
}