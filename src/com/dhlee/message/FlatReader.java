package com.dhlee.message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dom4j.Attribute;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlatReader implements StandardReader {
	static Logger logger = null;
	private String FIELD_SEPARATOR = ".";
	private boolean ZERO_BASE_INDEX = true;
	
	private String encoding = "euc-kr";;
	
	public FlatReader() {
		if(logger == null) logger = LoggerFactory.getLogger(this.getClass());
	}
	
	public StandardMessage parse(StandardMessage message, String flatString) throws Exception {
		ArrayList<StandardItem> list = message.toList();
		for(int i=0; i<list.size(); i++) {
			StandardItem item = list.get(i);
			logger.debug("{} Level={} Name={}",  i,  item.getLevel(), item.getName());
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
			logger.debug("~ itemMap : {} => {}", key, itemMap.get(key) );
		}
		return message;		
	}
	
	private void traverse(StandardMessage message, StandardItem currentItem, byte[] srcbytes, String parentName, LinkedHashMap<String, String> itemMap){
		String fieldName = null;
		ArrayList<StandardItem> childList = null;
		StandardItem item = null;
		
		int type = currentItem.getType();
		int start = message.getReadPosition();
		fieldName = genPath(parentName, currentItem.getName());
		logger.debug("<-> CALL path = " + fieldName);
		switch(type) {
			case StandardType.MESSAGE:
				childList = currentItem.getChildsList();
				for(int i=0; i<childList.size(); i++) {
					item = childList.get(i);
					logger.debug("@MESSAGE path = {}, start={}, length={}, value=[{}]", fieldName, start, item.length, item.getValue());
					traverse(message, item, srcbytes, null, itemMap);
				}
				break;
			case 1:
				item = message.findItem(fieldName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
				byte[] value = cut(fieldName, srcbytes, start, item.length);
				item.setBytesValue(value);
				itemMap.put(fieldName, item.getValue());
				logger.debug("@FIELD path = {}, start={}, length={}, value=[{}]", fieldName, start, item.length, item.getValue());
		        start = start + item.length;
		        message.setReadPosition(start);
				break;
			case 2:
				childList = currentItem.getChildsList();
				for(int i=0; i<childList.size(); i++) {
					item = childList.get(i);					
					logger.debug("@GROUP item={}, path = {}, start={}, length={}, value=[{}]", item.name, fieldName, start, item.length, item.getValue());
			        traverse(message, item, srcbytes, fieldName, itemMap);
				}
				break;
			case 3:
				int arraySize = currentItem.getLength();
				for(int ai = 0; ai<arraySize; ai++) {
					 LinkedHashMap<String, StandardItem> map = currentItem.getArrayChilds(ai, true);
					if(map == null) {
						logger.warn("Array create failed  item path = {}",  fieldName +"[" + ai +"]");
						continue;
					}
					childList = new ArrayList<StandardItem>(map.values());
					for(int i=0; i<childList.size(); i++) {
						item = childList.get(i);						
						logger.debug("@ARRAY parent path = {}, item={}, start={}, length={}, current value=[{}]", fieldName+"[" + ai +"]", item.name,  start, item.length, item.getValue());
				        traverse(message, item, srcbytes, fieldName +"[" + ai +"]", itemMap);
					}
				}
				break;
			default:
				break;
		}		
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
        	sb.append("item="+ fieldName);
        	sb.append(", cut failed(underflow) - start index=" + start);
        	sb.append(", item length=" + length);
        	sb.append(", total size=" + srcBytes.length);
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
