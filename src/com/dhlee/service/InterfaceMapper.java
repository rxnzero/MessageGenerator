package com.dhlee.service;

import java.util.HashMap;

import com.dhlee.message.StandardMessage;

public interface InterfaceMapper {
	String TRAN_CODE       = "TRAN_CODE";
	String RECV_TRAN_CD       = "RECV_TRAN_CD";
	public void initPathMap(HashMap<String, String> map);
	//------------------------------------------------------------
	// 표준전문과 Mapping 되는 항목을 모두 정의해야 함.
	//------------------------------------------------------------
	public String getEaiSvcCode(StandardMessage standardMessage);
	
	public String getTranCode(StandardMessage standardMessage);
	public void setTranCode(StandardMessage standardMessage, String tranCode);
	
	public String getRecvTranCode(StandardMessage standardMessage);
	public void setRecvTranCode(StandardMessage standardMessage, String recvTranCode);
	
}
