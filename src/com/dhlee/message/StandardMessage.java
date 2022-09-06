package com.dhlee.message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

public class StandardMessage extends StandardItem{
	private static final long serialVersionUID = 8250470942348479859L;

	public StandardMessage() {
		this.name = "StandardRoot";
		this.type = 0; 
	}
	
	public StandardItem findItem(String path) {
		String[] paths = path.split("[.]");
		StandardItem item = null;
		if(paths.length ==1) {
			item = this.getChilds().get(path);
			if(item == null) {
				System.out.println("@WARN - Item not found : " + path);
				return null;
			}
			else {
				return item;
			}
		}
		LinkedHashMap<String , StandardItem> childItems = this.getChilds(); 
		for(int i=0; i< paths.length; i++) {
			item = childItems.get(paths[i]);
			if(i< paths.length-1) {
				if(item != null && item.getType() >1) {
					childItems = item.getChilds();
				}
				else {
					System.out.println("@WARN - group not found : " + paths[i] +" / "+ path);
					return null;
				}
			}
		}
		if(item == null) {
			System.out.println("@WARN - Item not found : " + path);
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
	
	// TODO : list to messageTree
	public static StandardMessage generate(ArrayList<StandardItem> itemList) {
		StandardMessage message = new StandardMessage();
		Stack<StandardItem> stack = new Stack<StandardItem>();
		int pLevel = 0;
		
		StandardItem parent = message;
		for(int i=0; i< itemList.size(); i++) {
			StandardItem item = itemList.get(i);
			// 최상위일 경우 messahe에 저장
			if(item.getLevel() == 1) {
				message.addItem(item);
			}
			else {
				// 상위를 얻어서 add
				int levelDiff = (item.getLevel() - pLevel);
				if( levelDiff == 0) {
					parent.addItem(item);
				}
				else if( levelDiff == 1) {
					parent = stack.pop();
					if(parent !=null) {
						parent.addItem(item);
					}
				}
				else if(levelDiff < 0) {
					for(int p=levelDiff; p<0;p++) {
						parent = stack.pop();
					}
					parent.addItem(item);
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
}
