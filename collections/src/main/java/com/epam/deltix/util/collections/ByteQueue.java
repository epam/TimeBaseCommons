package com.epam.deltix.util.collections;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Fixed size circular buffer of byte values
 */
public final class ByteQueue implements ByteContainer {
    private int                 capacity;
    private byte []             buffer;
    private int                 size = 0;
    private int                 head = 0;
    private int                 tail = 0;
    
    public ByteQueue (int capacity) {
        this.capacity = capacity;
        buffer = new byte [capacity];
    }
    
    public void                 offer (int value) {
        offer ((byte) value);
    }

    public void                 offer (long value) {
        offer ((byte) value);
    }

    public void                 offer (byte value) {
        assert size < capacity :
            "size: " + size + "; capacity: " + capacity;
        
        buffer [tail] = value;
        
        size++;
        tail++;
        
        if (tail == capacity)
            tail = 0;                
    }

//    public void                 insert (byte [] src, int offset, int length) {
//        if (capacity - size < length) {
//            // increase buffer
//            if (tail > head) {
//                byte[] temp = new byte[capacity + length];
//                System.arraycopy (buffer, head, temp, length + head, size);
//                buffer = temp;
//            } else {
//                byte[] temp = new byte[capacity + length];
//                System.arraycopy (buffer, 0, temp, 0, tail);
//                System.arraycopy (buffer, head, temp, capacity - head + length, capacity - head);
//                buffer = temp;
//            }
//            capacity += length;
//        }
//
//        if (head > length) {
//            System.arraycopy (src, offset, buffer, head - length, length);
//            head -= length;
//        } else {
//            int remains = length - head;
//            System.arraycopy (src, offset, buffer, capacity - remains, remains);
//            System.arraycopy (src, offset + remains, buffer, 0, head);
//
//            head = capacity - remains;
//        }
//        size += length;
//
//    }
    
    public void                 offer (byte [] src, int offset, int length) {
        assert size + length <= capacity :
            "size: " + size + "; length: " + length + "; capacity: " + capacity;
        
        int                 end = tail + length;
        int                 excess = end - capacity;
        
        if (excess > 0) {
            int             n = capacity - tail;
            
            System.arraycopy (src, offset, buffer, tail, n);
            
            tail = length - n;
            
            System.arraycopy (src, offset + n, buffer, 0, tail);
        }
        else {
            System.arraycopy (src, offset, buffer, tail, length);        
            tail = excess == 0 ? 0 : end;
        }  
        
        size += length;
    }

    public void                 poll(OutputStream out) throws IOException {
        int end = head + size;

        if (end - capacity > 0) {
            out.write(buffer, head, capacity - head);
            out.write(buffer, 0, tail);
        } else {
            out.write(buffer, head, tail - head);
        }

        clear();
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

    @Override
    public byte                 get (int srcOffset) {
        assert srcOffset < size : "srcOffset: " + srcOffset + "; size: " + size;

        return (buffer [logicalToInternal (srcOffset)]);
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
    
    public void                 get (int srcOffset, byte [] dest, int destOffset, int length) {
        assert srcOffset + length <= size :
            "size: " + size + "; srcOffset: " + srcOffset + "; length: " + length;

        int                 start = logicalToInternal (srcOffset);
        int                 end = start + length;
        int                 excess = end - capacity;

        if (excess > 0) {
            int             n = capacity - start;

            System.arraycopy (buffer, start, dest, destOffset, n);
            System.arraycopy (buffer, 0, dest, destOffset + n, excess);
        }
        else
            System.arraycopy (buffer, start, dest, destOffset, length);
    }

    public void                 skip (int length) {
        assert size >= length && length >= 0 : "skip length " + length + " > size: " + size;
        
        size -= length;
        head += length;
        
        if (head >= capacity)
            head -= capacity;
    }
    
    public void                 clear () {
        size = 0;
        head = 0;
        tail = 0;
    }
    
    public boolean        isEmpty () {
        return (size == 0);
    }
    
    public boolean        isFull () {
        return (size == capacity);
    }
    
    public int            size () {
        return (size);
    }  
    
    public int            free () {
        return (capacity - size);
    }
    
    public byte []              getBuffer () {
        return buffer;
    }

    public int                  getHead () {
        return head;
    }

    public int                  getTail () {
        return tail;
    }    

    public int                  capacity() {
        return capacity;
    }

    public void                 addCapacity(int increment) {
        setCapacity(capacity + increment);
    }

    public void                 truncate(int length) {
        assert length < size;
        int newTail = tail - length;
        if (newTail < 0)
            newTail =+ capacity;
        
        size -= length;
        tail = newTail;
    }

    public boolean              setCapacity (int value) {
        if (capacity == value)
            return false; 

        assert(value > capacity);
        capacity = value;
        
        byte[] previous = buffer;
        buffer = new byte[capacity];
        if (tail > head) {
            System.arraycopy (previous, head, buffer, 0, size);
        } else {
            System.arraycopy (previous, head, buffer, 0, previous.length - head);
            System.arraycopy (previous, 0, buffer, previous.length - head, tail);
        }
        
        head = 0;
        tail = size;

        return true;
    }
}
