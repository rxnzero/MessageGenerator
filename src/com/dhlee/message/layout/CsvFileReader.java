package com.dhlee.message.layout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardMessageUtil;

public class CsvFileReader implements LayoutReader {
	static Logger logger = LoggerFactory.getLogger(CsvFileReader.class);
	String COMMA_DELIMITER = ",";
	String COMMENT_HEADER = "#";
	
	public CsvFileReader() {
	}
	
	private BufferedReader getReader(String filePath) throws Exception {
		BufferedReader reader = null;
		logger.info("getReader from filepath. read file = {} ", filePath);
		try {
			reader = new BufferedReader(new FileReader(filePath));
			return reader;
		}
		catch(Exception ex) {
			logger.warn("getReader failed. read file = {} ", filePath, ex);
		}
		try {
			logger.info("getReader from classpath. read file = {} ", filePath);
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			reader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(filePath)) );			
			return reader;
		}
		catch(Exception ex) {
			logger.warn("getReader failed. read classpath file = {} ", filePath, ex);
		}
		throw new Exception("find not found : " + filePath);
	}
	
	@Override
	public ArrayList<StandardItem> parse(String filePath) throws Exception {
		logger.info("CsvFileReader read file = {} ", filePath);
		
		ArrayList<StandardItem>  list = new ArrayList<StandardItem>();

		int lineNo = 0;
		try (BufferedReader br = getReader(filePath)) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	lineNo++;
		    	if(line.startsWith(COMMENT_HEADER)) {
		    		logger.debug("SKIP comment {} : {} ", lineNo, line);
		    		continue;
		    	}
		    	logger.debug("Parsing line {} : {} ", lineNo, line);
		        String[] values = line.split(COMMA_DELIMITER, StandardItem.ITEM_COUNT);
		        StandardItem item = new StandardItem(values);		        
	            list.add(item);
		    }
		}
		catch(Exception ex) {
        	throw new Exception(String.format("csv file %s parsing error lineNo=%d", filePath, lineNo), ex);
        }
		return list;
	}
	
	public static void main(String[] args) {
		ArrayList<StandardItem> list = null;
		String filePath = "./resources/standard-layout.csv";
		try {
			LayoutReader reader = new CsvFileReader();
			list = (ArrayList<StandardItem>) reader.parse(filePath);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		StandardMessage message = null;;
		try {
			message = StandardMessageUtil.generate(list);
			logger.debug("@toCSVString\n" + message.toCSVString());
			logger.debug("@toString\n" + message.toString());
			logger.debug("@toJson\n" + message.toJson());
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
}
