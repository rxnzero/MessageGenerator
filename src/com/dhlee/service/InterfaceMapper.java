package com.dhlee.service;

import com.dhlee.message.StandardMessage;

public interface InterfaceMapper {
	//------------------------------------------------------------
	// 표준전문과 Mapping 되는 항목을 모두 정의해야 함.
	//------------------------------------------------------------
	public String getEaiSvcCode(StandardMessage standardMessage);
	
	public String getTranCode(StandardMessage standardMessage);
	public void setTranCode(StandardMessage standardMessage, String tranCode);
}
