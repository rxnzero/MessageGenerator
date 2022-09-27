package com.dhlee.service;

import com.dhlee.message.StandardMessage;

public class InterfaceMessage {
	private InterfaceMapper mapper;
	private StandardMessage standardMessage;
	//------------------------------------------------------------
	// Service 정보에서 정의한 고정값들
	//------------------------------------------------------------
	private String interfaceCode;
	private String interfaceType;
	
	public String getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public InterfaceMessage() {		
	}
	
	public InterfaceMapper getMapper() {
		return mapper;
	}

	public void setMapper(InterfaceMapper mapper) {
		this.mapper = mapper;
	}

	public StandardMessage getStandardMessage() {
		return standardMessage;
	}

	public void setStandardMessage(StandardMessage standardMessage) {
		this.standardMessage = standardMessage;
	}
}
