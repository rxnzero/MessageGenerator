package com.dhlee.message.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardDataType;
import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardType;

public class FlatReader implements StandardReader {
	static Logger logger = LoggerFactory.getLogger(FlatReader.class);
	private char FIELD_SEPARATOR = '.';
	private boolean ZERO_BASE_INDEX = true;
	
	private String encoding = "euc-kr";
	
	public FlatReader() {
		 
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
		
		// make item map(path, value)
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
	
	private void traverse(StandardMessage message, StandardItem currentItem, byte[] srcbytes, String parentName
			, LinkedHashMap<String, String> itemMap){
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
					logger.debug("@MESSAGE path = {}, start={}, length={}, value=[{}]", fieldName, start, item.getLength(), item.getValue());
					traverse(message, item, srcbytes, null, itemMap);
				}
				break;
			case StandardType.FIELD:
				item = currentItem; //message.findItem(fieldName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
				if(item.getDataType() == StandardDataType.ZZ_STRING) {
					start = start - item.getLength();
					logger.debug("@FIELD path = {}, start={}, length={}, ZZ check start {}", fieldName, start, item.getLength(), item.getValue(), start);
					// cut bizData
					try {
						message.cutBizData(item.getLength());
					} catch (Exception e) {
						logger.error("@FIELD path = {}, start={}, length={}, cutBizData Failed", fieldName, start, item.getLength(), e);
					}
				}
				byte[] value = cut(fieldName, srcbytes, start, item.getLength());
				item.setBytesValue(value);
				itemMap.put(fieldName, item.getValue());
				logger.debug("@FIELD path = {}, start={}, length={}, value=[{}]", fieldName, start, item.getLength(), item.getValue());
		        start = start + item.getLength();
		        message.setReadPosition(start);
				break;
			case StandardType.GROUP:
				logger.debug("@GROUP name={}, getLength={}, getRefPath={}, getRefValue=[{}]"
						, currentItem.getName(), currentItem.getLength(), currentItem.getRefPath(), currentItem.getRefValue());
				if( currentItem.getSize() == 0 
					&& StringUtils.isNoneEmpty(currentItem.getRefPath()) 
					&& StringUtils.isNoneEmpty(currentItem.getRefValue()) ) {
					String refItemValue = itemMap.get(currentItem.getRefPath());
				
					logger.debug("@GROUP name={}, getRefPath={}, refItemValue=[{}], getRefValue=[{}]"
							, currentItem.getName(), currentItem.getRefPath(), refItemValue, currentItem.getRefValue());
					
					if(refItemValue != null) refItemValue = refItemValue.trim();
					
					if( !currentItem.getRefValue().equals(refItemValue) ) {
						logger.warn("@GROUP name={} SKIPPED.", currentItem.getName());
							break;
					}
				}
				childList = currentItem.getChildsList();
				for(int i=0; i<childList.size(); i++) {
					item = childList.get(i);					
					logger.debug("@GROUP item={}, path = {}, start={}, length={}, value=[{}]", item.getName(), fieldName, start, item.getLength(), item.getValue());
			        traverse(message, item, srcbytes, fieldName, itemMap);
				}
				if( currentItem.getSize() == 0) {
						currentItem.setSize(1);
				}
				break;
			case StandardType.GRID:
				logger.debug("@GRID name={}, getLength={}, getRefPath={}, getRefValue=[{}]"
						, currentItem.getName(), currentItem.getLength(), currentItem.getRefPath(), currentItem.getRefValue());
				if( currentItem.getSize() == 0 
					&& StringUtils.isNoneEmpty(currentItem.getRefPath()) 
					&& StringUtils.isNoneEmpty(currentItem.getRefValue()) ) {
					String refItemValue = itemMap.get(currentItem.getRefPath());
				
					logger.debug("@GRID name={}, getRefPath={}, refItemValue=[{}], getRefValue=[{}]"
							, currentItem.getName(), currentItem.getRefPath(), refItemValue, currentItem.getRefValue());
					
					if( !currentItem.getRefValue().equals(refItemValue) ) {
						logger.warn("@GRID name={} SKIPPED.", currentItem.getName());
							break;
					}
				}
				int arraySize = currentItem.getSize();
				for(int ai = 0; ai<arraySize; ai++) {
					 LinkedHashMap<String, StandardItem> map = currentItem.getArrayChilds(ai, true);
					if(map == null) {
						logger.warn("Array create failed  item path = {}",  fieldName +"[" + ai +"]");
						continue;
					}
					childList = new ArrayList<StandardItem>(map.values());
					for(int i=0; i<childList.size(); i++) {
						item = childList.get(i);						
						logger.debug("@ARRAY parent path = {}, item={}, start={}, length={}, current value=[{}]", fieldName+"[" + ai +"]", item.getName(),  start, item.getLength(), item.getValue());
				        traverse(message, item, srcbytes, fieldName +"[" + ai +"]", itemMap);
					}
				}
				break;
			case StandardType.FARRAY:
				logger.debug("@FARRAY name={}, getLength={}, getRefPath={}, getRefValue=[{}]"
						, currentItem.getName(), currentItem.getLength(), currentItem.getRefPath(), currentItem.getRefValue());
				
				int farraySize = currentItem.getSize();
				item = currentItem; //message.findItem(fieldName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
				for(int ai = 0; ai<farraySize; ai++) {
					if(item.getDataType() == StandardDataType.ZZ_STRING) {
						start = start - item.getLength();
						logger.debug("@FARRAY path = {}, start={}, length={}, ZZ check start {}", fieldName, start, item.getLength(), item.getValue(), start);
						// cut bizData
						try {
							message.cutBizData(item.getLength());
						} catch (Exception e) {
							logger.error("@FARRAY path = {}, start={}, length={}, cutBizData Failed", fieldName, start, item.getLength(), e);
						}
					}
					value = cut(fieldName, srcbytes, start, item.getLength());
					item.addBytesValue(value);
					
					itemMap.put(fieldName, item.getValue());
					logger.debug("@FARRAY path = {}, start={}, length={}, value=[{}]", fieldName, start, item.getLength(), item.getValue());
			        start = start + item.getLength();
			        message.setReadPosition(start);
				}
				break;
			case StandardType.BIZDATA:
				// FIX-ME : how to calculate bzi data size ?
				//             current : bizData is last item in layout items (variable size)
				item = message.findItem(fieldName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
				int bizDataSize = item.getLength();
				if(bizDataSize == 0) {
					bizDataSize = srcbytes.length - start;
				}
				byte[] bizDataBytes = cut(fieldName, srcbytes, start, bizDataSize);
				
				item.setBytesValue(bizDataBytes);
				
				itemMap.put(fieldName, item.getValue());
				logger.debug("@BIZDATA path = {}, start={}, length={}, value=[{}]", fieldName, start, item.getLength(), item.getValue());
		        start = start + bizDataSize;
		        message.setReadPosition(start);
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
