package com.dhlee.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class StandardItem implements Serializable, Cloneable {
	private static final long serialVersionUID = -4430144517271735912L;
	static Logger logger = LoggerFactory.getLogger(StandardItem.class);
	LinkedHashMap<String , StandardItem> childs = new LinkedHashMap<String , StandardItem>();
	ArrayList<LinkedHashMap<String , StandardItem>> list = new ArrayList<LinkedHashMap<String , StandardItem>>();
	
	ArrayList<String> values = new ArrayList<String>();
	
	// FIX-ME : check encoding
	// String <-> byte[] charset
	String encode = "euc-kr";
	
	public static int ITEM_COUNT = 11;
	String name;
	int level; // 0 : root  ~ n
	int type; // 0: Message, 1: Field, 2:Group,  3 : Array, 4 : Field Array, 9 : BIZ DATA	
	int size; // array size
	int fieldType; // 0: ELEMENT, 1: ATTRIBUTE,
	int length;
	int dataType; // 0: String, 1: Number, 10: ZZ String, 11: Length Number
	String refPath;
	String refValue;
	String value;
	String desc = "";
	
	byte[] bytesValue = null;
	
	// configuration options
	// if data is null, convert to white-space
	private static boolean NULL_TO_SPACE = true;
	
	// if add json root item with message name 
	private static boolean ADD_JSON_ROOT = false;
	
	static {
		try {
			NULL_TO_SPACE = Boolean.parseBoolean( System.getProperty("standard.message.nulltospace", "true") );
		}
		catch(Exception ex) {
			
		}
		try {
			ADD_JSON_ROOT = Boolean.parseBoolean( System.getProperty("standard.message.addjsonroot", "fales") );
		}
		catch(Exception ex) {
			
		}
	}
	
	public StandardItem() {
//		childs = new LinkedHashMap<String, StandardItem>();
//		list = new ArrayList<StandardItem>();
	}
	
	public StandardItem(String name, int level, int type, int size, int fieldType, int length, 
			int dataType, String refPath, String refValue, String value, String desc) {
		super();
		initValues(name, level, type, size, fieldType, length, dataType, refPath, refValue, value, desc);
	}
	
	public StandardItem(String name, int level, int type, int size, int length, int dataType, String value, String desc) {
		super();
		initValues(name, level, type, size, 0, length, dataType, null, null, value, desc);
	}
	public StandardItem(String name, int level, int type, int size, int length, int dataType, String value) {
		super();
		initValues(name, level, type, size, 0, length, dataType, null, null, value, null);
	}
	
	public StandardItem(String name, int level, int type, int length, int dataType, String value, String desc) {
		super();
		initValues(name, level, type, 1, 0, length, dataType, null, null, value, desc);
	}
	
	public StandardItem(String name, int level, int type, int length, int dataType, String value) {
		super();
		initValues(name, level, type, 1, 0, length, dataType, null, null, value, null);
	}
	
	public void initValues(String name, int level, int type, int size, int fieldType, int length, 
			int dataType, String refPath, String refValue, String value, String desc) {
		this.name = name;
		this.level = level;
		this.type = type;
		this.size = size;
		this.fieldType = fieldType;
		this.length = length;
		this.dataType = dataType;
		this.refPath = refPath;
		this.refValue = refValue;
		setValue(value);
		if(desc != null) this.desc = desc;
	}
	
	public StandardItem(String[] values) throws Exception {
		if(values == null || values.length < ITEM_COUNT-1) {
			throw new Exception("invalid StandardItem defination values : " + (values==null? "":values.length)); 
		}		
		this.name = values[0].trim();
		this.level = Integer.parseInt(values[1].trim());
		this.type = Integer.parseInt(values[2].trim());
		this.size = Integer.parseInt(values[3].trim());;
		this.fieldType = Integer.parseInt(values[4].trim());
		this.length = Integer.parseInt(values[5].trim());;
		this.dataType = Integer.parseInt(values[6].trim());
		this.refPath = values[7].trim();
		this.refValue = values[8].trim();
		setValue(values[9]);
		if(values.length == ITEM_COUNT) {
			setDesc(values[10].trim());
		}
		else {
			setDesc(values[0].trim());
		}
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
		else if(list.size() > index) {
			list.get(index).put(item.getName(), item);
		}
		else {
			logger.debug("index too big : index={} / array size={}", index, list.size());
		}
	}
	public void addArray(LinkedHashMap<String , StandardItem> items) {
		list.add(items);
	}
	
	public LinkedHashMap<String, StandardItem> getArrayChilds(int index, boolean createNew) {
		if(index == list.size()) {
//			logger.debug("@@ add cloned childs in array item={}, index={}", this.name, index);
			if(createNew) {
				list.add((LinkedHashMap<String , StandardItem>)(SerializationUtils.clone(childs)) );
			}
			else {
				return null;
			}
		}
		else if(index > list.size()) {
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
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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
	
	public String getRefPath() {
		return refPath;
	}

	public void setRefPath(String refPath) {
		this.refPath = refPath;
	}

	public String getRefValue() {
		return refValue;
	}

	public void setRefValue(String refValue) {
		this.refValue = refValue;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
		if(value  != null) { 
			try {
				this.bytesValue = value.getBytes(encode);
			} catch (UnsupportedEncodingException e) {
				this.bytesValue = value.getBytes();
			}
		}
	}

	public void addValue(String value) {
		values.add(value);		
	}
	
	public void addBytesValue(byte[] bytesValue) {
//		this.bytesValue = bytesValue;
		String svalue = null;
		try {
			svalue = new String(bytesValue, encode);				
		} catch (UnsupportedEncodingException e) {
			svalue = new String(bytesValue);
		}
		addValue(svalue);
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
	
	protected String getLevelIndent() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<level; i++) {
			sb.append("  ");
		}
		return sb.toString();
	}
	
	protected String getLevelTreeIndent() {
		StringBuilder sb = new StringBuilder();
		if(level > 0) {
			for(int i=0; i<level-1; i++) {
				sb.append("??");
			}
			sb.append("??");
		}
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLevelTreeIndent()).append("StandardItem [name=" + name + ", level=" + level 
				+ ", type=" + type +", size=" + size + ", fieldType=" + fieldType +", length=" + length + ", dataType=" + dataType
				+ ", refPath=" + refPath + ", refValue=" + refValue
				+ ", value=" + value
				+ ", desc=" + desc + "]");
		Iterator<String> keyIter = null;;
		switch(getType())  {
			case StandardType.MESSAGE :
				keyIter = childs.keySet().iterator();
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = childs.get(key);
					sb.append("\n").append(item.toString());
				}
				break;
//			case StandardType.FIELD :
//				break;
			case StandardType.GROUP :
				keyIter = childs.keySet().iterator();
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = childs.get(key);
					sb.append("\n").append(item.toString());
				}
				break;
			case StandardType.GRID :
				for(int p=0; p<list.size(); p++) {
					LinkedHashMap<String , StandardItem> group = list.get(p);
					keyIter = group.keySet().iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = group.get(key);
						sb.append("\n").append(item.toString());
					}				
				}
				break;
