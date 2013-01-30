package deltix.util.collections;

import java.util.LinkedList;

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

            assert length > 0;
        }
        
        boolean contains(int pos) {
            int end = offset + length;
            if (end < capacity)
                return pos >= offset && pos < end;

            return pos >= offset || pos < end % capacity;
        }
    }

    private class Gaps {

        private final QuickList<Gap> gaps = new QuickList<Gap>();

        int available() {
            Gap first = gaps.getFirst();
            if (first != null)
                return absolute(first.offset);

            return size;
        }

        boolean write(int offset, int length) {

            Gap next = gaps.getFirst();

            if (next == null) {
                int total = (tail - head <= 0 ? capacity : 0) + (tail - head);

                if (total != size) {
                    gaps.linkFirst(new Gap((head + size - length) % capacity, total - size));
                    return size > length;
                }

                return size > 0;
            }

            // find gap
            while (next != null && !next.contains(offset))
                next = (Gap) next.next();

            assert next != null;

            if (next.offset == offset) { // left side check
                if (next.length == length) {
                    next.unlink();
                } else {
                    next.length = next.length - length;
                    next.offset = (next.offset + length) % capacity;
                }
            } else if (next.offset > offset) {
                int off = offset - next.offset + capacity;
                if (off + length == next.length) { // right side check
                    next.length -= length;
                } else { // new gap inside this one
                    int len = next.length;
                    next.length = off;
                    QuickList.linkAfter(next, new Gap(offset + length, len - next.length - length));
                }
            } else  {
                if ((offset - next.offset + length) == next.length) { // right side check
                    next.length -= length;
                } else { // new gap inside this one
                    int len = next.length;
                    next.length = offset - next.offset;

                    Gap gap = new Gap((offset + length) % capacity, len - next.length - length);
                    QuickList.linkAfter(next, gap);
                }
            }

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

    public boolean                  write (byte [] src, int offset, int length, int position) {
        assert size + length <= capacity :
                "size: " + size + "; length: " + length + "; capacity: " + capacity;

        int oldTail = tail;

        assert position >= 0 && position < capacity;

        int                 end = position + length;
        int                 excess = end - capacity;

        boolean overhead = absolute(position) + length > absolute(tail);

        if (excess > 0) {
            int             n = capacity - position;

            System.arraycopy (src, offset, buffer, position, n);
            System.arraycopy (src, offset + n, buffer, 0, length - n);

            tail = overhead ? length - n : tail;
        }
        else {
            System.arraycopy (src, offset, buffer, position, length);
            tail = overhead ? (excess == 0 ? 0 : end) : tail;

//            if (excess == 0)
//                tail = 0;
//            else
//                tail = overhead ? end : tail;
        }
        if (oldTail < tail)
            assert true;

        assert tail >= 0 && tail <= capacity;
        size += length;

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

    /**
     *  Equivalent to (head + offset) % capacity for 0 &lt;= offset &lt; capacity
     *  but a bit faster.
     */
    private int                 logicalToInternal (int srcOffset) {
        int     ret = head + srcOffset;

        if (ret >= capacity)
            ret -= capacity;

        return (ret);
    }

    public void                 clear () {
        size = 0;
        head = 0;
        tail = 0;
    }

    public boolean        isEmpty () {
        return (size == 0);
    }

    public int            size () {
        return available();
    }
}
