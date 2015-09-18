package deltix.util.collections;

/**
 * Similar to ByteArrayList but not resizable (although supports flyweight pattern)
 */
public final class ByteArray {

    private byte[] array;
    private int offset;
    private int length;

    public ByteArray() {
        array = new byte[0];
    }

    public ByteArray(byte [] array) {
        this.array = array;
        offset = 0;
        length = array.length;
    }

    public ByteArray(byte [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    public ByteArray(int length) {
        array = new byte[length];
        offset = 0;
        this.length = length;
    }


    public void setArray(byte [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    public byte get(int index) {
        int i = index + offset;
        if (index > length)
            throw new ArrayIndexOutOfBoundsException(index);
        return array[index + offset];
    }

    public byte[] getArray() {
        return array;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
