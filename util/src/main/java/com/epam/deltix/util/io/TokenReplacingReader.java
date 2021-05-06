package com.epam.deltix.util.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class TokenReplacingReader extends Reader {

    private final StringBuilder     nameBuffer = new StringBuilder();

    private final PushbackReader    reader;
    private final TokenResolver     resolver;

    private String          tokenValue = null;
    private int             tokenValueIndex = 0;

    public TokenReplacingReader(Reader in, TokenResolver resolver) {
        this.reader = new PushbackReader(in, 2);
        this.resolver = resolver;
    }

    @Override
    public int read() throws IOException {
        if (tokenValue != null) {
            int length = tokenValue.length();

            if (tokenValueIndex < length)
                return tokenValue.charAt(tokenValueIndex++);
            
            if (tokenValueIndex == length) {
                tokenValue = null;
                tokenValueIndex = 0;
            }
        }

        int data = reader.read();
        if (data != '$')
            return data;

        data = reader.read();
        if (data != '{') {
            reader.unread(data);
            return '$';
        }
        nameBuffer.delete(0, nameBuffer.length());

        data = reader.read();
        while (data != '}') {
            nameBuffer.append((char) data);
            data = reader.read();
        }

        tokenValue = resolver.resolveToken(nameBuffer.toString());

        if (tokenValue == null || tokenValue.length() == 0)
            tokenValue = "${" + nameBuffer.toString() + "}";

        return tokenValue.charAt(tokenValueIndex++);
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        return read(target.array(), 0, target.limit());
    }

    @Override
    public int read(char[] input) throws IOException {
        return read(input, 0, input.length);
    }

    @Override
    public int read(char[] input, int off, int len) throws IOException {
        int count = 0;

        while (count < len) {
            int next = read();

            if (next == -1) { // EOF?
                if (count == 0)
                    return -1; // none read
                break;
            }

            input[off + (count++)] = (char) next;
        }
        return count;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public long skip(long n) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override
    public void reset() throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    public interface TokenResolver {
        String resolveToken(String token);
    }
}
