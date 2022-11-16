package com.dhlee.test;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.message.StandardMessageUtil;
import com.dhlee.message.StandardType;
import com.dhlee.message.filter.FlatMessageFilter;
import com.dhlee.message.filter.MessageFilter;
import com.dhlee.message.manager.StandardMessageManager;
import com.dhlee.message.parser.StandardReader;
import com.dhlee.message.parser.XmlReader;

public class SrandardMessageTest {
	static Logger logger = LoggerFactory.getLogger(SrandardMessageTest.class);
	static String strString ="000000000100000000020000000003KB100000000210000000022000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313";

	static String jsonString = "{" 
			+"  \"Header\": {" 
			+"    \"StndCicsTrncd\":\"JI6H\", " 
			+"    \"StndIntnlStndTelgmLen\":100, " 
			+"    \"StndTranBaseYmd\":\"20220913\"" 
			+"  }, " 
			+"  \"Common\": {" 
			+"    \"TranInfo\": {" 
			+"      \"StndCicsTrncd\":\"KB0\", " 
			+"      \"StndTelgmRecvTranCd\":\"EDU0100101\", " 
			+"      \"StndPrcssRtdTranCd\":\"JI6HWSE02002\"" 
			+"    }" 
			+"  }, " 
			+"  \"Array\": [" 
			+"  {" 
			+"    \"Item1\":\"ARR1\", " 
			+"    \"Group\": [{" 
			+"      \"gitem1\":100, " 
			+"      \"gitem2\":\"20220913\"" 
			+"    }]" 
			+"  }" 
			+"  ,{" 
			+"    \"Item1\":\"ARR2\", " 
			+"    \"Group\": [{" 
			+"      \"gitem1\":200, " 
			+"      \"gitem2\":\"20220914\"" 
			+"    }]" 
			+"  }"
			+",{" 
			+"    \"Item1\":\"ARR3\", " 
			+"    \"Group\": [{" 
			+"      \"gitem1\":301, " 
			+"      \"gitem2\":\"20220915\"" 
			+"    },{" 
			+"      \"gitem1\":302, " 
			+"      \"gitem2\":\"20220915\"" 
			+"    }]" 
			+"  }"
			+"  ]"
			+"  , \"AddOn\": {" 
			+"    \"StndCicsTrncd\":\"TEST\", " 
			+"    \"StndIntnlStndTelgmLen\":100, " 
			+"    \"StndTranBaseYmd\":\"20220913\"" 
			+"  }" 
			+"  , \"bizData\": {" 
			+"    \"bizfield1\":\"BIZ1\", " 
			+"    \"bizfield2\":\"BIZ2\", " 
			+"    \"bizfield3\":\"BIZ3\" " 
			+"  }"
			+"  ,\"FArray\": [\"STR1\", \"STR2\",\"STR3\"]"
			+"}";
	
	static String xmlString = 
			"<Root>"
			
// Case Attributes
			+"<Header StndCicsTrncd=\"JI6H\" StndIntnlStndTelgmLen=\"726\" StndTranBaseYmd=\"20220905\" />"
// Case Elements
//			+"<Header>"
//			+" <StndCicsTrncd>JI6H</StndCicsTrncd>"
//			+" <StndIntnlStndTelgmLen>726</StndIntnlStndTelgmLen>"
//			+" <StndTranBaseYmd>20220905</StndTranBaseYmd>"
//		+"</Header>"
			
