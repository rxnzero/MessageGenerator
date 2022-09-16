package com.dhlee.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.SerializationUtils;
 
public class StandardItem  implements Serializable, Cloneable {
	private static final long serialVersionUID = -4430144517271735912L;
	LinkedHashMap<String , StandardItem> childs = new LinkedHashMap<String , StandardItem>();
	ArrayList<LinkedHashMap<String , StandardItem>> list = new ArrayList<LinkedHashMap<String , StandardItem>>();

	// FIX-ME : check encoding
	// String <-> byte[] charset
	String encode = "euc-kr";
	
	String name;
	int level; // 0 : root  ~ n
	int type; // 0: Message, 1: Field, 2:Group 3, Array
	int fieldType; // 0: ELEMENT, 1: ATTRIBUTE
	int length;
	int dataType; // 0: String, 1: Number
	String value;
	byte[] bytesValue = null;
	
	public StandardItem() {
//		childs = new LinkedHashMap<String , StandardItem>();
//		list = new ArrayList<StandardItem>();
	}
	
	public StandardItem(String name, int level, int type, int length, int dataType) {
		super();
		this.name = name;
		this.level = level;
		this.type = type;
		this.fieldType = 0;
		this.length = length;
		this.dataType = dataType;
	}
	
	public StandardItem(String name, int level, int type, int fieldType, int length, int dataType) {
		super();
		this.name = name;
		this.level = level;
		this.type = type;
		this.fieldType = fieldType;
		this.length = length;
		this.dataType = dataType;
	}
	
	public StandardItem(String name, int level, int type, int length, int dataType, String value) {
		super();
		this.name = name;
		this.level = level;
		this.type = type;
		this.fieldType = 0;
		this.length = length;
		this.dataType = dataType;
		this.value = value;
	}
	
	public StandardItem(String name, int level, int type, int fieldType,  int length, int dataType, String value) {
		super();
		this.name = name;
		this.level = level;
		this.type = type;
		this.fieldType = fieldType;
		this.length = length;
		this.dataType = dataType;
		this.value = value;
	}

	public void addItem (StandardItem item) {
		childs.put(item.getName(), item);
	}
	
	public void addArrayItem(int index, StandardItem item) {
		if( !childs.containsKey(item.getName()) ) {
			childs.put(item.getName(), item);
		}
		// add last
		if(list.size() == index) {
			LinkedHashMap<String , StandardItem> items = new LinkedHashMap<String , StandardItem>();
			items.put(item.getName(), item);
			list.add(items);
		}
		if(list.size() > index) {
			list.get(index).put(item.getName(), item);
		}
		else {
			System.out.println("index too big :  index = "+ index +" / array size = "+ list.size());
		}
	}
	public void addArray(LinkedHashMap<String , StandardItem> items) {
		list.add(items);
	}
	
	public LinkedHashMap<String, StandardItem> getArrayChilds(int index, boolean createNew) {
		if(index == list.size()) {
//			System.out.println("@@ add cloned childs in array  item="+ this.name +", index = "+ index);
			if(createNew) {
				list.add((LinkedHashMap<String , StandardItem>)(SerializationUtils.clone(childs)) );
			}
			else {
				return null;
			}
		}
		if(index > list.size()) {
			return null;
		}
		return list.get(index);
	}
	
	public LinkedHashMap<String, StandardItem> getChilds() {
		return childs;
	}
	
	public ArrayList<StandardItem> getChildsList() {
		ArrayList<StandardItem> childList = new ArrayList<StandardItem>(childs.values());
		return childList;
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
	
	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
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
		try {
			this.bytesValue = value.getBytes(encode);
		} catch (UnsupportedEncodingException e) {
			this.bytesValue = value.getBytes();
		}
	}
	
	public byte[] getBytesValue() {
		return bytesValue;
	}
	
	public void setBytesValue(byte[] bytesValue) {
		this.bytesValue = bytesValue;
		try {
			this.value = new String(bytesValue, encode);
		} catch (UnsupportedEncodingException e) {
			this.value = new String(bytesValue);
		}
	}
	
