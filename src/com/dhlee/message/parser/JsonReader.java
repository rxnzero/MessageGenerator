package com.dhlee.message.parser;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardType;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonReader implements StandardReader {
	static Logger logger = LoggerFactory.getLogger(JsonReader.class);
	private char FIELD_SEPARATOR = '.';
	private boolean ZERO_BASE_INDEX = true;
	
	public JsonReader() {
		
	}

	public StandardMessage parse(StandardMessage message, String jsonString) throws Exception {
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
			throw e;
		}
		try {
			jsonNode = mapper.readTree(parser);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
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
	
	private void traverse(StandardMessage message, JsonNode currentNode, String parentName, LinkedHashMap<String, String> itemMap){
		StandardItem currentItem = null;
		String fieldName = null;
		if(parentName != null) {
			logger.debug("$ findItem :: findItem="+ parentName);
			currentItem = message.findItem(parentName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
			if(currentItem == null) {
				logger.debug("$ SKIP :: undefined path in StandardMessage node name="+ parentName);
				return;
			}			
		}
		if(currentNode == null) {
			logger.debug("$ SKIP :: node is NULL name="+ parentName);
			return;
		}
		
		if( currentItem!=null && currentItem.getType() == StandardType.BIZDATA) {
			logger.warn("BIZDATA1 : {}", currentNode.toString());
			logger.warn("BIZDATA2 : {}", currentNode.asText());
			String bizData = currentNode.toString();
			itemMap.put(parentName , bizData);
	        currentItem.setValue(bizData);
//	        item.setBizData(bizData);
	        return;
		}
		
		switch(currentNode.getNodeType()) {
			case OBJECT:
				if( currentItem != null && currentItem.getSize() == 0 
				&& StringUtils.isNoneEmpty(currentItem.getRefPath()) 
				&& StringUtils.isNoneEmpty(currentItem.getRefValue()) ) {
					String refItemValue = itemMap.get(currentItem.getRefPath());
				
					logger.debug("@GROUP name={}, getRefPath={}, refItemValue=[{}], getRefValue=[{}]"
							, currentItem.getName(), currentItem.getRefPath(), refItemValue, currentItem.getRefValue());
					
					if( !currentItem.getRefValue().equals(refItemValue) ) {
						logger.warn("@GROUP name={} SKIPPED.", currentItem.getName());
							break;
					}
				}
				Iterator<String> fieldNames = currentNode.fieldNames();
		        while(fieldNames.hasNext()) {
		            fieldName = fieldNames.next();
		            JsonNode fieldValue = currentNode.get(fieldName);
		            fieldName = genPath(parentName, fieldName);
		            traverse(message, fieldValue, fieldName, itemMap);
		        }
				if(currentItem != null &&  currentItem.getSize() == 0) {
					currentItem.setSize(1);
				}
				break;
			case ARRAY:
				logger.debug("@ARRAY name={}, getLength={}, getRefPath={}, getRefValue=[{}]"
						, currentItem.getName(), currentItem.getLength(), currentItem.getRefPath(), currentItem.getRefValue());
				if( currentItem != null && currentItem.getSize() == 0 
					&& StringUtils.isNoneEmpty(currentItem.getRefPath()) 
					&& StringUtils.isNoneEmpty(currentItem.getRefValue()) ) {
					String refItemValue = itemMap.get(currentItem.getRefPath());
				
					logger.debug("@ARRAY name={}, getRefPath={}, refItemValue=[{}], getRefValue=[{}]"
							, currentItem.getName(), currentItem.getRefPath(), refItemValue, currentItem.getRefValue());
					
					if( !currentItem.getRefValue().equals(refItemValue) ) {
						logger.warn("@ARRAY name={} SKIPPED.", currentItem.getName());
							break;
					}
				}
				ArrayNode arrayNode = (ArrayNode) currentNode;
		        for(int i = 0; i < arrayNode.size(); i++) {
		            JsonNode arrayElement = arrayNode.get(i);
		            traverse(message, arrayElement, parentName+"["+ i+"]", itemMap);
		        }
				break;
			default:
				logger.debug("parsing : " + parentName + "=" +currentNode.asText());
		        itemMap.put(parentName , currentNode.asText());
		        if(currentItem.getType() == StandardType.FARRAY) {
		        	currentItem.addValue(currentNode.asText());
		        }
		        else {
		        	currentItem.setValue(currentNode.asText());
		        }
				
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
