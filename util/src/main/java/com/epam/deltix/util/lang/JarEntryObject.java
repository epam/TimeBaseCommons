package com.epam.deltix.util.lang;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * @author atamur
 * @since 15-Oct-2009
 */
class JarEntryObject implements JavaFileObject {
    private final String binaryName;
    private final URI uri;
    private final String name;

    public JarEntryObject(String binaryName, URI uri) {
        this.uri = uri;
        this.binaryName = binaryName;
        this.name = uri.getPath() == null ? uri.getSchemeSpecificPart() : uri.getPath(); // for FS based URI the path is not null, for JAR URI the scheme specific part is not null
    }

    @Override
    public URI toUri() {
        return uri;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return uri.toURL().openStream(); // easy way to handle any URI!
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public boolean delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kind getKind() {
        return Kind.CLASS;
    }

    @Override // copied from SimpleJavaFileManager
    public boolean isNameCompatible(String simpleName, Kind kind) {
        String baseName = simpleName + kind.extension;
        return kind.equals(getKind())
                && (baseName.equals(getName())
                || getName().endsWith("/" + baseName));
    }

    @Override
    public NestingKind getNestingKind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Modifier getAccessLevel() {
        throw new UnsupportedOperationException();
    }

    public String binaryName() {
        return binaryName;
    }

    @Override
    public String toString() {
        return "CustomJavaFileObject{" +
                "uri=" + uri +
                '}';
    }
}