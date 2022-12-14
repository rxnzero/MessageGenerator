package com.dhlee.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.filter.MessageFilter;

/**
 * @author elink
 *
 */
public class StandardMessage extends StandardItem {
	private static final long serialVersionUID = 8250470942348479859L;
	static Logger logger = LoggerFactory.getLogger(StandardMessage.class);
	int readPosition = 0;
	private String bizDataPath = null;
	private String llDataPath = null;
	private String zzDataPath = null;
	
	private MessageFilter flatFilter = null;
	
	public MessageFilter getFlatFilter() {
		return flatFilter;
	}

	public void setFlatFilter(MessageFilter flatFilter) {
		this.flatFilter = flatFilter;
	}

	public String getBizDataPath() {
		return bizDataPath;
	}

	public void setBizDataPath(String bizDataPath) {
		this.bizDataPath = bizDataPath;
	}
	
	public String getLlDataPath() {
		return llDataPath;
	}

	public void setLlDataPath(String llDataPath) {
		this.llDataPath = llDataPath;
	}

	public String getZzDataPath() {
		return zzDataPath;
	}

	public void setZzDataPath(String zzDataPath) {
		this.zzDataPath = zzDataPath;
	}
	
	public String getBizData() throws Exception {
		if(bizDataPath == null) {
			throw new Exception("bizDataPath path not configured, check layout definition.");
		}
		StandardItem Item = findItem(bizDataPath);
		if(Item == null) {
			return null;
		}
		else {
			return Item.getValue();
		}
	}
		
	public void setBizData(String bizData) throws Exception {
		if(bizDataPath == null) {
			throw new Exception("bizDataPath path not configured, check layout definition.");
		}
		StandardItem Item = findItem(bizDataPath);
		if(Item != null) {
			Item.setValue(bizData);
		}
	}
	
	public void cutBizData(int zzSize) throws Exception {
		if(bizDataPath == null) {
			throw new Exception("bizDataPath path not configured, check layout definition.");
		}
		StandardItem Item = findItem(bizDataPath);
		if(Item != null) {
			byte[] bizDataBytes = Item.getBytesValue();
			int finalSize = bizDataBytes.length - zzSize;
			 
			if(finalSize > 0) {
				byte[] fbizDataBytes = new byte[finalSize];
				for(int i=0; i<finalSize; i++) {
					fbizDataBytes[i] = bizDataBytes[i];
				}
				Item.setBytesValue(fbizDataBytes);
			}
		}
	}
	
	public String getLlData() throws Exception {
		if(llDataPath == null) {
			throw new Exception("llDataPath path not configured, check layout definition.");
		}
		StandardItem Item = findItem(llDataPath);
		if(Item == null) {
			return null;
		}
		else {
			return Item.getValue();
		}
	}
		
	public void setLlData(String llData) throws Exception {
		if(llDataPath == null) {
			throw new Exception("llDataPath path not configured, check layout definition.");
		}
		StandardItem Item = findItem(llDataPath);
		if(Item != null) {
			Item.setValue(llData);
		}
	}
	public void setLlData(int llDataSize) throws Exception {
		if(llDataPath == null) {
			throw new Exception("llDataPath path not configured, check layout definition.");
		}
		StandardItem bizItem = findItem(llDataPath);
		if(bizItem != null) {
			bizItem.setValue(Integer.toString(llDataSize));
		}
	}
	
	public String getZzData() throws Exception {
		if(zzDataPath == null) {
			throw new Exception("zzDataPath path not configured, check layout definition.");
		}
		StandardItem Item = findItem(zzDataPath);
		if(Item == null) {
			return null;
		}
		else {
			return Item.getValue();
		}
	}
		
	public void setZzData(String zzData) throws Exception {
		if(zzDataPath == null) {
			throw new Exception("zzDataPath path not configured, check layout definition.");
		}
		StandardItem bizItem = findItem(zzDataPath);
		if(bizItem != null) {
			bizItem.setValue(zzData);
		}
	}
		
	public StandardMessage() {
		this.name = "StandardRoot"; // XML ?? ???? Root name???? ???? ?
		this.type = 0; 
	}
	
	public int getReadPosition() {
		return readPosition;
	}
	public void setReadPosition(int readPosition) {
		this.readPosition = readPosition;
	}
	
