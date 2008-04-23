package deltix.util.text;

/**
 *
 */
public interface WordMatcher {
    public boolean      matches (final byte [] bytes, int offset, int len);
    
    public boolean      matches (CharSequence s);
    
    public boolean      matches (CharSequence s, int offset, int len);
}
