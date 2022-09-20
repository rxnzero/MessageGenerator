package com.dhlee.message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardMessageUtil {
	static Logger logger = LoggerFactory.getLogger(StandardMessageUtil.class);
	public StandardMessageUtil() {		
	}
	
	
	// TODO : if Fixed Array, clone list item to length ?
	public static StandardMessage generate(ArrayList<StandardItem> itemList) throws Exception {
		StandardMessage message = new StandardMessage();
		Stack<StandardItem> stack = new Stack<StandardItem>();
		int pLevel = 0;
		int arrayIndex = 0;
		StandardItem parent = message;
//		logger.debug("======================================================");
		for(int i=0; i< itemList.size(); i++) {
			StandardItem item = itemList.get(i);
//			logger.debug(String.format("%03d : item - %s", i, item) );
			// 최상위일 경우 message에 저장
			if(item.getLevel() == 0) {
				message.setName(item.getName());
				continue;
			}
			
			if(item.getLevel() == 1) {
				message.addItem(item);
			}
			else {
				// 상위를 얻어서 add
				int levelDiff = (item.getLevel() - pLevel);
				if( levelDiff == 0) {
					// do nothing
				}
				else if( levelDiff == 1) {
					parent = stack.pop();
				}
				else if(levelDiff < 0) {
					for(int p=levelDiff; p<0; p++) {
						parent = stack.pop();
					}
				}
				else {
					throw new Exception("item level error - "+ item.getName() +" level diff=" + levelDiff);
				}
				
				if(parent.getType() == StandardType.GROUP) {
//					logger.debug("add group child item - " + item);
					parent.addItem(item);
				}
				else if(parent.getType() == StandardType.ARRAY) {
					parent.addArrayItem(0, item);
				}
				else {
					throw new Exception("item parent is not GROUP/ARRAY : item - " + item);
				}
			}
			
			// group 일 경우 stack에 저장, pLevel 저장
			if(item.getType() > 1) {
				stack.push(item);				
			}
			pLevel = item.getLevel();
		}
		return message;
	}

	public static int getArrayIndex(String path) {
		int index = -1;
		
		if(path.charAt(path.length()-1) != ']') {
			return index;
		}
		int start = path.indexOf("[");
		if(start > 0) {
			String p = path.substring(start + 1, path.length()-1);
			try {
				index = Integer.parseInt(p);
			}
			catch(NumberFormatException e) {
				;
			}
		}
		return index;
	}
	
	public static String getArrayName(String path) {
		if(path.charAt(path.length()-1) != ']') {
			return path;
		}
		int start = path.indexOf("[");
		if(start > 0) {
			String p = path.substring(0, start);
			return p;
		}
		else {
			return path;
		}
	}
	
	public static void main(String[] args) {
		logger.debug("{}", getArrayIndex("group[10]"));
		logger.debug("{}", getArrayName("group[10]"));
		logger.debug("{}", getArrayIndex("group"));
		logger.debug("{}", getArrayName("group"));
	}
}
