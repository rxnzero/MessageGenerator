package com.dhlee.message;

import java.util.LinkedHashMap;

public class StandardMessage extends StandardItem{
	private static final long serialVersionUID = 8250470942348479859L;
	static boolean debug = false;
	public StandardMessage() {
		this.name = "StandardRoot"; // XML 일 경우 Root name으로 사용 ?
		this.type = 0; 
	}
	
	public StandardItem findItem(String path) {
		String[] paths = path.split("[.]");
		StandardItem item = null;
//		if(paths.length ==1) {
//			String itemName = path;
//			int arrayIndex = StandardMessageUtil.getArrayIndex(itemName);
//			if(arrayIndex >= 0) {
//				itemName = StandardMessageUtil.getArrayName(itemName);
//			}
//			// Array는 저장된 frame을 return
////			if(arrayIndex >= 0) {
////				item = this.getArrayChilds(arrayIndex);
////			}
////			else {
//				item = this.getChilds().get(itemName);
////			}
//			if(item == null) {
//				("@WARN - Item not found : " + path);
//				return null;
//			}
//			else {
//				return item;
//			}
//		}
		LinkedHashMap<String , StandardItem> childItems = this.getChilds(); 
		for(int i=0; i< paths.length; i++) {
			String itemName = paths[i];
			int arrayIndex = StandardMessageUtil.getArrayIndex(itemName);
			if(arrayIndex >= 0) {
				itemName = StandardMessageUtil.getArrayName(itemName);
			}
			item = childItems.get(itemName);
			if(i< paths.length-1) {
				if(item != null && item.getType() >1) {
					if(arrayIndex >= 0) {
						childItems = item.getArrayChilds(arrayIndex);
					}
					else {
						childItems = item.getChilds();
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
