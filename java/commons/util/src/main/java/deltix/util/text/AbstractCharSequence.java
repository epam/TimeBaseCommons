package deltix.util.text;

import deltix.util.lang.Util;
import deltix.util.collections.CharSubSequence;

/**
 *
 */
public abstract class AbstractCharSequence implements CharSequence {
    @Override
    public boolean          equals (Object obj) {
        return (Util.equals (this, (CharSequence) obj));
    }

    @Override
    public int              hashCode () {
        return (Util.hashCode (this));
    }

    @Override
    public String           toString () {
        return (Util.toString (this));
    }

    public CharSequence     subSequence (int start, int end) {
        return (new CharSubSequence (this, start, end));
    }
}
