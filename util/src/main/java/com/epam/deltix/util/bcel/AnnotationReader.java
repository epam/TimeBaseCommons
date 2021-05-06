package com.epam.deltix.util.bcel;

import java.io.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.*;

public class AnnotationReader implements AttributeReader
{
    private AnnotationReader () { }
    
    public static final AnnotationReader    INSTANCE = new AnnotationReader ();
    
    public static void      install () {
        Attribute.addAttributeReader ("RuntimeInvisibleAnnotations", INSTANCE);
        Attribute.addAttributeReader ("RuntimeVisibleAnnotations", INSTANCE);
    }
    
    @Override
	public Attribute createAttribute (int name_index, int length, DataInputStream in, ConstantPool cp)
	{
		try {
			short numAnnotations = in.readShort();

			Annotation [] a = new Annotation [numAnnotations];
			for (int i = 0; i < numAnnotations; i++) {
				a[i] = readAnnotation(in, cp);
			}

			return new AnnotationsAttribute(Constants.ATTR_UNKNOWN, name_index, length, cp, a);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Annotation readAnnotation (DataInputStream in, ConstantPool cp)
		throws IOException
	{
		short           typeIndex = in.readShort();
        short           numMVPairs = in.readShort ();
        
		Annotation      a = new Annotation ();
        
        a.type = cp.constantToString (cp.getConstant (typeIndex));
        a.elements = new AnnotationElement [numMVPairs];
        
		for (int i = 0; i < numMVPairs; i++) {
			short       nameIndex = in.readShort();
			
            AnnotationElement   e = new AnnotationElement ();
            
            e.name = cp.constantToString (cp.getConstant (nameIndex));
			e.value = readMemberValue (in, cp);
            
            a.elements [i] = e;
		}

		return (a);
	}

	public Object readMemberValue (DataInputStream in, ConstantPool cp)
		throws IOException
	{
		byte tag = in.readByte();
		switch(tag) {
			case 'B': case 'C': case 'D': case 'J':
            case 'F': case 'I': case 'S': case 'Z':
				return ((ConstantObject)cp.getConstant(in.readShort())).getConstantValue(cp);

			case 's':
                return cp.constantToString (cp.getConstant (in.readShort ()));
                
			case 'c':
            	return new ClassValue (cp.constantToString (cp.getConstant (in.readShort ())));

            case '[': {
                short numElems = in.readShort ();
                Object [] arr = new Object [numElems];
                for (int ii = 0; ii < numElems; ii++)
                    arr [ii] = readMemberValue (in, cp);
                return (arr);
            }
                                
            case 'e':
                return new EnumValue (
                    cp.constantToString (cp.getConstant (in.readShort ())),
                    cp.constantToString (cp.getConstant (in.readShort ()))
                );
                
            case '@':  
                return readAnnotation (in, cp);
            default:
                throw new UnsupportedOperationException("tag = " + (char) tag);
	      }
	}
}

