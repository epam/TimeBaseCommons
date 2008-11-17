package deltix.util.collections;

/**
 *
 */
public class ByteQueue {
    private final int           capacity;
    private final byte []       buffer;
    private int                 size = 0;
    private int                 head = 0;
    private int                 tail = 0;
    
    public ByteQueue (int capacity) {
        this.capacity = capacity;
        buffer = new byte [capacity];
    }
    
    public void                 offer (byte value) {
        assert size < capacity;
        
        buffer [tail] = value;
        
        size++;
        tail++;
        
        if (tail == capacity)
            tail = 0;                
    }
    
    public void                 offer (byte [] src, int offset, int length) {
        assert size + length < capacity;
        
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
        assert size > 0;
        
        byte                value = buffer [head];
        
        size--;
        head++;
        
        if (head == capacity)
            head = 0;
        
        return (value);
    }
    
    public void                 poll (byte [] dest, int offset, int length) {
        assert size >= length;
        
        int                 end = head + length;
        int                 excess = end - capacity;
        
        if (excess > 0) {
            int             n = capacity - head;
            
            System.arraycopy (buffer, head, dest, offset, n);
            
            head = length - n;
            
            System.arraycopy (buffer, 0, dest, offset + n, head);
        }
        else {
            System.arraycopy (buffer, head, dest, offset, length);
            head = excess == 0 ? 0 : end;
        }
        
        size -= length;
    }
    
    public void                 skip (int length) {
        assert size >= length;
        
        size -= length;
        head += length;
        
        if (head > capacity)
            head -= capacity;
    }
    
    public void                 clear () {
        size = 0;
        head = 0;
        tail = 0;
    }
    
    public final boolean        isEmpty () {
        return (size == 0);
    }
    
    public final boolean        isFull () {
        return (size == capacity);
    }
    
    public final int            size () {
        return (size);
    }  
    
    public final int            free () {
        return (capacity - size);
    }
    
    public final int            capacity () {
        return (capacity);
    }  
}
