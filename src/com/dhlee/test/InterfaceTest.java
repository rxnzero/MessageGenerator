package com.dhlee.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardMessage;
import com.dhlee.message.manager.StandardMessageManager;
import com.dhlee.service.InterfaceMapper;
import com.dhlee.service.InterfaceMessage;

public class InterfaceTest {
	static Logger logger = LoggerFactory.getLogger(InterfaceTest.class);

	public InterfaceTest() {
	}

	public static void main(String[] args) throws Exception {
//		System.setProperty(StandardMessageManager.STANDARD_MESSAGE_CONFIG, "./resources/standard-message-config-test.properties");
		System.setProperty(StandardMessageManager.STANDARD_MESSAGE_CONFIG, "./resources/standard-message-config.properties");
		testInterfaceWithManager();		
//		String data = ",,2,,4";
//		String[] sp = data.split(",", -1);
//		
//		System.out.println("DATA [" + data +"]");
//		
//		int index = 0;
//		for(String item : sp) {
//			System.out.println(index++ + " : " + item);
//		}
		
	}
	
	public static void testInterfaceWithManager() {
		StandardMessageManager manager = StandardMessageManager.getInstance();
		StandardMessage standardMessage = manager.getStandardMessage();
		InterfaceMapper mapper = manager.getMapper();
		
		
		InterfaceMessage interfaceMessage = new InterfaceMessage();
		interfaceMessage.setStandardMessage(standardMessage);
		interfaceMessage.setMapper(mapper);
		
		logger.debug(">> Init.");
		logger.debug("getTranCode = {}",interfaceMessage.getMapper().getTranCode(interfaceMessage.getStandardMessage()));
		logger.debug("getEaiSvcCode = {}", interfaceMessage.getMapper().getEaiSvcCode(standardMessage));
		logger.debug("standardMessage = {}",interfaceMessage.getStandardMessage().toPrettyJson());
		
		String changed = "ELK";
		logger.debug("\n>> Change : setTranCode = {}", changed);
		interfaceMessage.getMapper().setTranCode(interfaceMessage.getStandardMessage(), changed);
		logger.debug("getTranCode = {}",mapper.getTranCode(interfaceMessage.getStandardMessage()));
		logger.debug("getEaiSvcCode = {}", interfaceMessage.getMapper().getEaiSvcCode(standardMessage));
		logger.debug("standardMessage = {}",interfaceMessage.getStandardMessage().toPrettyJson());
	}

}
