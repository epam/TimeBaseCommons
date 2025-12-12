package com.epam.deltix.util.collections;

/**
 *
 */
public class GapByteQueue {

    protected int                   capacity;
    protected byte []               buffer;
    protected int                   size = 0;
    protected int                   head = 0;
    protected int                   tail = 0;

    private final Gaps              gaps;

    private class Gap extends QuickList.Entry {

        int offset;
        int length;

        private Gap(int start, int length) {
            this.offset = start;
            this.length = length;

            assert offset >= 0 && offset < capacity;
            assert length > 0;
        }

        boolean contains(int pos) {
            int end = offset + length;
            if (end < capacity)
                return pos >= offset && pos < end;

            return pos >= offset || pos < (end % capacity);
        }
    }

    private class Gaps {

        private final QuickList<Gap> gaps = new QuickList<Gap>();

        int available() {
            Gap first = gaps.getFirst();

            return first != null ? absolute(first.offset) : size;
        }

        boolean write(int offset, int length) {

            int total = total();

            int space = 0; // space used by gaps

            // find gap which contains data block
            Gap next = gaps.getFirst();
            while (next != null && !next.contains(offset)) {
                space += next.length;
                next = (Gap) next.next();
            }

            if (next == null) { // gap containing data block does not exists
                if (total > space + size)
                    gaps.linkLast(new Gap((head + space + size - length) % capacity, total - space - size));

                return gaps.isEmpty() || available() > 0;
            }

            if (next.offset == offset) { // left side check
                if (next.length <= length) {
                    next.unlink();
                } else {
                    next.length = next.length - length;
                    next.offset = (next.offset + length) % capacity;
                }
            } else if (next.offset > offset) {
                int off = offset - next.offset + capacity;
                if (off + length == next.length) { // right side check
                    next.length -= length;
                } else {
                    int len = next.length;
                    next.length = off;

                    if (len - next.length - length > 0) // new gap inside this one
                        QuickList.linkAfter(next, new Gap(offset + length, len - next.length - length));
                }
            } else {
                if ((offset - next.offset + length) == next.length) { // right side check
                    next.length -= length;
                } else {
                    int len = next.length;
                    next.length = offset - next.offset;
                    if (len - next.length - length > 0) { // new gap inside this one
                        Gap gap = new Gap((offset + length) % capacity, len - next.length - length);
                        QuickList.linkAfter(next, gap);
                    }
                }
            }

//            int total = total();
//
//            next = gaps.getFirst();
//            while (next != null) {
//                total -= next.length;
//                next = (Gap) next.next();
//            }
//
//            assert total == size;

            return available() > 0;
        }
    }

    public GapByteQueue (int capacity) {
        this.capacity = capacity;
        this.buffer = new byte [capacity];
        this.gaps = new Gaps();
    }

    private int                     absolute(int offset) {
        return (offset - head < 0 ? capacity : 0) + (offset - head);
    }

    private int                     total() {
        return (tail - head <= 0 && size > 0 ? capacity : 0) + (tail - head);
    }

    public boolean                  write (byte [] src, int offset, int length, int position) {
        assert size + length <= capacity :
                "size: " + size + "; length: " + length + "; capacity: " + capacity;

        assert position >= 0 && position < capacity;

        int                 end = position + length;
        int                 excess = end - capacity;

        boolean overhead = absolute(position) + length > total();

        if (excess > 0) {
            int             n = capacity - position;

            System.arraycopy (src, offset, buffer, position, n);
            System.arraycopy (src, offset + n, buffer, 0, length - n);

            tail = overhead ? length - n : tail;
        }
        else {
            System.arraycopy (src, offset, buffer, position, length);
            tail = overhead ? (excess == 0 ? 0 : end) : tail;
        }

        size += length;

        assert tail >= 0 && tail <= capacity;

        return gaps.write(position, length);
    }

    public int                  available() {
        return gaps.available();
    }

    public byte                 poll () {
        assert size > 0 : "size: " + size;

        byte                value = buffer [head];

        size--;
        head++;

        if (head == capacity)
            head = 0;

        return (value);
    }

    public void                 poll (byte [] dest, int offset, int length) {
        assert size >= length : "size: " + size + "; length: " + length;

        int                 end = head + length;
        int                 excess = end - capacity;

        if (excess > 0) {
            int             n = capacity - head;

            if (dest != null) {
                System.arraycopy (buffer, head, dest, offset, n);
                System.arraycopy (buffer, 0, dest, offset + n, excess);
            }

            head = excess;
        }
        else {
            if (dest != null)
                System.arraycopy (buffer, head, dest, offset, length);

            head = excess == 0 ? 0 : end;
        }

        size -= length;
    }

    public int                 skip (int length) {
        assert size >= length : "size: " + size + "; length: " + length;

        int                 end = head + length;
        int                 excess = end - capacity;

        if (excess > 0)
            head = excess;
        else
            head = excess == 0 ? 0 : end;

        size -= length;
        return length;
    }

    public void                 clear () {
        size = 0;
        head = 0;
        tail = 0;
    }

    public int            size () {
        return available();
    }

    public int              capacity() {
        return capacity;
    }
}
