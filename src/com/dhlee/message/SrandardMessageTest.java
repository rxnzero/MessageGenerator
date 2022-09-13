package com.dhlee.message;

import java.util.ArrayList;

public class SrandardMessageTest {

	public SrandardMessageTest() {

	}

	public static void testGenerate() {
		
		// Test generator from List definition (with default values)
		System.out.println("\n>> ----------------------------------------------------------------------------------");
		System.out.println(">> Test generator from List definition ( with default values)");
		ArrayList<StandardItem> list = new ArrayList<StandardItem>();
		StandardItem group = null;
		StandardItem item = null;
		group = new StandardItem("Header", 1, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group);
		item = new StandardItem("StndCicsTrncd", 2, StandardType.FIELD.getValue(), 10, 0, "JI6H");
		list.add(item);
		item = new StandardItem("StndIntnlStndTelgmLen", 2, StandardType.FIELD.getValue(), 10, 1, "726");
		list.add(item);
		item = new StandardItem("StndTranBaseYmd", 2, StandardType.FIELD.getValue(), 10, 0, "20220905");
		list.add(item);

		StandardItem group1 = new StandardItem("Common", 1, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group1);
		group = new StandardItem("TranInfo", 2, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group);
		item = new StandardItem("StndCicsTrncd", 3, StandardType.FIELD.getValue(), 10, 0, "KB0");
		list.add(item);
		item = new StandardItem("StndTelgmRecvTranCd", 3, StandardType.FIELD.getValue(), 10, 0, "EDU0100101");
		list.add(item);
		item = new StandardItem("StndPrcssRtdTranCd", 3, StandardType.FIELD.getValue(), 10, 0, "JI6HWSE02002");
		list.add(item);
		
		group = new StandardItem("Array", 1, StandardType.ARRAY.getValue(), 5, 0, null);
		list.add(group);
		item = new StandardItem("Item1", 2, StandardType.FIELD.getValue(), 10, 0, "JI6H");
		list.add(item);
		item = new StandardItem("Group", 2, StandardType.ARRAY.getValue(), 0, 0, null);
		list.add(item);
		item = new StandardItem("gitem1", 3, StandardType.FIELD.getValue(), 10, 1, "726");
		list.add(item);
		item = new StandardItem("gitem2", 3, StandardType.FIELD.getValue(), 10, 0, "20220905");
		list.add(item);

		StandardMessage message = StandardMessageUtil.generate(list);
		// TODO
		// XML -> List -> gen
		// JSON -> List -> gen
		
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
		
		System.out.println(">> Test Json Parsing");
		String jsonString = "{" 
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
		System.out.println("jsonString = " +jsonString);
		
		// Test Json parsing
		StandardReader jsonReader = new JsonReader();
		jsonReader.parse(message, jsonString);
		
		System.out.println(">> message=\n" + message);
		System.out.println(">> message.toJson()=\n" + message.toJson());
		
		// Test getter after parsing
		System.out.println("\n>> ----------------------------------------------------------------------------------");
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
	public static void main(String[] args) {
//		testSimple();
		testGenerate();
	}

}
