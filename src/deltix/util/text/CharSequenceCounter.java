package deltix.util.text;

import java.util.Arrays;

/** CharSequence that contains zero-padded counter */
public class CharSequenceCounter implements CharSequence {
    final char [] counter = new char [12];

    public CharSequenceCounter () {
        Arrays.fill(counter, '0');
    }

    public void increment() {
        int i = counter.length - 1;
        while (true) {
            char c = counter[i];
            // increment character at index i
            if (c < '9') {
                counter[i] = (char) (c + 1);
                break;
            } else {
                counter[i] = '0';
            }
            if (--i == 0)
                throw new ArithmeticException("Overflow");
        }
    }


    @Override
    public int length() {
        return counter.length;
    }

    @Override
    public char charAt(int index) {
        return counter[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return new String (counter);
    }
}
