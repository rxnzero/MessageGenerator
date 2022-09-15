package com.dhlee.message;

import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Node;

public class XmlReader implements StandardReader {
	static boolean debug = true;
	private String FIELD_SEPARATOR = "/";
	private boolean ZERO_BASE_INDEX = false;
	
	public XmlReader() {
	}
	
	public StandardMessage parse(StandardMessage message, String xmlString) {
		org.dom4j.io.SAXReader saxReader = new org.dom4j.io.SAXReader();
        org.dom4j.Document document = null;
		try {
			document = saxReader.read(new StringReader(xmlString));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        org.dom4j.Element root = document.getRootElement();
//		ArrayList<StandardItem> list = message.toList();
//		for(int i=0; i<list.size(); i++) {
//			StandardItem item = list.get(i);
//			debug( i + " - "+ item.getLevel() + " : " +item.getName());
//		}
		
		
//		ArrayList<StandardItem> childList = message.getChildsList();
//		int i=0;
//		for(StandardItem item:childList) {
//			debug( i++ + " - "+ item.getLevel() + " : " +item.getName());
//			jsonNode.get(item.getName());
//		}
		
		// make json map(path, value)
		LinkedHashMap<String, String> itemMap = new LinkedHashMap<String, String>();
		String rootName = root.getName();
		message.setName(rootName);
		traverse(message, root, null, itemMap);
		for(String key:itemMap.keySet()) {
			debug("~ itemMap : " + key + " => " + itemMap.get(key));
		}
		return message;		
	}
	
	private void traverse(StandardMessage item, org.dom4j.Element root, String parentName, LinkedHashMap<String, String> itemMap){
		StandardItem currentItem = null;
		String fullPath = null;
		String fieldName = null;
		if(root == null) {
			debug("$ SKIP :: node is NULL name="+ parentName);
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
            if(currentItem == null) return;
            
            if(Node.ELEMENT_NODE== child.getNodeType()) {
            	debug("$ ELEMENT_NODE name="+ fieldName + ", fullPath="+ fullPath+ ", value="+ child.getStringValue());            	
            	if(currentItem.getType() == StandardType.FIELD.getValue()) {
            		itemMap.put(fieldName , child.getStringValue());
        	        currentItem.setValue(child.getStringValue());
            	}
            	else {
            		int attrCnt = child.attributeCount();
            		for (int ai=0; ai<attrCnt; ai++) {
            			Attribute at = child.attribute(ai);
            			String atPath = genPath(fieldName, at.getName());
            			debug("@ ATTRIBUTE name="+ atPath + ", fullPath="+ at.getUniquePath()+ ", value="+ at.getStringValue());
            			StandardItem attrItem = item.findItem(atPath, FIELD_SEPARATOR, ZERO_BASE_INDEX, true);
            			itemMap.put(atPath, at.getStringValue());
            			attrItem.setValue(at.getStringValue());
            		}
            		traverse(item, child, fieldName, itemMap);
            	}
            }
            else if(Node.TEXT_NODE == child.getNodeType()) {
            	debug("$ TEXT_NODE value="+ child.getStringValue());
            }
            else {
            	debug("$ type="+ child.getNodeType() + ", name=" + fieldName);
            }
		}
	}
    
	private String genPath(String parent, String nodeName) {
		if(parent == null || parent.length() < 1) {
			return nodeName;
		}
		return parent + FIELD_SEPARATOR + nodeName;
	}
	
	private static void debug(String msg) {
		if(debug) System.out.println(msg);
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
