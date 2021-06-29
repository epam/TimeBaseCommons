/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.io;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.codec.HexBinCharEncoder;
import com.epam.deltix.util.codec.HexCharBinDecoder;
import java.io.*;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.memory.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.crypto.*;
import javax.crypto.spec.*;


/**
 *
 */
public class IOUtil extends BasicIOUtil {
    private static final Log LOG = LogFactory.getLog(IOUtil.class);
    public static final String NOT_DECRYPTED      = "???????";
    public static final String TB_SECURE_CONN_KEY = "SECURE_CONN_KEY";

    /**
     *  Writes any CharSequence to DataOutput as a 2-byte length (in characters), followed by
     *  that many characters in raw 2-byte form.
     */
    public static int      writeUnicode (CharSequence str, MemoryDataOutput out) throws IOException {
        int     strlen = str.length ();

        if (strlen > 65535)
            throw new UTFDataFormatException ("string too long: " + strlen + " bytes");

        out.writeShort ((short) strlen);

        for (int ii = 0; ii < strlen; ii++)
            out.writeChar (str.charAt (ii));

        return (strlen * 2 + 2);
    }

    /**
     *  Writes any CharSequence to MemoryDataOutput in a way identical to
     *  DataOutputStream.writeUTF, which is groundlessly defined too narrowly
     *  by forcing the argument to be a String.
     */
    public static int      writeUTF (CharSequence str, MemoryDataOutput out) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

