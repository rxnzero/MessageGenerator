package com.dhlee.message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dom4j.Attribute;
import org.dom4j.Node;

public class FlatReader implements StandardReader {
	static boolean debug = false;
	private String FIELD_SEPARATOR = ".";
	private boolean ZERO_BASE_INDEX = true;
	
	private String encoding = "euc-kr";;
	
	public FlatReader() {
	}
	
	public StandardMessage parse(StandardMessage message, String flatString) throws Exception {
		ArrayList<StandardItem> list = message.toList();
		for(int i=0; i<list.size(); i++) {
			StandardItem item = list.get(i);
			debug( i + " - "+ item.getLevel() + " : " +item.getName());
		}
		
		byte[] srcbytes;
		try {
			srcbytes = flatString.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			srcbytes = flatString.getBytes();
		}
		
		// make json map(path, value)
		LinkedHashMap<String, String> itemMap = new LinkedHashMap<String, String>();
		
		traverse(message, message, srcbytes, null, itemMap);
		int remain = srcbytes.length - message.getReadPosition();
		if(remain > 0) {
			throw new Exception("flat message parsing error(overflow) remain size="+remain);
		}
		for(String key:itemMap.keySet()) {
			debug("~ itemMap : " + key + " => " + itemMap.get(key));
		}
		return message;		
	}
	
	private void traverse(StandardMessage message, StandardItem currentItem, byte[] srcbytes, String parentName, LinkedHashMap<String, String> itemMap){
		String fieldName = null;
		fieldName = genPath(parentName, currentItem.getName());
		int type = currentItem.getType();
		
		int start = message.getReadPosition();
		debug("<-> CALL path = " + fieldName);
		if(type == 0) {
			ArrayList<StandardItem> childList = currentItem.getChildsList();
			for(int i=0; i<childList.size(); i++) {
				StandardItem item = childList.get(i);
				debug("@MESSAGE path = " + fieldName +", start=" + start+", length=" + item.length+", value=[" + item.getValue()+"]");
				traverse(message, item, srcbytes, null, itemMap);
			}
		}
		else if(type == 1) {
			StandardItem item = message.findItem(fieldName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
			byte[] value = cut(fieldName, srcbytes, start, item.length);
			item.setBytesValue(value);
			itemMap.put(fieldName, item.getValue());
			debug("@FIELD path = " + fieldName +", start=" + start+", length=" + item.length+", value=[" + item.getValue()+"]");
	        start = start + item.length;
	        message.setReadPosition(start);
		}
		else if(type == 2) {
			ArrayList<StandardItem> childList = currentItem.getChildsList();
			for(int i=0; i<childList.size(); i++) {
				StandardItem item = childList.get(i);
				debug("@GROUP parent path = " + fieldName +", start=" + start+", item=" + item.name + ", length=" + item.length+", current value=[" + item.getValue()+"]");
				traverse(message, item, srcbytes, fieldName, itemMap);
			}
		}
		else if(type == 3) {
			int arraySize = currentItem.getLength();
			for(int ai = 0; ai<arraySize; ai++) {
				 LinkedHashMap<String, StandardItem> map = currentItem.getArrayChilds(ai, true);
				if(map == null) {
					debug("@WARN Array create failed  item path = " + fieldName +"[" + ai +"]");
					continue;
				}
				ArrayList<StandardItem> childList = new ArrayList<StandardItem>(map.values());
				for(int i=0; i<childList.size(); i++) {
					StandardItem item = childList.get(i);
					debug("@ARRAY parent path = " + fieldName+"[" + ai +"]" +", item=" + item.name +", start=" + start+", length=" + item.length+", current value=[" + item.getValue()+"]");
					traverse(message, item, srcbytes, fieldName +"[" + ai +"]", itemMap);
				}
			}
		}		
	}
    
	private static void debug(String msg) {
		if(debug) System.out.println(msg);
	}
	
	private String genPath(String parent, String nodeName) {
		if(parent == null || parent.length() < 1) {
			return nodeName;
		}
		return parent + FIELD_SEPARATOR + nodeName;
	}
	
	private static byte[] cut(String fieldName, byte[] srcBytes, int start, int length) {
        if (srcBytes == null) {
            return null;
        }
        if (srcBytes.length < (start+length)) {
        	StringBuffer sb = new StringBuffer();
        	sb.append("item="+ fieldName + ", cut failed(underflow) - start index=" + start + ", item length=" + length + ", total size=" + srcBytes.length);
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
//	public static void main(String[] args) {
//	}
}
