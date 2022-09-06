package com.dhlee.message;

import java.util.ArrayList;

public class SrandardMessageTest {

	public SrandardMessageTest() {

	}

	public static void testGenerate() {
		ArrayList<StandardItem> list = new ArrayList<StandardItem>();
		int level = 0;
		StandardItem group = null;
		StandardItem item = null;
		level++;
		group = new StandardItem("Header", level, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group);
		level++;
		item = new StandardItem("StndCicsTrncd", level, StandardType.FIELD.getValue(), 10, 0, "JI6H");
		list.add(item);
		item = new StandardItem("StndIntnlStndTelgmLen", level, StandardType.FIELD.getValue(), 10, 1, "726");
		list.add(item);
		item = new StandardItem("StndTranBaseYmd", level, StandardType.FIELD.getValue(), 10, 0, "20220905");
		list.add(item);
		
		level--;
		StandardItem group1 = new StandardItem("Common", level, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group1);
		level++;
		group = new StandardItem("TranInfo", level, StandardType.GROUP.getValue(), 0, 0, null);
		list.add(group);
		level++;
		item = new StandardItem("StndCicsTrncd", level, StandardType.FIELD.getValue(), 10, 0, "KB0");
		list.add(item);
		item = new StandardItem("StndTelgmRecvTranCd", level, StandardType.FIELD.getValue(), 10, 0, "EDU0100101");
		list.add(item);
		item = new StandardItem("StndPrcssRtdTranCd", level, StandardType.FIELD.getValue(), 10, 0, "JI6HWSE02002");
		list.add(item);
		StandardMessage message = StandardMessage.generate(list);
		
		System.out.println(message.toString());
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
		
		System.out.println(message.toString());
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
