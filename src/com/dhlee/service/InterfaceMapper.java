package com.dhlee.service;

import com.dhlee.message.StandardMessage;

public interface InterfaceMapper {
	//------------------------------------------------------------
	// ǥ�������� Mapping �Ǵ� �׸��� ��� �����ؾ� ��.
	//------------------------------------------------------------
	public String getEaiSvcCode(StandardMessage standardMessage);
	
	public String getTranCode(StandardMessage standardMessage);
	public void setTranCode(StandardMessage standardMessage, String tranCode);
}
