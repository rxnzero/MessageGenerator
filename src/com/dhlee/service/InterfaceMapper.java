package com.dhlee.service;

import java.util.HashMap;

import com.dhlee.message.StandardMessage;

public interface InterfaceMapper {
	String TRAN_CODE       = "TRAN_CODE";
	String RECV_TRAN_CD       = "RECV_TRAN_CD";
	public void initPathMap(HashMap<String, String> map);
	//------------------------------------------------------------
	// ǥ�������� Mapping �Ǵ� �׸��� ��� �����ؾ� ��.
	//------------------------------------------------------------
	public String getEaiSvcCode(StandardMessage standardMessage);
	
	public String getTranCode(StandardMessage standardMessage);
	public void setTranCode(StandardMessage standardMessage, String tranCode);
	
	public String getRecvTranCode(StandardMessage standardMessage);
	public void setRecvTranCode(StandardMessage standardMessage, String recvTranCode);
	
}
