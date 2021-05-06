package com.epam.deltix.util.codec;

import java.io.*;

public class Base64Encoder extends BinCharEncoder {
     private final static char DICTIONARY [] = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
          'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
          'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
          'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
          'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
          'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
          'w', 'x', 'y', 'z', '0', '1', '2', '3',
          '4', '5', '6', '7', '8', '9', '+', '/'
     };

    private int             mByte1;
    private int             mByte2;
    private int             mNumBytes = 0;
     
    public Base64Encoder (Writer wr) {
        super (wr);
    }
     
    private void    writeCode (int code) throws IOException {
        mWriter.write (DICTIONARY [code]);
    }

    public void     write (int byt) throws IOException {
        byt = byt & 0xFF;
        
        switch (mNumBytes) {
            case 0:
                mByte1 = byt;
                mNumBytes = 1;
                break;
                
            case 1:
                mByte2 = byt;
                mNumBytes = 2;
                break;
                
            case 2:
                writeCode (mByte1 >> 2);
                writeCode (((mByte1 & 0x3) << 4) | (mByte2 >> 4));
                writeCode (((mByte2 & 0xf) << 2) | (byt >> 6));
                writeCode (byt & 0x3f);
                mNumBytes = 0;
                break;
                
            default:
                throw new RuntimeException ();
        }
    }
  
    public void     close () throws IOException {
        switch (mNumBytes) {
            case 0:
                break;
                
            case 1:
                writeCode (mByte1 >> 2);
                writeCode ((mByte1 & 0x3) << 4);
                mWriter.write ("==");
                break;
                
            case 2:
                writeCode (mByte1 >> 2);
                writeCode (((mByte1 & 0x3) << 4) | (mByte2 >> 4));
                writeCode ((mByte2 & 0xf) << 2);
                mWriter.write ("=");
                break;
                
            default:
                throw new RuntimeException ();
        }
        
        super.close ();
    }
}