	private String getLevelIndent() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<level; i++) {
			sb.append("  ");
		}
		return sb.toString();
	}
	
	private String getLevelTreeIndent() {
		StringBuilder sb = new StringBuilder();
		if(level > 0) {
			for(int i=0; i<level-1; i++) {
				sb.append("┃");
			}
			sb.append("┣");
		}
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLevelTreeIndent()).append("StandardItem [name=" + name + ", level=" + level + ", type=" + type + ", fieldType=" + fieldType +", length=" + length + ", dataType=" + dataType
				+ ", value=" + value + "]");
		if (type==0 || type==2) {
			Iterator<String> keyIter = childs.keySet().iterator();
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				sb.append("\n").append(item.toString());
			}
		}
		else if(type ==3) {
			for(int p=0; p<list.size(); p++) {
				LinkedHashMap<String , StandardItem> group = list.get(p);
				Iterator<String> keyIter = group.keySet().iterator();
				int i=0;
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = group.get(key);
					sb.append("\n").append(item.toString());
				}				
			}
		}
		return sb.toString();
	}
	
	public String toCSVString() {
		StringBuilder sb = new StringBuilder();
		
		if(level == 0) {
			sb.append("name,level,type,fieldType,llength,dataType,value\n");
		}
		sb.append(name);
		sb.append(",");
		sb.append(level);
		sb.append(",");
		sb.append(type);
		sb.append(",");
		sb.append(fieldType);
		sb.append(",");
		sb.append(length);
		sb.append(",");
		sb.append(dataType);
		sb.append(",");
		sb.append(value);
		sb.append("\n");
		Iterator<String> keyIter = childs.keySet().iterator();
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			StandardItem item = childs.get(key);
			sb.append(item.toCSVString());
		}
		return sb.toString();
	}
	
	public ArrayList<StandardItem> toList() {
		ArrayList<StandardItem> list = new ArrayList<StandardItem>();
		if(this.type == 0) {
			ArrayList<StandardItem> childList = new ArrayList<StandardItem>(childs.values());
			for(int i=0; i<childList.size(); i++) {
				list.addAll(childList.get(i).toList());
			}
		}
		else if(this.type == 1) {
			list.add(this);			
		}
		else if(this.type == 2) {
			list.add(this);
			ArrayList<StandardItem> childList = new ArrayList<StandardItem>(childs.values());
			for(int i=0; i<childList.size(); i++) {
				list.addAll(childList.get(i).toList());
			}
		}
		else if(this.type == 3) {
			list.add(this);
			ArrayList<StandardItem> childList = new ArrayList<StandardItem>(childs.values());
			for(int i=0; i<childList.size(); i++) {
				list.addAll(childList.get(i).toList());
			}
		}
		return list;
	}
	
	protected String toJsonValue() {
		StringBuilder sb = new StringBuilder();
		if(dataType == 0) {
			return "\"" + value + "\"";
		}
		else {
			return value;
		}
	}
	
	public String toJson() {
		return toJson(false);
	}
	public String toPrettyJson() {
		return toJson(true);
	}
	
	public String toJson(boolean showPretty) {
		StringBuilder sb = new StringBuilder();
		if(type ==0) {
			sb.append("{");
// if root name add			
//			sb.append("\"").append( name ).append("\"").append(": {");
			Iterator<String> keyIter = childs.keySet().iterator();
			int i=0;
			while(keyIter.hasNext()) {
				if(i>0) sb.append(",");
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				if(showPretty) sb.append("\n");
				sb.append(item.toJson(showPretty));
				i++;
			}
//			sb.append("}");
			if(showPretty) sb.append("\n");
			sb.append("}");
		}
		if(type ==1) {
			if(showPretty) sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\":").append(toJsonValue());
		}
		else if(type ==2) {
			if(showPretty) sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\":{");
			Iterator<String> keyIter = childs.keySet().iterator();
			int i=0;
			while(keyIter.hasNext()) {
				if(i>0) sb.append(",");
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				if(showPretty) sb.append("\n");
				sb.append(item.toJson(showPretty));
				i++;
			}
			if(showPretty) {
				sb.append("\n");
				sb.append(getLevelIndent());
			}
			sb.append("}");
		}
		else if(type ==3) {
			if(showPretty) {				
				sb.append(getLevelIndent());
			}
			sb.append("\"").append(name).append("\":[");
//			if(showPretty) sb.append("\n");
			for(int p=0; p<list.size(); p++) {
				LinkedHashMap<String , StandardItem> group = list.get(p);
				Iterator<String> keyIter = group.keySet().iterator();
				int i=0;
				if(showPretty) {
					sb.append("\n");
					sb.append(getLevelIndent());
				}
				if(p>0) sb.append(",");
				sb.append("{");
				while(keyIter.hasNext()) {
					if(i>0) sb.append(",");
					String key = keyIter.next();
					StandardItem item = group.get(key);
					if(showPretty) sb.append("\n");
					sb.append(item.toJson(showPretty));
					i++;
				}
				
				if(showPretty) {
					sb.append("\n");
					sb.append(getLevelIndent()).append("}");					
				}
				else {
					sb.append("}");
				}
			}
//			if(showPretty) {
//				sb.append("\n");
//				sb.append(getLevelIndent());
//			}
			sb.append("]");
		}
		return sb.toString();
	}
	
	private String toStartTag(String nodeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(nodeName).append(">");
		return sb.toString();
	}
	private String toEndTag(String nodeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("</").append(nodeName).append(">");
		return sb.toString();
	}
	
	protected String toXmlValue() {
		return value;
	}
	
	public String toXML() {
		return toXML("utf-8", false);
	}
	
	public String toPrettyXML() {
		return toXML("utf-8", true);
	}
	
	// FIX-ME : Attribute와 Element가 혼합된 Group/Array는 현재 처리 불가함.
	//            XML의 Attribute Group일 경우 Group과 Item의 FieldType은 모두 1 로 설정되어야 함.
	//            향후 XMLParser로 생성하는 방안은 고려해 봐야 함.
	public String toXML(String encode, boolean showPretty) {
		StringBuilder sb = new StringBuilder();
		if(type ==0) {
			sb.append("<?xml version=\"1.0\" encoding=\""+encode+"\"?>");
			if(showPretty) sb.append("\n");
			
			sb.append(toStartTag(name));
			
			Iterator<String> keyIter = childs.keySet().iterator();
			int i=0;
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				sb.append(item.toXML(encode, showPretty));
				i++;
			}
			
			if(showPretty) sb.append("\n");
			sb.append(toEndTag(name));
		}
		if(type ==1) {
			if(fieldType == 0) {
				if(showPretty) {
					sb.append("\n");
					sb.append(getLevelIndent());
				}
				sb.append(toStartTag(name));
				sb.append(toXmlValue());
				sb.append(toEndTag(name));
			}
			else {
				sb.append(" ").append(name).append("=\"");
				sb.append(toXmlValue()).append("\"");;
			}
		}
		else if(type ==2) {
			if(showPretty) {
				sb.append("\n");
				sb.append(getLevelIndent());
			}
			if(fieldType == 0) {
				sb.append(toStartTag(name));
			}
			else {
				sb.append("<").append(name);
			}
			Iterator<String> keyIter = childs.keySet().iterator();
			int i=0;
			while(keyIter.hasNext()) {
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				sb.append(item.toXML(encode, showPretty));
				i++;
			}
			
			if(fieldType == 0) {
				if(showPretty) { 
					sb.append("\n");
					sb.append(getLevelIndent());
				}
				sb.append(toEndTag(name));
			}
			else {
				sb.append(" />");;
			}
		}
		else if(type ==3) {
			if(showPretty) {
				sb.append(getLevelIndent());
			}
			for(int p=0; p<list.size(); p++) {
				if(showPretty) { 
					sb.append("\n");
					sb.append(getLevelIndent());
				}	
				sb.append(toStartTag(name));
				LinkedHashMap<String , StandardItem> group = list.get(p);
				Iterator<String> keyIter = group.keySet().iterator();
				int i=0;
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = group.get(key);
					sb.append(item.toXML(encode, showPretty));
					i++;
				}
				if(showPretty) {
					sb.append("\n");
					sb.append(getLevelIndent());
				}
				sb.append(toEndTag(name));												
			}			
		}
		return sb.toString();
	}
	
	
	public byte[] toByteArray() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			if(type ==0) {
				Iterator<String> keyIter = childs.keySet().iterator();
				int i=0;
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = childs.get(key);
					bos.write(item.toByteArray());
					i++;
				}
			}
			if(type ==1) {							
				if (getValue() == null) {
	            	bos.write( new byte[getLength()] );	            	
	            } else if (dataType == 0) {	            	
	            	bos.write( ByteUtil.padding(getBytesValue(), getLength()) );
	            } else if (dataType == 1) {
	            	bos.write( ByteUtil.padding(getValue(), getLength(), (byte)'0', true) );
	            } else {	            	
	            	bos.write( ByteUtil.padding(getBytesValue(), getLength()) );
	            }
			}
			else if(type ==2) {
				Iterator<String> keyIter = childs.keySet().iterator();
				int i=0;
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = childs.get(key);				
					bos.write(item.toByteArray());
					i++;
				}			
			}
			else if(type ==3) {			
				for(int p=0; p<list.size(); p++) {					
					LinkedHashMap<String , StandardItem> group = list.get(p);
					Iterator<String> keyIter = group.keySet().iterator();
					int i=0;
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = group.get(key);
						bos.write(item.toByteArray());
						i++;
					}												
				}			
			}
			return bos.toByteArray();
		} catch(Exception e) {
    		e.printStackTrace();
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
}