		+"<Common>"
			+"<TranInfo>"
				+" <StndCicsTrncd>KB0</StndCicsTrncd>"
				+" <StndTelgmRecvTranCd>EDU0100101</StndTelgmRecvTranCd>"
				+" <StndPrcssRtdTranCd>JI6HWSE02002</StndPrcssRtdTranCd>"
			+"</TranInfo>"
		+"</Common>"
		+"<Array>"
		+" <Item1>ARR1</Item1>"
		+"<Group>"
			+" <gitem1>100</gitem1>"
			+" <gitem2>20220913</gitem2>"
		+"</Group>"
		+"</Array>"
		+"<Array>"
		+" <Item1>ARR2</Item1>"
		+"<Group>"
			+" <gitem1>200</gitem1>"
			+" <gitem2>20220914</gitem2>"
		+"</Group>"
		+"</Array>"
		+"<Array>"
		+" <Item1>ARR3</Item1>"
		+"<Group>"
			+"<gitem1>301</gitem1>"
			+"<gitem2>20220915</gitem2>"
		+"</Group>"
		+"<Group>"
			+"<gitem1>302</gitem1>"
			+"<gitem2>20220915</gitem2>"
		+"</Group>"
		+"</Array>"
		+"<FArray>STR1</FArray>"
		+"<FArray>STR2</FArray>"
		+"<FArray>STR3</FArray>"
		+"<AddOn>"
		+"<StndCicsTrncd>TEST</StndCicsTrncd>"
		+"<StndIntnlStndTelgmLen>100</StndIntnlStndTelgmLen>"
		+"<StndTranBaseYmd>20220913</StndTranBaseYmd>"
		+"</AddOn>"
		+"<bizData>"
		+"<bizfield1>BIZ1</bizfield1>"
		+"<bizfield2>BIZ2</bizfield2>"
		+"<bizfield3>BIZ3</bizfield3>"
		+"</bizData>"
	+"</Root>";
	
	public SrandardMessageTest() {

	}
	
