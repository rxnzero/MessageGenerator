package com.dhlee.message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.layout.CsvFileReader;
import com.dhlee.message.layout.LayoutReader;

public class StandardMessageUtil {
	static Logger logger = LoggerFactory.getLogger(StandardMessageUtil.class);
	public StandardMessageUtil() {		
	}
	
	public static StandardMessage generateMessageFromCCsvFile() {
		String filePath = "./resources/standard-layout.csv";
		return generateMessageFromCCsvFile(filePath);
	}
	public static StandardMessage generateMessageFromCCsvFile(String filePath) {
		ArrayList<StandardItem> list = null;

		try {
			LayoutReader reader = new CsvFileReader();
			list = (ArrayList<StandardItem>) reader.parse(filePath);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		StandardMessage message = null;
		try {
			message = generate(list);			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return message;
	}
	
	
	// TODO : if Fixed Array, clone list item to length ?
	public static StandardMessage generate(ArrayList<StandardItem> itemList) throws Exception {
		StandardMessage message = new StandardMessage();
		Stack<StandardItem> stack = new Stack<StandardItem>();
		Stack<String> stackPath = new Stack<String>();
		int pLevel = 0;
		StandardItem parent = message;
		String parentPath = null;
//		logger.debug("======================================================");
		for(int i=0; i< itemList.size(); i++) {
			StandardItem item = itemList.get(i);
//			logger.debug("{} : item - {}", i, item);
			// top level
			if(item.getLevel() == 0) {
				message.setName(item.getName());
				continue;
			}
			
			if(item.getLevel() == 1) {
				message.addItem(item);
			}
			else {
				// get parent & add
				int levelDiff = (item.getLevel() - pLevel);
				if( levelDiff == 0) {
					// do nothing
				}
				else if( levelDiff == 1) {
					parent = stack.pop();
					parentPath = stackPath.pop();
				}
				else if(levelDiff < 0) {
					for(int p=levelDiff; p<0; p++) {
						parent = stack.pop();
						parentPath = stackPath.pop();
					}
				}
				else {
					throw new Exception("item level error - "+ item.getName() +" level diff=" + levelDiff);
				}
				
				if(parent.getType() == StandardType.GROUP) {
//					logger.debug("add group child item - {}", item);
					parent.addItem(item);
				}
				else if(parent.getType() == StandardType.ARRAY) {
					parent.addArrayItem(0, item);
				}
				else {
					throw new Exception("item parent is not GROUP/ARRAY : item - " + item);
				}
			}
			
			if(item.getType() == StandardType.BIZDATA) {
				if(item.getLevel() == 1) {
					message.setBizDataPath(item.getName());
				}
				else {
					message.setBizDataPath(getFullPath(parentPath , item.getName()));
				}
				logger.debug("@@@ BIZ DATA PATH = {}", message.getBizDataPath());
			}
			
			// if group/array,push to stack, pLevel
			if(item.getType() > 1) {
				stackPath.push( getFullPath(parentPath , item.getName()) );
				stack.push(item);
			}
			pLevel = item.getLevel();
		}
		return message;
	}
	
	private static String getFullPath(String parentPath, String itemName) {
		if(parentPath == null || parentPath.length() == 0) {
			return itemName;
		}
		return parentPath + "." + itemName;
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