//			case StandardType.FARRAY :
//				break;
//			case StandardType.BIZDATA :
//				break;
			default :
				break;
		}
		return sb.toString();
	}
	
	public String toCSVString() {
		StringBuilder sb = new StringBuilder();
		
		if(level == 0) {
			sb.append("#name,level,type,size,fieldType,length,dataType,refPath,refValue,value,desc\n");
		}
		sb.append(name);
		sb.append(",");
		sb.append(level);
		sb.append(",");
		sb.append(type);
		sb.append(",");
		sb.append(size);
		sb.append(",");
		sb.append(fieldType);
		sb.append(",");
		sb.append(length);
		sb.append(",");
		sb.append(dataType);
		sb.append(",");
		sb.append(refPath);
		sb.append(",");
		sb.append(refValue);
		sb.append(",");
		sb.append(value);
		sb.append(",");
		sb.append(desc);
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
		ArrayList<StandardItem> childList = null;
		switch(getType())  {
			case StandardType.MESSAGE :
				list.add(this);
				childList = new ArrayList<StandardItem>(childs.values());
				for(int i=0; i<childList.size(); i++) {
					list.addAll(childList.get(i).toList());
				}
				break;
			case StandardType.FIELD :
				list.add(this);
				break;
			case StandardType.GROUP :
				list.add(this);
				childList = new ArrayList<StandardItem>(childs.values());
				for(int i=0; i<childList.size(); i++) {
					list.addAll(childList.get(i).toList());
				}
				break;
			case StandardType.GRID :
				list.add(this);
				childList = new ArrayList<StandardItem>(childs.values());
				for(int i=0; i<childList.size(); i++) {
					list.addAll(childList.get(i).toList());
				}
				break;
			case StandardType.FARRAY :
				list.add(this);
				break;
			case StandardType.BIZDATA :
				list.add(this);
				break;
			default :
				break;
		}
		return list;
	}
	
	protected String toJsonValue() {
		return toJsonValue(this.value);
	}
	
	protected String toJsonValue(String svalue) {
		if(dataType == StandardDataType.STRING || dataType == StandardDataType.ZZ_STRING) {
			if(svalue == null) {
				if(NULL_TO_SPACE) return "\"\""; 
				else return null;
			}
			return "\"" + svalue + "\"";
		}
		else {
			if(StringUtils.isEmpty(svalue)) {
				return "0";
			}
			else {
				return svalue;
			}
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
		Iterator<String> keyIter = null;
		switch(getType())  {
		case StandardType.MESSAGE :
			sb.append("{");
// if root name add			
			if(ADD_JSON_ROOT) sb.append("\"").append( name ).append("\"").append(":{");
			if(showPretty) sb.append("\n");
			keyIter = childs.keySet().iterator();
			int i=0;
			String part = null;
			while(keyIter.hasNext()) {
				if(i>0 && StringUtils.isNotEmpty(part)) sb.append(",");
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				if(showPretty && StringUtils.isNotEmpty(part)) sb.append("\n");
				part = item.toJson(showPretty);
				sb.append(part);
				i++;
			}
			if(ADD_JSON_ROOT) sb.append("}");
			if(showPretty) sb.append("\n");
			sb.append("}");
			break;
		case StandardType.FIELD :
			if(showPretty) sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\":").append(toJsonValue());
			break;
		case StandardType.GROUP :
			// skip variable group
			if( getSize() == 0) {
				break;
			}
			if(showPretty) sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\":{");
			keyIter = childs.keySet().iterator();
			int g=0;
			while(keyIter.hasNext()) {
				if(g>0) sb.append(",");
				String key = keyIter.next();
				StandardItem item = childs.get(key);
				if(showPretty) sb.append("\n");
				sb.append(item.toJson(showPretty));
				g++;
			}
			if(showPretty) {
				sb.append("\n");
				sb.append(getLevelIndent());
			}
			sb.append("}");
			break;
		case StandardType.GRID :
			if(showPretty) {				
				sb.append(getLevelIndent());
			}
			sb.append("\"").append(name).append("\":[");
//			if(showPretty) sb.append("\n");
			for(int p=0; p<list.size(); p++) {
				LinkedHashMap<String , StandardItem> group = list.get(p);
				keyIter = group.keySet().iterator();
				int ai=0;
				if(showPretty) {
					sb.append("\n");
					sb.append(getLevelIndent());
				}
				if(p>0) sb.append(",");
				sb.append("{");
				while(keyIter.hasNext()) {
					if(ai>0) sb.append(",");
					String key = keyIter.next();
					StandardItem item = group.get(key);
					if(showPretty) sb.append("\n");
					sb.append(item.toJson(showPretty));
					ai++;
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
			break;
		case StandardType.FARRAY :
			if(showPretty) {				
				sb.append(getLevelIndent());
			}
			sb.append("\"").append(name).append("\":[");
//			if(showPretty) sb.append("\n");
			for(int p=0; p<values.size(); p++) {				
				if(p>0) sb.append(",");
				sb.append(toJsonValue(values.get(p)));
			}
//			if(showPretty) {
//				sb.append("\n");
//				sb.append(getLevelIndent());
//			}
			sb.append("]");
			break;	
		case StandardType.BIZDATA :
			if(showPretty) sb.append(getLevelIndent());
			sb.append("\"").append(name).append("\":");
			if(StringUtils.isEmpty(getValue())) {
				sb.append(toJsonValue(null));
			}
			else {
				sb.append(getValue());
			}
			break;
		default :
			break;
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
		return toXML(encode, false);
	}
	
	public String toPrettyXML() {
		return toXML(encode, true);
	}
	
	// FIX-ME : Attribute?? Element?? ?????? Group/Array?? ???? ???? ??????.
	//            XML?? Attribute Group?? ???? Group?? Item?? FieldType?? ???? 1 ?? ?????????? ??.
	//            ???? XMLParser?? ???????? ?????? ?????? ???? ??.
	public String toXML(String encode, boolean showPretty) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> keyIter = null;
		switch(getType()) {
			case StandardType.MESSAGE :
				sb.append("<?xml version=\"1.0\" encoding=\""+encode+"\"?>");
				if(showPretty) sb.append("\n");
				
				sb.append(toStartTag(name));
				
				keyIter = childs.keySet().iterator();
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = childs.get(key);
					sb.append(item.toXML(encode, showPretty));
				}
				
				if(showPretty) sb.append("\n");
				sb.append(toEndTag(name));
				break;
			case StandardType.FIELD :
				if(fieldType == StandardFieldType.ELEMENT) {
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
				break;
			case StandardType.GROUP :
				// skip variable group
				if( getSize() == 0) {
					break;
				}
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
				keyIter = childs.keySet().iterator();
				while(keyIter.hasNext()) {
					String key = keyIter.next();
					StandardItem item = childs.get(key);
					sb.append(item.toXML(encode, showPretty));
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
				break;
			case StandardType.GRID :
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
					keyIter = group.keySet().iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = group.get(key);
						sb.append(item.toXML(encode, showPretty));
					}
					if(showPretty) {
						sb.append("\n");
						sb.append(getLevelIndent());
					}
					sb.append(toEndTag(name));												
				}
				break;
			case StandardType.FARRAY :
				if(showPretty) {
					sb.append(getLevelIndent());
				}
				for(int p=0; p<values.size(); p++) {
					if(showPretty) { 
						sb.append("\n");
						sb.append(getLevelIndent());
					}	
					sb.append(toStartTag(name));
					sb.append(values.get(p));
					sb.append(toEndTag(name));												
				}
				break;	
			case StandardType.BIZDATA :
				if(showPretty) {
					sb.append("\n");
					sb.append(getLevelIndent());
				}
				sb.append(toStartTag(name));
				sb.append(getValue());
				sb.append(toEndTag(name));
				break;
			default :
				break;			
		}
		return sb.toString();
	}
	
	public int getBytesDataLength() {
		int totalSize = 0;
		Iterator<String> keyIter = null;
		try {
			switch(getType()) {
				case StandardType.MESSAGE :
					keyIter = childs.keySet().iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = childs.get(key);
						totalSize +=item.getBytesDataLength();
					}
					break;
				case StandardType.FIELD :
					totalSize += getLength();
					break;
				case StandardType.GROUP :
					// skip variable group
					if( getSize() == 0) {
						break;
					}
					keyIter = childs.keySet().iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = childs.get(key);				
						totalSize +=item.getBytesDataLength();
					}			
					break;
				case StandardType.GRID :			
					for(int p=0; p<list.size(); p++) {					
						LinkedHashMap<String , StandardItem> group = list.get(p);
						keyIter = group.keySet().iterator();
						while(keyIter.hasNext()) {
							String key = keyIter.next();
							StandardItem item = group.get(key);
							totalSize +=item.getBytesDataLength();
						}												
					}
					break;
				case StandardType.FARRAY :			
					for(int p=0; p<values.size(); p++) {					
						totalSize += getLength();												
					}
					break;	
				case StandardType.BIZDATA :							
					totalSize += getBytesValue().length;
		            
					break;	
				default :
					break;
			}		
			return totalSize;
		} catch(Exception e) {
    		logger.error("getBytesDataLength failed", e);
    		return totalSize;
    	}
    	finally {
    	}
	}
	
	public String toFixedString() {
		return toFixedString(true);
	}
	
	public String toFixedString(boolean withBizDta) {
		byte[] bytes = toByteArray(withBizDta);
		try {
			return new String(bytes, encode);
		} catch (UnsupportedEncodingException e) {
			logger.error("encoding fail, use default.", e);
			return new String(bytes);
		}
	}
	
	public byte[] toByteArray() {
		return toByteArray(true);
	}
	public byte[] toByteArray(boolean withBizDta) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Iterator<String> keyIter = null;
		try {
			switch(getType()) {
				case StandardType.MESSAGE :
					keyIter = childs.keySet().iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = childs.get(key);
						bos.write(item.toByteArray());
					}
					break;
				case StandardType.FIELD :
					if (getValue() == null) {
		            	bos.write( new byte[getLength()] );	            	
		            } else if (dataType == StandardDataType.STRING || dataType == StandardDataType.ZZ_STRING) {	            	
		            	bos.write( ByteUtil.padding(getBytesValue(), getLength()) );
		            } else if (dataType == StandardDataType.NUMBER || dataType == StandardDataType.LL_NUMBER) {
		            	bos.write( ByteUtil.padding(getValue(), getLength(), (byte)'0', true) );
		            } else {	            	
		            	bos.write( ByteUtil.padding(getBytesValue(), getLength()) );
		            }
					break;
				case StandardType.GROUP :
					// skip variable group
					if( getSize() == 0) {
						break;
					}
					keyIter = childs.keySet().iterator();
					while(keyIter.hasNext()) {
						String key = keyIter.next();
						StandardItem item = childs.get(key);				
						bos.write(item.toByteArray());
					}			
					break;
				case StandardType.GRID :			
					for(int p=0; p<list.size(); p++) {					
						LinkedHashMap<String , StandardItem> group = list.get(p);
						keyIter = group.keySet().iterator();
						while(keyIter.hasNext()) {
							String key = keyIter.next();
							StandardItem item = group.get(key);
							bos.write(item.toByteArray());
						}												
					}
					break;
				case StandardType.FARRAY :			
					for(int p=0; p<values.size(); p++) {					
						if (values.get(p) == null) {
			            	bos.write( new byte[getLength()] );	            	
			            } else if (dataType == StandardDataType.STRING || dataType == StandardDataType.ZZ_STRING) {	            	
			            	bos.write( ByteUtil.padding(values.get(p).getBytes(encode), getLength()) );
			            } else if (dataType == StandardDataType.NUMBER || dataType == StandardDataType.LL_NUMBER) {
			            	bos.write( ByteUtil.padding(values.get(p).getBytes(), getLength(), (byte)'0', true) );
			            } else {	            	
			            	bos.write( ByteUtil.padding(values.get(p).getBytes(encode), getLength()) );
			            }												
					}
					break;	
				case StandardType.BIZDATA :
					if(withBizDta) {
						if (getValue() == null) {
			            	bos.write( new byte[getLength()] );	            	
			            } else {	            	
			            	bos.write( getBytesValue() );
			            }
					}
					break;	
				default :
					break;
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
}