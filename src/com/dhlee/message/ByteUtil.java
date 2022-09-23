package com.dhlee.message;

public class ByteUtil {
    public static byte[] cut(byte[] srcBytes, int length) {
        if (srcBytes == null) {
            return null;
        }
        
        if (srcBytes.length < length) {
        	StringBuffer sb = new StringBuffer();
        	sb.append("정의한 길이=" + length + ", 받은 길이=" + srcBytes.length);
        	throw new RuntimeException(sb.toString());        	
        }

        byte[] result = new byte[length];

        for (int i = 0; i < length; i++)
            result[i] = srcBytes[i];

        return result;
    }
    
    public static byte[] cut(byte[] srcBytes, int start, int length) {
        if (srcBytes == null) {
            return null;
        }
        
        if (srcBytes.length < (start+length)) {
        	StringBuffer sb = new StringBuffer();
        	sb.append("item start index  =" + start + ", item length =" + length + ", total size =" + srcBytes.length);
        	throw new RuntimeException(sb.toString());        	
        }

        byte[] result = new byte[length];
        int end = start + length;
        int p=0;
        for (int i = start; i < end; i++) {
            result[p] = srcBytes[i];
            p++;
        }
        return result;
    }
    
    public static byte[] sub(byte[] srcBytes, int offset) {
        if (srcBytes == null) {
            return null;
        }

        byte[] result = new byte[srcBytes.length - offset];
        System.arraycopy(srcBytes, offset, result, 0, result.length);
        return result;
    }

    public static byte[] padding(String src, int length) {
        return padding(src.getBytes(), length);
    }

    public static byte[] padding(byte[] srcBytes, int length) {
        return padding(srcBytes, length, (byte)0x20);
    }
    
    public static byte[] padding(String src, int length, byte pad) {
        return padding(src.getBytes(), length, pad);
    }
    
    public static byte[] padding(byte[] srcBytes, int length, byte pad) {
        if (srcBytes == null) {
            srcBytes = new byte[0];
        }
        
        if (length < 0) throw new RuntimeException("length=" + length);
        
        byte[] result = new byte[length];
        
        for (int i=0; i<length; i++)
            result[i] = pad;

        for (int i = 0;
                i < ((srcBytes.length < length) ? srcBytes.length : length);
                i++)
            result[i] = srcBytes[i];

        return result;
    }
    
    public static byte[] padding(byte[] srcBytes, int length, byte pad, boolean isNumber) {
    	if(isNumber) {
    		return paddingleft(srcBytes, length, pad);
    	}
    	else {
    		return padding(srcBytes, length, pad);
    	}
    }
    
    public static byte[] padding(String src, int length, byte pad, boolean isNumber) {
    	if(isNumber) {
    		return paddingleft(src.getBytes(), length, pad);
    	}
    	else {
    		return padding(src.getBytes(), length, pad);
    	}
    }
    
    public static byte[] paddingleft(byte[] srcBytes, int length, byte pad) {
        if (srcBytes == null) {
            srcBytes = new byte[0];
        }
        
        if (length < 0) throw new RuntimeException("length=" + length);
        
        byte[] result = new byte[length];
		
		int spaces = length - srcBytes.length;
		
		if(spaces > 0) {
			for(int i = 0; i <spaces ; i++) {
				result[i] = pad;
			}
			System.arraycopy(srcBytes, 0, result, spaces, srcBytes.length);			
		}
		else if(spaces < 0) {
			System.arraycopy(srcBytes, -spaces, result, 0, length);
			return result;
		}
		else {
			return srcBytes;
		}
		return result;
    }
    
    public static byte[] merge(byte[] src1, byte[] src2) {
        if ((src1 == null) && (src2 == null)) {
            return new byte[0];
        }

        if (src1 == null) {
            return src2;
        }

        if (src2 == null) {
            return src1;
        }

        byte[] result = new byte[src1.length + src2.length];

        System.arraycopy(src1, 0, result, 0, src1.length);
        System.arraycopy(src2, 0, result, src1.length, src2.length);
        return result;
    }

    public static byte[] copy(byte[] src) {
        if (src == null) {
            return null;
        }

        byte[] result = new byte[src.length];
        System.arraycopy(src, 0, result, 0, src.length);

        return result;
    }
    
}

