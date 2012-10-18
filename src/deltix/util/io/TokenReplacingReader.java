package deltix.util.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class TokenReplacingReader extends Reader {

    private PushbackReader pushbackReader = null;
    private ITokenResolver tokenResolver = null;
    private StringBuilder tokenNameBuffer = new StringBuilder();
    private String tokenValue = null;
    private int tokenValueIndex = 0;

    public static interface ITokenResolver {

        String resolveToken(String token);
    }

    public TokenReplacingReader(Reader source, ITokenResolver resolver) {
        this.pushbackReader = new PushbackReader(source, 2);
        this.tokenResolver = resolver;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override
    public int read() throws IOException {
            if (this.tokenValue != null) {
                    if (this.tokenValueIndex < this.tokenValue.length()) {
                            return this.tokenValue.charAt(this.tokenValueIndex++);
                    }
                    if (this.tokenValueIndex == this.tokenValue.length()) {
                            this.tokenValue = null;
                            this.tokenValueIndex = 0;
                    }
            }

            int data = this.pushbackReader.read();
            if (data != '$')
                    return data;

            data = this.pushbackReader.read();
            if (data != '{') {
                    this.pushbackReader.unread(data);
                    return '$';
            }
            this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());

            data = this.pushbackReader.read();
            while (data != '}') {
                    this.tokenNameBuffer.append((char) data);
                    data = this.pushbackReader.read();
            }

            this.tokenValue = this.tokenResolver.resolveToken(this.tokenNameBuffer
                            .toString());

            if (this.tokenValue == null) {
                    this.tokenValue = "${" + this.tokenNameBuffer.toString() + "}";
            }
            return this.tokenValue.charAt(this.tokenValueIndex++);

    }

    @Override
    public int read(char cbuf[]) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }


    @Override
    public int read(char cbuf[], int off, int len) throws IOException {
        int charsRead = 0;
        while (charsRead < len) {
            int nextChar = read();
            if (nextChar == -1) { // EOF?
                if (charsRead == 0) {
                    return -1; // none read
                }
                break;
            }
            cbuf[off + (charsRead++)] = (char) nextChar;
        }
        return charsRead;
    }

    @Override
    public void close() throws IOException {
        this.pushbackReader.close();
    }

    @Override
    public long skip(long n) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override
    public boolean ready() throws IOException {
        return this.pushbackReader.ready();
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
}
