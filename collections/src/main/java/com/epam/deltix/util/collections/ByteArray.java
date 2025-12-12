package com.epam.deltix.util.collections;

/**
 * Similar to ByteArrayList but not resizable (although supports flyweight pattern)
 */
public final class ByteArray {

    private byte[] array;
    private int offset;
    private int length;

    public ByteArray() {
        //array = new byte[0];
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
        if (index > length)
            throw new ArrayIndexOutOfBoundsException(index);
        return array[index + offset];
    }

    public void put(int index, int b) {
        if (index > length)
            throw new ArrayIndexOutOfBoundsException(index);
        array[index + offset] = (byte) b;
    }

    public int         getOffset(int local) {
        return offset + local;
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

    public boolean isIdentical(ByteArray other) {
        if (length != other.length)
            return false;

        for (int i=0; i < length; i++) {
            if (get(i) != other.get(i))
                return false;
        }
        return true;
    }

    public void copyTo (int srcPos, byte [] dest, int destPos, int length) {
        System.arraycopy(array, offset + srcPos, dest, destPos, length);
    }

    public void copyFrom (byte [] src, int srcPos, int destPos, int length) {
        System.arraycopy(src, srcPos, array, offset + destPos, length);
    }

    public static void arraycopy(ByteArray src, int srcPos, ByteArray dest, int destPos, int length) {
        System.arraycopy(src.getArray(), src.getOffset(srcPos), dest.getArray(), dest.getOffset(destPos), length);
    }

    public void fill(byte b) {
        for (int i=0; i < length; i++)
            array[i + offset] = b;
    }
}