	public StandardItem findItem(String path) {
		return findItem(path, '.', true, false);
	}
	
	private String[] spilt(String source, char spliter) {
    	List<String> list = new ArrayList<String>();
    	CharSequence chars = source;
    	int start = 0, end = 0;
    	for(int i=0; i<chars.length(); i++) {
    		if(chars.charAt(i) == spliter) {
    			end = i;
    			list.add(chars.subSequence(start, end).toString());
    			start = end+1;
    			i = i+1;
    		}
    	}
    	
    	if(end == 0) {
    		list.add(chars.toString());
    	}
    	else {
    		list.add(chars.subSequence(start, chars.length()).toString());
    	}
    	
    	return (String[])list.toArray(new String[0]);
    }
	
	public StandardItem findItem(String path, char pathSeparator, boolean zeroBase, boolean createNew) {
		
		String[] paths = spilt(path, pathSeparator);
		StandardItem item = null;

		LinkedHashMap<String , StandardItem> childItems = this.getChilds();
		for(int i=0; i < paths.length; i++) {
			String itemName = paths[i];
			int arrayIndex = StandardMessageUtil.getArrayIndex(itemName);
			
			if(arrayIndex >= 0) {
				itemName = StandardMessageUtil.getArrayName(itemName);
				// if XML, use 1 base index 
				if( !zeroBase ) {
					arrayIndex = arrayIndex -1;
				}
			}
			try {
				item = childItems.get(itemName);				
			}
			catch(Exception ex) {
				logger.error("get Child error : this=" + itemName, ex);		
				return null;
			}
			
			if(i < paths.length-1) {
				if(item != null && item.getType() > StandardType.FIELD) {
					if(arrayIndex >= 0) {
						childItems = item.getArrayChilds(arrayIndex, createNew);
					}
					else {
						childItems = item.getChilds();
					}
					if(childItems == null) {
						logger.warn("childItems not found : " + paths[i] +" / "+ path);
						return  null;
					}
				}
				else {
					logger.warn("group not found : " + paths[i] +" / "+ path);
					return null;
				}
			}			
		}
		if(item == null) {
			logger.warn("Item not found : " + path);
			return null;
		}
		else {
			return item;
		}
	}
	
	// group1.group2.item1
	public String findItemValue(String path) {
		StandardItem item = findItem(path);
		if(item == null) {
			logger.warn("Item Path not found {}", path);
			return null;
		}
		else {
			return item.getValue();
		}
	}
	
	public byte[] toByteArray() {
		return toByteArray(true);
	}
	
	public byte[] toByteArray(boolean runFilter) {
		if(runFilter && flatFilter != null) {
			flatFilter.doFilter(this);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Iterator<String> keyIter = null;
		try {
			keyIter = childs.keySet().iterator();
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				bos.write(item.toByteArray());
			}
			return bos.toByteArray();
		} catch(Exception e) {
			logger.error("toByteArray failed", e);
			return new byte[0];
		}
		finally {
			if(bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					;
				}
		}
	}
	
	public int getBytesDataLength() {
		Iterator<String> keyIter = null;
		int totalSize = 0;
		try {
			keyIter = childs.keySet().iterator();
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				totalSize +=item.getBytesDataLength();
			}
			return totalSize;
		} catch(Exception e) {
			logger.error("getBytesDataLength failed", e);
			return totalSize;
		}
		finally {			
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLevelTreeIndent()).append("StandardMessage [name=" + name + ", level=" + level 
				+ ", type=" + type 
//				+", size=" + size + ", fieldType=" + fieldType +", length=" + length + ", dataType=" + dataType
//				+ ", refPath=" + refPath + ", refValue=" + refValue
//				+ ", value=" + value 
				+ ", desc=" + desc + "]");
		if(bizDataPath != null) sb.append(", bizDataPath=" + bizDataPath);
		if(llDataPath != null) sb.append(", llDataPath=" + llDataPath);
		if(zzDataPath != null) sb.append(", zzDataPath=" + zzDataPath);
		
		
		Iterator<String> keyIter = childs.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			StandardItem item = childs.get(key);
			sb.append("\n").append(item.toString());
		}
		return sb.toString();
	}
	
	
}
