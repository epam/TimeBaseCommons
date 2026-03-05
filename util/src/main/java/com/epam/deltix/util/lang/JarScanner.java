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

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;

class JarScanner {

    private static final String CLASS_FILE_EXTENSION = ".class";

    public static List<JavaFileObject> list(ClassLoader loader, String packageName) throws IOException {
        String javaPackageName = packageName.replaceAll("\\.", "/");

        List<JavaFileObject> result = new ArrayList<>();

        Enumeration<URL> urlEnumeration = loader.getResources(javaPackageName);
        while (urlEnumeration.hasMoreElements()) {
            URL packageFolderURL = urlEnumeration.nextElement();
            result.addAll(listUnder(packageName, packageFolderURL));
        }

        return result;
    }

    private static Collection<JavaFileObject> listUnder(String packageName, URL packageFolderURL) {
        String protocol = packageFolderURL.getProtocol();

        if ("jar".equals(protocol)) {
            return processJar(packageName, packageFolderURL);
        }
//        else if ("file".equals(protocol)) {
//            File file = new File(packageFolderURL.getFile());
//            if (file.isDirectory())
//                return processDir(packageName, file);
//        }

        return Collections.emptyList();
    }

    private static List<JavaFileObject> processJar(String packageName, URL packageFolderURL) {
        List<JavaFileObject> result = new ArrayList<>();
        try {
            String[] uris = packageFolderURL.toExternalForm().split("!");
            String jarUri = uris[0];
            String secondUri = uris[1];
            /*
             *  Spring Boot BootJar contains classes of current projects in BOOT-INF/classes,
             *  so we need to look into it.
             */
            if (secondUri.endsWith("classes")) {
                JarURLConnection connection = (JarURLConnection) packageFolderURL.openConnection();
                Enumeration<JarEntry> entryEnum = connection.getJarFile().entries();
                while (entryEnum.hasMoreElements()) {
                    JarEntry jarEntry = entryEnum.nextElement();
                    String name = jarEntry.getName();
                    if (name.endsWith(CLASS_FILE_EXTENSION)) {
                        URI uri = URI.create(jarUri + "!" + secondUri + "/" + name);
                        String binaryName = name.replaceAll("/", ".");
                        binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                        int ind = binaryName.lastIndexOf('.');
                        String currentPackage = binaryName.substring(0, ind);
                        if (currentPackage.equals(packageName))
                            result.add(new JarEntryObject(binaryName, uri));
                    }
                }
            } else if (secondUri.endsWith(".jar")) {
                JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
                String rootEntryName = jarConn.getEntryName();
                if (rootEntryName == null) {
                    rootEntryName = "";
                }
                int rootEnd = rootEntryName.length() + 1;
                if (!secondUri.endsWith("jar"))
                    return result;

                Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
                while (entryEnum.hasMoreElements()) {
                    JarEntry jarEntry = entryEnum.nextElement();
                    String name = jarEntry.getName();
                    if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_FILE_EXTENSION)) {
                        URI uri = URI.create(jarUri + "!" + secondUri + "!/" + name);
                        String binaryName = name.replaceAll("/", ".");
                        binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                        result.add(new JarEntryObject(binaryName, uri));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
        }
        return result;
    }

//    private static List<JavaFileObject> processDir(String packageName, File directory) {
//        List<JavaFileObject> result = new ArrayList<JavaFileObject>();
//        File[] files = directory.listFiles();
//
//        for (int i = 0; files != null && i < files.length; i++) {
//            File file = files[i];
//
//            if (file.isFile() && file.getName().endsWith(CLASS_FILE_EXTENSION)) {
//                String binaryName = packageName.length() != 0 ? packageName + "." + file.getName() : file.getName();
//                binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
//                result.add(new JarEntryObject(binaryName, file.toURI()));
//            }
//        }
//
//        return result;
//    }
}