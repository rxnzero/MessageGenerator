package com.dhlee.message;

import java.util.LinkedHashMap;

public class StandardMessage extends StandardItem{
	private static final long serialVersionUID = 8250470942348479859L;
	static boolean debug = true;
	
	int readPosition = 0;
	
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
				ex.printStackTrace();
				debug("@ERROR - getChilds not found : this=" + item.toString());
				debug("@ERROR - getChilds not found : itemName=" + itemName);
			}
			if(i< paths.length-1) {
				if(item != null && item.getType() >StandardType.FIELD.getValue()) {
					if(arrayIndex >= 0) {
						childItems = item.getArrayChilds(arrayIndex, createNew);
					}
					else {
						childItems = item.getChilds();
					}
					if(childItems == null) {
						debug("@WARN - childItems not found : " + paths[i] +" / "+ path);
						return  null;
					}
				}
				else {
					debug("@WARN - group not found : " + paths[i] +" / "+ path);
					return null;
				}
			}			
		}
		if(item == null) {
			debug("@WARN - Item not found : " + path);
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
	
	private void debug(String msg) {
		if(debug) System.out.println(msg);
	}
}
