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
package com.epam.deltix.util.vfs;

import com.epam.deltix.util.io.StreamPump;
import com.epam.deltix.util.lang.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class VFiles {
 
//    public static long copyFile(VFile from, VFile to) throws IOException, InterruptedException {
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            return StreamPump.pump(in, out);
//        } finally {
//            Util.close(in);
//            Util.close(out);
//        }
//    }
        
    public static URL appendRelativePath(URL root, String relativePath) throws MalformedURLException {
        relativePath = relativePath.trim();
        if (relativePath.startsWith(".")) {
            throw new MalformedURLException("Relative names '.' and '..' aren't supported yet.");
        }
        relativePath = relativePath.replace('\\', '/');
        final String rootPath = root.getPath();
        return rootPath.endsWith("/") || relativePath.startsWith("/") ?
                new URL(root, rootPath + relativePath) : new URL(root, rootPath + '/' + relativePath);
    }
    
    public static String getParent(String url) {
        return url.substring(0, url.length() - getName(url).length());        
    }

    public static String getParent(URL url) {
        return getParent(url.toString());        
    }

    public static URL getParentUrl(URL url) {
        try {
            return new URL(getParent(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getName(URL url) {
        return getName(url.getPath());
    }
    
    public static String getName(String path) {
        final int i = path.lastIndexOf("/");
        return i > -1
                ? (path.length() > 1 ? path.substring(i + 1) : "/")
                : path;
    }    
    
    private VFiles() {        
    }
}