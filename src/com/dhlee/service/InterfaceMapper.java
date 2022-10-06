package com.dhlee.service;

import java.util.HashMap;

import com.dhlee.message.StandardMessage;

public interface InterfaceMapper {
	String TRAN_CODE       = "TRAN_CODE";
	
	public void initPathMap(HashMap<String, String> map);
	//------------------------------------------------------------
	// ǥ�������� Mapping �Ǵ� �׸��� ��� �����ؾ� ��.
	//------------------------------------------------------------
	public String getEaiSvcCode(StandardMessage standardMessage);
	
	public String getTranCode(StandardMessage standardMessage);
	public void setTranCode(StandardMessage standardMessage, String tranCode);
}