	public static StandardMessage generateMessage() {
//		return StandardMessageUtil.generateMessageFromCCsvFile();
		StandardMessageManager manager = StandardMessageManager.getInstance();
		
		StandardMessage message = manager.getStandardMessage();
//		MessageFilter flatFilter = new FlatMessageFilter();
//		message.setFlatFilter(flatFilter);
		return message;
	}
	
	
	public static StandardMessage generateMessageFromItemList() {
		// Test generator from List definition (with default values)
		logger.debug("\n>> ----------------------------------------------------------------------------------");
		logger.debug(">> Test generator from List definition ( with default values)");
		ArrayList<StandardItem> list = new ArrayList<StandardItem>();
		StandardItem group = null;
		StandardItem item = null;
		group = new StandardItem("Header", 1, StandardType.GROUP, 1, 0, 0, null);
		list.add(group);
		item = new StandardItem("StndCicsTrncd", 2, StandardType.FIELD, 1, 10, 0, "");
		list.add(item);
		item = new StandardItem("StndIntnlStndTelgmLen", 2, StandardType.FIELD, 1, 10, 1, "0");
		list.add(item);
		item = new StandardItem("StndTranBaseYmd", 2, StandardType.FIELD, 1, 10, 0, "");
		list.add(item);

		StandardItem group1 = new StandardItem("Common", 1, StandardType.GROUP, 0, 0, null);
		list.add(group1);
		group = new StandardItem("TranInfo", 2, StandardType.GROUP, 0, 0, null);
		list.add(group);
		item = new StandardItem("StndCicsTrncd", 3, StandardType.FIELD, 3, 0, "KB0");
		list.add(item);
		item = new StandardItem("StndTelgmRecvTranCd", 3, StandardType.FIELD, 10, 0, "");
		list.add(item);
		item = new StandardItem("StndPrcssRtdTranCd", 3, StandardType.FIELD, 10, 0, "");
		list.add(item);
		
		group = new StandardItem("Array", 1, StandardType.GRID, 3, 0, null);
		list.add(group);
		item = new StandardItem("Item1", 2, StandardType.FIELD, 10, 0, "JI6H");
		list.add(item);
		item = new StandardItem("Group", 2, StandardType.GRID, 1, 0, null);
		list.add(item);
		item = new StandardItem("gitem1", 3, StandardType.FIELD, 10, 1, "726");
		list.add(item);
		item = new StandardItem("gitem2", 3, StandardType.FIELD, 10, 0, "20220905");
		list.add(item);
		
		item = new StandardItem("bizData", 1, StandardType.BIZDATA, 0, 0, null);
		list.add(item);
		
		StandardMessage message = null;;
		try {
			message = StandardMessageUtil.generate(list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// TODO
		// 1. CSV -> List -> gen
		// 2. XML -> List -> gen
		// 3. JSON -> List -> gen
		logger.debug("@toCSVString\n" + message.toCSVString());
		logger.debug("@toString\n" + message.toString());
		logger.debug("@toJson\n" + message.toJson());
		
		// Test initialize getter
		logger.debug("\n>> ----------------------------------------------------------------------------------");
		logger.debug(">> Test initialize getter");
		String findItem = "StndCicsTrncd";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Header.StndCicsTrncd";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		logger.debug("\n>> ----------------------------------------------------------------------------------");
		logger.debug(">> Test file item with path & set value");
		StandardItem it = message.findItem(findItem);
		if(it !=null) {
			it.setValue("changed!");
		}
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Array[0].Group.gitem2";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Array[0]";
		logger.debug(findItem + " => " +message.findItem(findItem));
		
		return message;
	}
	
	public static void testJsonReader() throws Exception {
		StandardMessage message = null;;
		message = generateMessage();
		if(message == null) {
			return;
		}
		
		logger.debug(">> Test Json Parsing");
		
		logger.debug("jsonString = " +jsonString);
		
		// Test Json parsing
//		StandardReader jsonReader = new JsonReader();
		
		// Test Dynamic Loading
		StandardReader jsonReader = null;
		String readerName = "com.dhlee.message.parser.JsonReader";
		Class cl = null;
		try {
			cl = Class.forName(readerName);
			jsonReader = (StandardReader)cl.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		jsonReader.parse(message, jsonString);
		
		
		logger.debug(">> message=\n" + message);
		logger.debug(">> message.toJson()=\n" + message.toJson());
		logger.debug(">> message.toPrettyJson()=\n" + message.toPrettyJson());
		logger.debug(">> message.toXML()=\n" + message.toXML());
		logger.debug(">> message.toPrettyXML()=\n" + message.toPrettyXML());
		// Test getter after parsing
		logger.debug("\n>> ----------------------------------------------------------------------------------");
		String findItem = null;
		logger.debug(">> Test getter after parsing");
		findItem = "Array[0].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[1].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[2].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));		
		findItem = "Array[2].Group[1].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
	}
	
	public static void testXmlReader() throws Exception {
		StandardMessage message = null;;
		message = generateMessage();
		if(message == null) {
			return;
		}
		
		logger.debug(">> Test Json Parsing");
		
		logger.debug("jsonString = " + xmlString);
		
		// Test XML parsing
		StandardReader jsonReader = new XmlReader();
		jsonReader.parse(message, xmlString);
		
		logger.debug(">> message=\n" + message);
		logger.debug(">> message.toJson()=\n" + message.toJson());
		logger.debug(">> message.toXML()=\n" + message.toXML());
		// Test getter after parsing
		logger.debug("\n>> ----------------------------------------------------------------------------------");
		String findItem = null;
		logger.debug(">> Test getter after parsing");
		findItem = "Array[0].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[1].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[2].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));		
		findItem = "Array[2].Group[1].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
	}
	
	
	public static void testDynamicReader(String messageType, String messageString) throws Exception {
		StandardMessage message = null;
		message = generateMessage();
		if(message == null) {
			return;
		}
		
		logger.debug(">> Test Parsing");
		
		logger.debug("messageString = " +messageString);
		
		// Test Json parsing
		// Test Dynamic Loading
		StandardReader reader = null;
		
		StandardMessageManager manager = StandardMessageManager.getInstance();
		reader = manager.getReader(messageType);
		
		reader.parse(message, messageString);
		
		logger.debug(">> message=\n" + message);
		logger.debug(">> message.toJson()=\n" + message.toJson());
		logger.debug(">> message.toPrettyJson()=\n" + message.toPrettyJson());
		logger.debug(">> message.toXML()=\n" + message.toXML());
		logger.debug(">> message.toPrettyXML()=\n" + message.toPrettyXML());
		
		logger.debug(">> message.setBizData() to TESTSET-DATA");
		message.setBizData("TESTSET-DATA");
		logger.debug(">> message.toByteArray()=\n[" + new String(message.toByteArray()) +"]");
		// Test getter after parsing
		logger.debug("\n>> ----------------------------------------------------------------------------------");
		String findItem = null;
		logger.debug(">> Test getter after parsing");
		findItem = "Array[0].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[1].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[2].Group[0].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));		
		findItem = "Array[2].Group[1].gitem1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		logger.debug(">> Test getter after parsing");
		findItem = "Array[0].Group[0]";
		StandardItem item = message.findItem(findItem);
		if(item == null) {
			logger.debug(findItem + " NOT FOUND!");
		}
		else {
			logger.debug("toCSVString=\n" + item.toCSVString());
			logger.debug("toString=\n" + item.toString());
			logger.debug("toJson=\n" + item.toJson());
			logger.debug("toXML=\n" + item.toXML());
			logger.debug("toByteArray=\n[" + new String(item.toByteArray()) + "]" );
		}
	}
	
	public static void testSimple() {
		int level = 0;
		StandardMessage message = new StandardMessage();
		StandardItem group = null;
		StandardItem item = null;
		level++;
		group = new StandardItem("Header", level, 2, 0, 0, null);
		level++;
		item = new StandardItem("StndCicsTrncd", level, 1, 10, 0, "JI6H");
		group.addItem(item);
		item = new StandardItem("StndIntnlStndTelgmLen", level, 1, 10, 1, "726");
		group.addItem(item);
		item = new StandardItem("StndTranBaseYmd", level, 1, 10, 0, "20220905");
		group.addItem(item);
		message.addItem(group);
		
		level--;
		StandardItem group1 = new StandardItem("Common", level, 2, 0, 0, null);
		level++;
		group = new StandardItem("TranInfo", level, 2, 0, 0, null);
		level++;
		item = new StandardItem("StndCicsTrncd", level, 1, 10, 0, "KB0");
		group.addItem(item);
		item = new StandardItem("StndTelgmRecvTranCd", level, 1, 10, 0, "EDU0100101");
		group.addItem(item);
		item = new StandardItem("StndPrcssRtdTranCd", level, 1, 10, 0, "JI6HWSE02002");
		group.addItem(item);
		group1.addItem(group);
		message.addItem(group1);
		
		logger.debug(">> toString");
		logger.debug(message.toString());
		logger.debug(">> toCSVString");
		logger.debug(message.toCSVString());
		logger.debug(">> toJson");
		logger.debug(message.toJson());
		
		String findItem = "StndCicsTrncd";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Header.StndCicsTrncd";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd1";
		logger.debug(findItem + " => " +message.findItemValue(findItem));
	}
	
	public static void splitTest(String path, String pathSeparator) {
		String[] paths = null;
		logger.debug("path=" + path);
		logger.debug("pathSeparator=" + pathSeparator);
		
		paths = path.split("[" + pathSeparator +"]");
		int index =0;
		for(String p: paths) {
			System.out.printf("path[%d]=(%s)\n", index++, p);
		}
	}
	
	public static void testFlatReader( int caseNumber) {
		String testString = null; 
		
		if( caseNumber == 1) {
			testString ="000000000100000000020000000003KB100000000210000000022000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313";
			try {
				testDynamicReader("FLAT", testString);
			}
			catch(Exception ex) {
				logger.debug("success test : " + ex.toString());
			}
		}		
		// underflow
		else if( caseNumber == 2) {
			testString ="000000000100000000020000000003KB1000000002100000000220000000111000000011200000001130000000211000000021200000002130000000311000000031";
			try {
				testDynamicReader("FLAT", testString);
			}
			catch(Exception ex) {
				logger.debug("underflow test : " + ex.toString());
			}
		}
		// with bizData
		else if( caseNumber == 3) {
			testString ="000000000100000000020000000003KB100000000210000000022000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313ADDED@@";
			try {
				testDynamicReader("FLAT", testString);
			}
			catch(Exception ex) {
				logger.debug("overflow test : " + ex.toString());
			}
		}
		// skip common
		else if( caseNumber == 4) {
			testString ="000000000Z00000000020000000003000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313ADDED";
			try {
				testDynamicReader("FLAT", testString);
			}
			catch(Exception ex) {
				logger.debug("overflow test : " + ex.toString());
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
//		testSimple();

//		testJsonReader();
//		testXmlReader();
		testFlatReader(3);
//		testFlatReader(4);
		
//		testDynamicReader("JSON", jsonString);
//		testDynamicReader("XML", xmlString);
		
		// Json Case
//		splitTest("Array[0].Group[0].gitem2", ".");
		// Xml Case
//		splitTest("/Root/Array[2]/Group/gitem2", "/");
		
	}

}
