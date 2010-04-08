package deltix.util.collections;

/**
 * Fixed size circular buffer of byte values
 */
public final class ByteQueue {
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
        assert size >= length : "size: " + size + "; length: " + length;
        
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

    public int                  getCapacity () {
        return capacity;
    }

    public boolean              setCapacity (int value) {
        if (capacity == value)
            return false; 

        capacity = value;
        
        byte[] previous = buffer;
        buffer = new byte[capacity];
        if (tail > head) {
            System.arraycopy (previous, head, buffer, 0, size);
        }
        else {
            System.arraycopy (previous, head, buffer, 0, previous.length - head);
            System.arraycopy (previous, 0, buffer, previous.length - head, tail);
            head = 0;
            tail = size;
        }

        return true;
    }
}
