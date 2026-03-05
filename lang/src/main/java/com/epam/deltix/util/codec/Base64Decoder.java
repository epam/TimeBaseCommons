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
package com.epam.deltix.util.codec;

import java.io.*;

/**
 * See RFC1521.
 */
public class Base64Decoder extends CharBinDecoder {
	private int			spill = 0;
	private int			mPos = 0;
	
	public Base64Decoder (OutputStream os) {
		super (os);
	}
	
    private void    output (int byt) throws IOException {
        mOutputStream.write (byt);
    }
    
	private void	decode6 (int sixBit) throws IOException {
		switch (mPos) {
			case 0:
				spill = sixBit << 2;
				mPos = 1;
				break;
			
			case 1:
				output (spill | (sixBit >> 4));
				spill = (sixBit & 0xF) << 4;
				mPos = 2;
				break;
				
			case 2:
				output (spill | (sixBit >> 2));
				spill = (sixBit & 0x3) << 6;
				mPos = 3;
				break;
				
			case 3:
				output (spill | sixBit);
				spill = 0;
				mPos = 0;
				break;
			
			case 4:
				throw new CharConversionException (
					"Index characters after the padding (=) are illegal."
				);
				
			default:
				throw new RuntimeException ("Illegal state");
		}
	}
	
	private void	pad () throws IOException {
		switch (mPos) {
			case 0:
				throw new CharConversionException (
					"Padding (=) in 1st position is illegal."
				);
				
			case 1:
				throw new CharConversionException (
					"Padding (=) in 2nd position is illegal."
				);
				
			case 2:
				if (spill != 0)
					throw new CharConversionException (
						"Padding (=) in 3rd position is illegal after non-0 bits (" +
						(spill >> 4) + ")."
					);

				mPos = 3;
				break;
				
			case 3:
				if (spill != 0)
					throw new CharConversionException (
						"Padding (=) in 3rd position is illegal after non-0 bits (" +
						(spill >> 6) + ")."
					);
					
				mPos = 4;
				break;
			
			default:
				throw new RuntimeException ("Illegal state");
		}
	}
	
    private void	decode (char ch) throws IOException {
    	int				diff = ch - 'A';
    	
    	if (diff >= 0 && diff <= 25) {
    		decode6 (diff);
    		return;
    	}
    	
    	diff = ch - 'a';
    	
    	if (diff >= 0 && diff <= 25) {
    		decode6 (diff + 26);
    		return;
    	}
    	
    	diff = ch - '0';
    	
    	if (diff >= 0 && diff <= 9) {
    		decode6 (diff + 52);
    		return;
    	}
    	
    	switch (ch) {
    		case '+':	decode6 (62);	return;
    		case '/':	decode6 (63);	return;
    		case '=':	pad ();			return;
    		default:	/*skip*/		return;
    	}
    }

    public void 	write (char cbuf [], int off, int len) throws IOException {
    	for (int ii = 0; ii < len; ii++)
    		decode (cbuf [off + ii]);
    }    
    
    public static void main (String [] args) throws Exception {
        Base64Decoder			dec = new Base64Decoder (System.out);
    		
        for (;;) {
            int     ch = System.in.read ();
            
            if (ch < 0)
                break;
            
            dec.write (ch);
        }
        
        dec.flush ();
    }
}