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
		System.setProperty(StandardMessageManager.STANDARD_MESSAGE_CONFIG, "./resources/standard-message-config-test.properties");
		testInterfaceWithManager();		
	}
	
	public static void testInterfaceWithManager() {
		StandardMessageManager manager = StandardMessageManager.getInstance();
		StandardMessage standardMessage = manager.getStandardMessage();
		InterfaceMapper mapper = manager.getMapper();
		
		InterfaceMessage interfaceMessage = new InterfaceMessage();
		interfaceMessage.setStandardMessage(standardMessage);
		interfaceMessage.setMapper(mapper);
		
		logger.debug("standardMessage = {}",standardMessage.toPrettyJson());
		logger.debug("getTranCode = {}",interfaceMessage.getTranCode());
		
		interfaceMessage.setTranCode("ELK");
		logger.debug("standardMessage = {}",standardMessage.toPrettyJson());
		logger.debug("getTranCode = {}",interfaceMessage.getTranCode());
		
	}

}
