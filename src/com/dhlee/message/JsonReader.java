package com.dhlee.message;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonReader implements StandardReader {
	static Logger logger = null;
	private String FIELD_SEPARATOR = ".";
	private boolean ZERO_BASE_INDEX = true;
	
	public JsonReader() {
		if(logger == null) logger = LoggerFactory.getLogger(this.getClass());
	}

	public StandardMessage parse(StandardMessage message, String jsonString) {
		JsonNode jsonNode = null;
	    ObjectMapper mapper =  null;
	    JsonFactory factory = null;
	    JsonParser parser = null;
	    mapper = new ObjectMapper();
	    mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		factory = mapper.getFactory();
		try {
			parser = factory.createParser(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			jsonNode = mapper.readTree(parser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		ArrayList<StandardItem> list = message.toList();
//		for(int i=0; i<list.size(); i++) {
//			StandardItem item = list.get(i);
//			logger.debug( i + " - "+ item.getLevel() + " : " +item.getName());
//		}
//		ArrayList<StandardItem> childList = message.getChildsList();
//		int i=0;
//		for(StandardItem item:childList) {
//			logger.debug( i++ + " - "+ item.getLevel() + " : " +item.getName());
//			jsonNode.get(item.getName());
//		}
		
		// make json map(path, value)
		LinkedHashMap<String, String> itemMap = new LinkedHashMap<String, String>();
		traverse(message, jsonNode, null, itemMap);

		for(String key:itemMap.keySet()) {
			logger.debug("~ itemMap : " + key + " => " + itemMap.get(key));
		}
		return message;		
	}
	
	private void traverse(StandardMessage item, JsonNode root, String parentName, LinkedHashMap<String, String> itemMap){
		StandardItem currentItem = null;
		String fieldName = null;
		if(parentName != null) {
			logger.debug("$ findItem :: findItem="+ parentName);
			currentItem = item.findItem(parentName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
			if(currentItem == null) {
				logger.debug("$ SKIP :: undefined path in StandardMessage node name="+ parentName);
				return;
			}			
		}
		if(root == null) {
			logger.debug("$ SKIP :: node is NULL name="+ parentName);
			return;
		}
		
		if( currentItem!=null && currentItem.getType() == StandardType.BIZDATA) {
			logger.warn("BIZDATA1 : {}", root.toString());
			logger.warn("BIZDATA2 : {}", root.asText());
			String bizData = root.toString();
			itemMap.put(parentName , bizData);
	        currentItem.setValue(bizData);
	        item.setBizData(bizData);
	        return;
		}
		
		switch(root.getNodeType()) {
			case OBJECT:
				Iterator<String> fieldNames = root.fieldNames();
		        while(fieldNames.hasNext()) {
		            fieldName = fieldNames.next();
		            JsonNode fieldValue = root.get(fieldName);
		            fieldName = genPath(parentName, fieldName);
		            traverse(item, fieldValue, fieldName, itemMap);
		        }
				break;
			case ARRAY:
				ArrayNode arrayNode = (ArrayNode) root;
		        for(int i = 0; i < arrayNode.size(); i++) {
		            JsonNode arrayElement = arrayNode.get(i);
		            traverse(item, arrayElement, parentName+"["+ i+"]", itemMap);
		        }
				break;
			default:
				logger.debug("parsing : " + parentName + "=" +root.asText());
		        itemMap.put(parentName , root.asText());
		        currentItem.setValue(root.asText());
				break;				
		}
	}

	private String genPath(String parent, String nodeName) {
		if(parent == null || parent.length() < 1) {
			return nodeName;
		}
		return parent +FIELD_SEPARATOR + nodeName;
	}

//	public static void main(String[] args) {
//	}

}
