package com.dhlee.service;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;

public class InterfaceMessage {
	private InterfaceMapper mapper;
	private StandardMessage standardMessage;
	
	private String interfaceCode;
	private String interfaceType;
	
	//------------------------------------------------------------
	// 표준전문과 Mapping 되는 항목을 모두 정의해야 함.
	//------------------------------------------------------------
	private String tranCode; // StandardMessage mapping field Header.StndCicsTrncd
	
	public String getTranCode() {
		return mapper.getTranCode(standardMessage);
	}

	public void setTranCode(String tranCode) {
		mapper.setTranCode(standardMessage, tranCode);
	}
	//------------------------------------------------------------
	
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

	public static void main(String[] args) {
	
	}
	
}
