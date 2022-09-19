package com.dhlee.message.parser;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardType;

public class XmlReader implements StandardReader {
	static Logger logger = null;
	private String FIELD_SEPARATOR = "/"; // XML use /
	private boolean ZERO_BASE_INDEX = false; // 1 base
	
	public XmlReader() {
		if(logger == null) logger = LoggerFactory.getLogger(this.getClass());
	}
	
	public StandardMessage parse(StandardMessage message, String xmlString) throws Exception {
		org.dom4j.io.SAXReader saxReader = new org.dom4j.io.SAXReader();
        org.dom4j.Document document = null;
		try {
			document = saxReader.read(new StringReader(xmlString));
		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		}
        org.dom4j.Element root = document.getRootElement();
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
		String rootName = root.getName();
		message.setName(rootName);
		traverse(message, root, null, itemMap);
		for(String key:itemMap.keySet()) {
			logger.debug("~ itemMap : " + key + " => " + itemMap.get(key));
		}
		return message;		
	}
	
	private void traverse(StandardMessage item, org.dom4j.Element root, String parentName, LinkedHashMap<String, String> itemMap){
		StandardItem currentItem = null;
		String fullPath = null;
		String fieldName = null;
		if(root == null) {
			logger.debug("$ SKIP :: node is NULL name="+ parentName);
			return;
		}
		Iterator<org.dom4j.Element> i = root.elementIterator();
		while (i.hasNext()) {
			org.dom4j.Element child = (org.dom4j.Element) i.next();
// if necessary, uncomment below 2 lines.
//	    	QName qn = new QName(child.getName(),Namespace.NO_NAMESPACE);
//           child.setQName(qn);
            
            fullPath = child.getUniquePath();
            fieldName = lastNodeName(fullPath);
            
            fieldName = genPath(parentName, fieldName);
            currentItem = item.findItem(fieldName, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
            if(currentItem == null) continue;
            
            if(currentItem.getType() == StandardType.BIZDATA) {
    			String bizData = child.asXML();
    			itemMap.put(parentName , bizData);
    	        currentItem.setValue(bizData);
    	        item.setBizData(bizData);
    	        return;
    		}
            
            switch(child.getNodeType()) {
            	case  Node.ELEMENT_NODE:
            		logger.debug("$ ELEMENT_NODE name="+ fieldName + ", fullPath="+ fullPath+ ", value="+ child.getStringValue());            	
                	if(currentItem.getType() == StandardType.FIELD) {
                		itemMap.put(fieldName , child.getStringValue());
            	        currentItem.setValue(child.getStringValue());
                	}
                	else {
                		int attrCnt = child.attributeCount();
                		for (int ai=0; ai<attrCnt; ai++) {
                			Attribute at = child.attribute(ai);
                			String atPath = genPath(fieldName, at.getName());
                			logger.debug("@ ATTRIBUTE name="+ atPath + ", fullPath="+ at.getUniquePath()+ ", value="+ at.getStringValue());
                			StandardItem attrItem = item.findItem(atPath, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
                			itemMap.put(atPath, at.getStringValue());
                			attrItem.setValue(at.getStringValue());
                		}
                		traverse(item, child, fieldName, itemMap);
                	}
            		break;
            	case  Node.TEXT_NODE:
            		logger.debug("$ TEXT_NODE value="+ child.getStringValue());
            		break;
            	default:
            		logger.debug("$ type="+ child.getNodeType() + ", name=" + fieldName);
            		break;
            }
		}
	}
    
	private String genPath(String parent, String nodeName) {
		if(parent == null || parent.length() < 1) {
			return nodeName;
		}
		return parent + FIELD_SEPARATOR + nodeName;
	}
	
	protected String lastNodeName(String xpath) {
		String itemPath = null;
		int sepPosition = xpath.lastIndexOf(FIELD_SEPARATOR);
		if(sepPosition < 0) {
			return xpath;
		}
		itemPath = xpath.substring(sepPosition+1);
		
        return itemPath;
    }

//	public static void main(String[] args) {
//	}
}
