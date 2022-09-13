package com.dhlee.message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.apache.commons.lang3.SerializationUtils;

public class StandardMessageUtil {

	public StandardMessageUtil() {
		// TODO Auto-generated constructor stub
	}
	
	// TODO : if Fixed Array, clone list item to length ?
	public static StandardMessage generate(ArrayList<StandardItem> itemList) {
		StandardMessage message = new StandardMessage();
		Stack<StandardItem> stack = new Stack<StandardItem>();
		int pLevel = 0;
		int arrayIndex = 0;
		StandardItem parent = message;
		System.out.println("======================================================");
		for(int i=0; i< itemList.size(); i++) {
			StandardItem item = itemList.get(i);
			System.out.println(String.format("%03d : item - %s", i, item) );
			// 최상위일 경우 message에 저장
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
				if(parent.getType() == StandardType.GROUP.getValue()) {
//					System.out.println("add group child item - " + item);
					parent.addItem(item);
				}
				else if(parent.getType() == StandardType.ARRAY.getValue()) {
					parent.addArrayItem(0, item);
				}
				else {
					System.out.println("@ERROR item parent is not GROUP/ARRAY : item - " + item);
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
		System.out.println(getArrayIndex("group[10]"));
		System.out.println(getArrayName("group[10]"));
	}
}
