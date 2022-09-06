package com.dhlee.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
 
public class StandardItem  implements Serializable{
	private static final long serialVersionUID = -4430144517271735912L;
	LinkedHashMap<String , StandardItem> childs = new LinkedHashMap<String , StandardItem>();
	String name;
	int level; // 0 : root  ~ n
	int type; // 0: Message, 1: Field, 2:Group 3, Array
	int length;
	int dataType; // 0: String, 1: Number
	String value;
	public StandardItem() {
	}
	
	public StandardItem(String name, int level, int type, int length, int dataType) {
		super();
		this.name = name;
		this.level = level;
		this.type = type;
		this.length = length;
		this.dataType = dataType;
	}
	
	public StandardItem(String name, int level, int type, int length, int dataType, String value) {
		super();
		this.name = name;
		this.level = level;
		this.type = type;
		this.length = length;
		this.dataType = dataType;
		this.value = value;
	}

	public void addItem (StandardItem item) {
		childs.put(item.getName(), item);
	}
	
	public LinkedHashMap<String, StandardItem> getChilds() {
		return childs;
	}
	public void setChilds(LinkedHashMap<String, StandardItem> childs) {
		this.childs = childs;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	private String getLevelIndent() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<level; i++) {
			sb.append("  ");
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLevelIndent()).append("StandardItem [name=" + name + ", level=" + level + ", type=" + type + ", length=" + length + ", dataType=" + dataType
				+ ", value=" + value + "]");
		Iterator<String> keyIter = childs.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			StandardItem item = childs.get(key);
			sb.append("\n").append(item.toString());
		}
		return sb.toString();
	}
	
	public String toJsonValue() {
		StringBuilder sb = new StringBuilder();
		if(dataType == 0) {
			return "\"" + value + "\"";
		}
		else {
			return value;
		}
	}
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		if(type ==0) {
			sb.append("{");
//			sb.append("\"").append( name ).append("\"").append(": {");
			Iterator<String> keyIter = childs.keySet().iterator();
			int i=0;
			while(keyIter.hasNext()) {
				if(i>0) sb.append(", ");
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				sb.append("\n").append(item.toJson());
				i++;
			}
//			sb.append("}");
			sb.append("\n}");
		}
		if(type ==1) {
			sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\":").append(toJsonValue());
		}
		if(type ==2) {
			sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\": {");
			Iterator<String> keyIter = childs.keySet().iterator();
			int i=0;
			while(keyIter.hasNext()) {
				if(i>0) sb.append(", ");
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				sb.append("\n").append(item.toJson());
				i++;
			}
			sb.append("\n");
			sb.append(getLevelIndent());
			sb.append("}");
		}
		return sb.toString();
	}
}