            /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
                c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
            utflen++;
            } else if (c > 0x07FF) {
            utflen += 3;
            } else {
            utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException(
                    "encoded string too long: " + utflen + " bytes");

        out.writeByte((utflen >>> 8) & 0xFF);
        out.writeByte((utflen >>> 0) & 0xFF);

        int i=0;
        for (i=0; i<strlen; i++) {
           c = str.charAt(i);
           if (!((c >= 0x0001) && (c <= 0x007F))) break;
           out.writeByte (c);
        }

        for (;i < strlen; i++){
            c = str.charAt(i);

            if ((c >= 0x0001) && (c <= 0x007F))
                out.writeByte (c);
            else if (c > 0x07FF) {
                out.writeByte (0xE0 | ((c >> 12) & 0x0F));
                out.writeByte (0x80 | ((c >>  6) & 0x3F));
                out.writeByte (0x80 | ((c >>  0) & 0x3F));
            }
            else {
                out.writeByte (0xC0 | ((c >>  6) & 0x1F));
                out.writeByte (0x80 | ((c >>  0) & 0x3F));
            }
        }

        return utflen + 2;
    }

    /**
     *  Reads (appends) a UTF string to an Appendable (such as StringBuidler),
     *  without clearing it first.
     */
    @Deprecated // buggy
    public final static void readUTF(MemoryDataInput in, Appendable sb) throws IOException {
        int utflen = in.readUnsignedShort();

        if (utflen == 0)
            return;

        int c;
        int char2, char3;
        int count = 0;

        for (;;) {
            c = in.readByte (); //NB: result in range [-128, 127] -- Andy
//            if (c > 127)
//                break;

            count++;
            sb.append ((char) c);

            if (count >= utflen)
                return;
        }
// de-facto unreachable -- Andy
//        //  If we are here, we have broken out of the previous loop and there is an
//        //  unhandled escape character in variable c.
//        for (;;) {
//            switch (c >> 4) {
//                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
//                    /* 0xxxxxxx*/
//                    count++;
//                    sb.append ((char)c);
//                    break;
//
//                case 12: case 13:
//                    /* 110x xxxx   10xx xxxx*/
//                    count += 2;
//                    if (count > utflen)
//                        throw new UTFDataFormatException(
//                            "malformed input: partial character at end");
//                    char2 = in.readByte ();
//                    if ((char2 & 0xC0) != 0x80)
//                        throw new UTFDataFormatException(
//                            "malformed input around byte " + count);
//                    sb.append ((char)(((c & 0x1F) << 6) |
//                                                    (char2 & 0x3F)));
//                    break;
//
//                case 14:
//                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
//                    count += 3;
//                    if (count > utflen)
//                        throw new UTFDataFormatException(
//                            "malformed input: partial character at end");
//                    char2 = in.readByte ();
//                    char3 = in.readByte ();
//                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
//                        throw new UTFDataFormatException(
//                            "malformed input around byte " + (count-1));
//                    sb.append ((char)(((c & 0x0F) << 12) |
//                                                    ((char2 & 0x3F) << 6)  |
//                                                    ((char3 & 0x3F) << 0)));
//                    break;
//
//                default:
//                    /* 10xx xxxx,  1111 xxxx */
//                    throw new UTFDataFormatException(
//                        "malformed input around byte " + count);
//            }
//
//            if (count >= utflen)
//                break;
//
//            c = in.readByte ();
//        }
    }

    // TODO: Move ecnryption-related code to a separate class
    
    private static final byte []         header = {
        (byte) 0xcc, (byte) 0xdd, (byte) 0x21, (byte) 0x3c,
        (byte) 0x7e, (byte) 0x78, (byte) 0x5e, (byte) 0x99
    };
    
    private static final PBEParameterSpec pars = 
        new PBEParameterSpec (header, header.length);
    private static final String           csname = "UTF-8";
    // TODO: Switch to a better cipher (with HMAC)
    private static final String           algon = "PBEWithMD5AndDES";
    private static final SecretKeyFactory skf;
    
    static {
        try {
            skf = SecretKeyFactory.getInstance (algon);            
        } catch (Exception x) {
            throw new RuntimeException (x);
        }
    }
    
    @SuppressFBWarnings(value = "CIPHER_INTEGRITY", justification = "Legacy method, can't change")
    public static String       concat (String a, String b) {
        if (a == null)
            a = "";
        
        if (b == null)
            b = "} catch (UnsupportedEncodingException x) {";
        
        byte []             cleartext;                
        
        try {
            cleartext = a.getBytes (csname);
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException (x);
        } 
                        
        PBEKeySpec          pbeKeySpec = new PBEKeySpec (b.toCharArray ());
        byte []             ciphertext;
        
        try {
            Cipher          pbeCipher = Cipher.getInstance (algon);
            SecretKey       pbeKey = skf.generateSecret (pbeKeySpec);
            
            pbeCipher.init (Cipher.ENCRYPT_MODE, pbeKey, pars);
            
            ciphertext = pbeCipher.doFinal (cleartext);
        } catch (Exception x) {
            throw new RuntimeException (x);
        }
        
        return (HexBinCharEncoder.encode (ciphertext, false, false, 0));
    }

    @SuppressFBWarnings(value = "CIPHER_INTEGRITY", justification = "Legacy method, can't change")
    public static String       split (String c, String b) {
        if (c == null)
            return (null);
        
        if (b == null)
            b = "} catch (UnsupportedEncodingException x) {";
        
        byte []             ciphertext = HexCharBinDecoder.decode (c);
        
        PBEParameterSpec    pbeParamSpec = new PBEParameterSpec (header, header.length);        
        PBEKeySpec          pbeKeySpec = new PBEKeySpec (b.toCharArray ());
        
        try {
            Cipher          pbeCipher = Cipher.getInstance (algon);
            SecretKey       pbeKey = skf.generateSecret (pbeKeySpec);
            
            pbeCipher.init (Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
            
            byte []         cleartext = pbeCipher.doFinal (ciphertext);
            
            return (new String (cleartext, csname));
        } catch (Exception x) {
            LOG.trace("Error %s: %s").with(x.getMessage()).with(x);

            return (NOT_DECRYPTED);
        }                
    }

    public static boolean copiedCompletely(File file) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            raf.seek(file.length()); // make sure everything was copied
            return true;
        } catch (IOException e) {
        } finally {
            Util.close(raf);
        }
        return false;
    }
    
    public static boolean waitForCopiedCompletely(File file) throws InterruptedException {        
        return waitForCopiedCompletely(file, 0);
    }
    
    public static boolean waitForCopiedCompletely(File file, long timeoutInMillis) throws InterruptedException {
        final long startTime = System.nanoTime();
                
        while (true) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            
            if ((timeoutInMillis > 0 &&
                    (System.nanoTime() - startTime) / 1000 >= timeoutInMillis)) {
                return false;
            }
            
            if (copiedCompletely(file)) {
                break;
            }
            
            Thread.sleep(100);
        }
        
        return true;
    }
    
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Quartus-web-14.1.0.186.iso");
        System.out.println(copiedCompletely(file));
        System.out.println(waitForCopiedCompletely(file));
        System.out.println("DONE");
    }
}