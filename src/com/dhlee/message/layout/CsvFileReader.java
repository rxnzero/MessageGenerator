package com.dhlee.message.layout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.SrandardMessageTest;
import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardMessageUtil;
import com.dhlee.message.StandardType;

public class CsvFileReader implements LayoutReader {
	static Logger logger = LoggerFactory.getLogger(CsvFileReader.class);
	String COMMA_DELIMITER = ",";
	String COMMENT_HEADER = "#";
	public CsvFileReader() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ArrayList<StandardItem> parse(String filePath) throws Exception {
		ArrayList<StandardItem>  list = new ArrayList<StandardItem>();
		BufferedReader br = null;
		int lineNo = 0;
		try {
			br = new BufferedReader(new FileReader(filePath));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	lineNo++;
		    	if(line.startsWith(COMMENT_HEADER)) {
		    		continue;
		    	}
		        String[] values = line.split(COMMA_DELIMITER);
		        StandardItem item = new StandardItem(values);		        
	            list.add(item);
		    }
		}
		catch(Exception ex) {
        	throw new Exception(String.format("csv file %s parsing error lineNo=%d", filePath, lineNo), ex);
        }
		finally {
			if(br != null) br.close();
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
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
	}
}
