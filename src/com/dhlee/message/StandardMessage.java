package com.dhlee.message;

import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author elink
 *
 */
public class StandardMessage extends StandardItem{
	private static final long serialVersionUID = 8250470942348479859L;
	static Logger logger = LoggerFactory.getLogger(StandardMessage.class);
	int readPosition = 0;
	private String bizDataPath = null;
	
	public String getBizDataPath() {
		return bizDataPath;
	}

	public void setBizDataPath(String bizDataPath) {
		this.bizDataPath = bizDataPath;
	}

	public String getBizData() {
		StandardItem bizItem = findItem(bizDataPath);
		if(bizItem == null) {
			return null;
		}
		else {
			return bizItem.getValue();
		}
	}
		
	public void setBizData(String bizData) {
		StandardItem bizItem = findItem(bizDataPath);
		if(bizItem != null) {
			bizItem.setValue(bizData);
		}
	}

	public StandardMessage() {
		this.name = "StandardRoot"; // XML 일 경우 Root name으로 사용 ?
		this.type = 0; 
	}
	
	public int getReadPosition() {
		return readPosition;
	}
	public void setReadPosition(int readPosition) {
		this.readPosition = readPosition;
	}
	
	public StandardItem findItem(String path) {
		return findItem(path, ".", true, false);
	}
	
	public StandardItem findItem(String path, String pathSeparator, boolean zeroBase, boolean createNew) {
		String  separator = "["+ pathSeparator +"]";
		
		String[] paths = path.split(separator);
		StandardItem item = null;

		LinkedHashMap<String , StandardItem> childItems = this.getChilds();
		for(int i=0; i< paths.length; i++) {
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
			if(i< paths.length-1) {
				if(item != null && item.getType() >StandardType.FIELD) {
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
			return null;
		}
		else {
			return item.getValue();
		}
	}	
}
