package com.dhlee.message;

import java.util.ArrayList;

public class SrandardMessageTest {
	static String strString ="000000000100000000020000000003KB100000000210000000022000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313";

	static String jsonString = "{" 
			+"  \"Header\": {" 
			+"    \"StndCicsTrncd\":\"TEST\", " 
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
			+" <gitem1>301</gitem1>"
			+" <gitem2>20220915</gitem2>"
		+"</Group>"
		+"<Group>"
			+" <gitem1>302</gitem1>"
			+" <gitem2>20220915</gitem2>"
		+"</Group>"
		+"</Array>"
		+"<AddOn>"
		+" <StndCicsTrncd>TEST</StndCicsTrncd>"
		+" <StndIntnlStndTelgmLen>100</StndIntnlStndTelgmLen>"
		+" <StndTranBaseYmd>20220913</StndTranBaseYmd>"
		+"</AddOn>"
	+"</Root>";
	
	public SrandardMessageTest() {

	}
	
	public static StandardMessage generateMessage() {
		// Test generator from List definition (with default values)
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		System.out.println(">> Test generator from List definition ( with default values)");
		ArrayList<StandardItem> list = new ArrayList<StandardItem>();
		StandardItem group = null;
		StandardItem item = null;
		group = new StandardItem("Header", 1, StandardType.GROUP.getValue(), 1, 0, 0, null);
		list.add(group);
		item = new StandardItem("StndCicsTrncd", 2, StandardType.FIELD.getValue(), 1, 10, 0, "");
		list.add(item);
		item = new StandardItem("StndIntnlStndTelgmLen", 2, StandardType.FIELD.getValue(), 1, 10, 1, "0");
		list.add(item);
		item = new StandardItem("StndTranBaseYmd", 2, StandardType.FIELD.getValue(), 1, 10, 0, "");
		list.add(item);

		StandardItem group1 = new StandardItem("Common", 1, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group1);
		group = new StandardItem("TranInfo", 2, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group);
		item = new StandardItem("StndCicsTrncd", 3, StandardType.FIELD.getValue(), 3, 0, "KB0");
		list.add(item);
		item = new StandardItem("StndTelgmRecvTranCd", 3, StandardType.FIELD.getValue(), 10, 0, "");
		list.add(item);
		item = new StandardItem("StndPrcssRtdTranCd", 3, StandardType.FIELD.getValue(), 10, 0, "");
		list.add(item);
		
		group = new StandardItem("Array", 1, StandardType.ARRAY.getValue(), 3, 0, null);
		list.add(group);
		item = new StandardItem("Item1", 2, StandardType.FIELD.getValue(), 10, 0, "JI6H");
		list.add(item);
		item = new StandardItem("Group", 2, StandardType.ARRAY.getValue(), 1, 0, null);
		list.add(item);
		item = new StandardItem("gitem1", 3, StandardType.FIELD.getValue(), 10, 1, "726");
		list.add(item);
		item = new StandardItem("gitem2", 3, StandardType.FIELD.getValue(), 10, 0, "20220905");
		list.add(item);

		StandardMessage message = null;;
		try {
			message = StandardMessageUtil.generate(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		// TODO
		// 1. CSV -> List -> gen
		// 2. XML -> List -> gen
		// 3. JSON -> List -> gen
		
		System.out.println("@toString\n" + message.toString());
		System.out.println("@toJson\n" + message.toJson());
		
		// Test initialize getter
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		System.out.println(">> Test initialize getter");
		String findItem = "StndCicsTrncd";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Header.StndCicsTrncd";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		System.out.println(">> Test file item with path & set value");
		StandardItem it = message.findItem(findItem);
		if(it !=null) {
			it.setValue("changed!");
		}
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Array[0].Group.gitem2";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Array[0]";
		System.out.println(findItem + " => " +message.findItem(findItem));
		
		return message;
	}
	
	public static void testJsonReader() throws Exception {
		StandardMessage message = null;;
		message = generateMessage();
		if(message == null) {
			return;
		}
		
		System.out.println(">> Test Json Parsing");
		
		System.out.println("jsonString = " +jsonString);
		
		// Test Json parsing
//		StandardReader jsonReader = new JsonReader();
		
		// Test Dynamic Loading
		StandardReader jsonReader = null;
		String readerName = "com.dhlee.message.JsonReader";
		Class cl = null;
		try {
			cl = Class.forName(readerName);
			jsonReader = (StandardReader)cl.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		jsonReader.parse(message, jsonString);
		
		System.out.println(">> message=\n" + message);
		System.out.println(">> message.toJson()=\n" + message.toJson());
		System.out.println(">> message.toPrettyJson()=\n" + message.toPrettyJson());
		System.out.println(">> message.toXML()=\n" + message.toXML());
		System.out.println(">> message.toPrettyXML()=\n" + message.toPrettyXML());
		// Test getter after parsing
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		String findItem = null;
		System.out.println(">> Test getter after parsing");
		findItem = "Array[0].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[1].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[2].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));		
		findItem = "Array[2].Group[1].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
	}
	
	public static void testXmlReader() throws Exception {
		StandardMessage message = null;;
		message = generateMessage();
		if(message == null) {
			return;
		}
		
		System.out.println(">> Test Json Parsing");
		
		System.out.println("jsonString = " + xmlString);
		
		// Test XML parsing
		StandardReader jsonReader = new XmlReader();
		jsonReader.parse(message, xmlString);
		
		System.out.println(">> message=\n" + message);
		System.out.println(">> message.toJson()=\n" + message.toJson());
		System.out.println(">> message.toXML()=\n" + message.toXML());
		// Test getter after parsing
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		String findItem = null;
		System.out.println(">> Test getter after parsing");
		findItem = "Array[0].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[1].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[2].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));		
		findItem = "Array[2].Group[1].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
	}
	
	
	public static void testDynamicReader(String readerClassName, String messageString) throws Exception {
		StandardMessage message = null;;
		message = generateMessage();
		if(message == null) {
			return;
		}
		
		System.out.println(">> Test Parsing");
		
		System.out.println("messageString = " +messageString);
		
		// Test Json parsing
		// Test Dynamic Loading
		StandardReader reader = null;
		
		Class cl = null;
		try {
			cl = Class.forName(readerClassName);
			reader = (StandardReader)cl.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		reader.parse(message, messageString);
		
		System.out.println(">> message=\n" + message);
		System.out.println(">> message.toJson()=\n" + message.toJson());
		System.out.println(">> message.toPrettyJson()=\n" + message.toPrettyJson());
		System.out.println(">> message.toXML()=\n" + message.toXML());
		System.out.println(">> message.toPrettyXML()=\n" + message.toPrettyXML());
		// Test getter after parsing
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		String findItem = null;
		System.out.println(">> Test getter after parsing");
		findItem = "Array[0].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[1].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		findItem = "Array[2].Group[0].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));		
		findItem = "Array[2].Group[1].gitem1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
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
		
		System.out.println(">> toString");
		System.out.println(message.toString());
		System.out.println(">> toCSVString");
		System.out.println(message.toCSVString());
		System.out.println(">> toJson");
		System.out.println(message.toJson());
		
		String findItem = "StndCicsTrncd";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Header.StndCicsTrncd";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
		
		findItem = "Common.TranInfo.StndCicsTrncd1";
		System.out.println(findItem + " => " +message.findItemValue(findItem));
	}
	
	public static void splitTest(String path, String pathSeparator) {
		String[] paths = null;
		System.out.println("path=" + path);
		System.out.println("pathSeparator=" + pathSeparator);
		
		paths = path.split("[" + pathSeparator +"]");
		int index =0;
		for(String p: paths) {
			System.out.printf("path[%d]=(%s)\n", index++, p);
		}
	}
	public static void main(String[] args) {
//		testSimple();
//		testJsonReader();
//		testDynamicReader("com.dhlee.message.JsonReader", jsonString);
//		testXmlReader();
//		testDynamicReader("com.dhlee.message.XmlReader", xmlString);
		
		String testString ="000000000100000000020000000003KB100000000210000000022000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313";
		try {
			testDynamicReader("com.dhlee.message.FlatReader", testString);
		}
		catch(Exception ex) {
			System.out.println("success test : " + ex.toString());
		}
		// underflow
		testString ="000000000100000000020000000003KB1000000002100000000220000000111000000011200000001130000000211000000021200000002130000000311000000031";
		try {
			testDynamicReader("com.dhlee.message.FlatReader", testString);
		}
		catch(Exception ex) {
			System.out.println("underflow test : " + ex.toString());
		}
		// overflow
		testString ="000000000100000000020000000003KB100000000210000000022000000011100000001120000000113000000021100000002120000000213000000031100000003120000000313ADDED";
		try {
			testDynamicReader("com.dhlee.message.FlatReader", testString);
		}
		catch(Exception ex) {
			System.out.println("overflow test : " + ex.toString());
		}
		
		// Json Case
//		splitTest("Array[0].Group[0].gitem2", ".");
		// Xml Case
//		splitTest("/Root/Array[2]/Group/gitem2", "/");
		
	}

}
