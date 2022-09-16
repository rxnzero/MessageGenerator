package com.dhlee.message;

import java.nio.ByteBuffer;
public class ByteUtil {
    public static byte[] cut(byte[] srcBytes, int length) {
        if (srcBytes == null) {
            return null;
        }
        
        if (srcBytes.length < length) {
        	StringBuffer sb = new StringBuffer();
//        	sb.append("data size가 맞지 않습니다. ");
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

        //TODO juns performance issue 
        System.arraycopy(src1, 0, result, 0, src1.length);
        System.arraycopy(src2, 0, result, src1.length, src2.length);
        return result;
    }

    public static byte[] copy(byte[] src) {
        if (src == null) {
            return null;
        }

        byte[] result = new byte[src.length];
        //TODO juns performance issue
        System.arraycopy(src, 0, result, 0, src.length);

        return result;
    }

    // 이거 아무데서도 안쓰이는거 같은데???
    public static boolean isSame(byte[] b1, int o1, byte[] b2) {
        if (b2.length > (b1.length - o1)) {
            return false;
        }

        for (int i = 0; i < b2.length; i++) {
            if (b1[o1 + i] != b2[i]) {
                return false;
            }
        }

        return true;
    }
    
    public static  int checkLengthField(byte[] buf) throws Exception {
    	int length = 0;
		ByteBuffer	buffer = ByteBuffer.wrap(buf);
		
    	if ( buffer.capacity() == 2 ) {
    		length = ( buffer.getShort(0) & 0xFFFF ); 
    	} else if ( buffer.capacity() == 4 ) {
    		length = ( buffer.getInt(0) ); 
    	} else {
    		String lenStr = new String( buffer.array() );
    		try {
    			length = Integer.parseInt( lenStr );
    		} catch ( NumberFormatException e ) {
    			throw new Exception("FL");
    		}
    	}
    	return length;    	
    }
//    public static void main(String[] args) throws Exception{
//		StringBuffer sb = new StringBuffer();
//		String text ="1234567890ABCDEFGHIJKLMNOPQRSTUVWZZYZ";
//		for(int i=0;i<100;i++){
//			sb.append(text);
//		}
//		StopWatch stopWatch = new StopWatch();
//		for(int j=0;j<100;j++){
//			stopWatch.start();
//	    	//sub
//	    	byte[] data = null;
//	    	byte[] src = sb.toString().getBytes();
//	    	for(int i=0;i<10000;i++){
//	    		data = ByteUtil.sub(src, 3);
//	    	}
//	    	//System.out.println("sub="+new String(data));
//    	
//
//		
//			stopWatch.stop();
//			System.out.println("    elapsed       : " + stopWatch.toString());
//		}
//    	
//    	//merge
//    	data = ByteUtil.merge("12345".getBytes(), "67890".getBytes());
//    	System.out.println("merge="+new String(data));
//    	
//    	//copy
//    	data = ByteUtil.copy("12345".getBytes());
//    	System.out.println("copy="+new String(data));
//    	
//    	byte[] data = ByteUtil.paddingleft("12345".getBytes(), 10, (byte)0x30);
//    	System.out.println("paddingleft="+new String(data));
//    	data = ByteUtil.paddingleft("12345".getBytes(), 5, (byte)0x30);
//    	System.out.println("paddingleft="+new String(data));
//    }
}

