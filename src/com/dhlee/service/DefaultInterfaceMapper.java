package com.dhlee.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;
import com.dhlee.test.SrandardMessageTest;

public class DefaultInterfaceMapper implements InterfaceMapper {
	static Logger logger = LoggerFactory.getLogger(DefaultInterfaceMapper.class);

	HashMap<String, String> itemPathMap = new HashMap<String, String>();
	
	public void initPathMap(HashMap<String, String> map) {
		itemPathMap = map;
	}
	
	private String getPath(String key) {
		String path = itemPathMap.get(key);
		if(path == null) {
			logger.warn("message-mapping not defined key = {}", key);
		}
		return path;
	}
	
	
	public DefaultInterfaceMapper() {

	}

	private String getItemValue(StandardMessage standardMessage, String path) {
		if(standardMessage == null) {
			return null;
		}
		if(path == null) {
			return null;
		}
		return standardMessage.findItemValue(path);		
	}

	private void setItemValue(StandardMessage standardMessage, String path, String value) {
		if(standardMessage == null || path == null) {
			return;
		}
		StandardItem item = standardMessage.findItem(path);
		if(item != null) item.setValue(value);		
	}

	//------------------------------------------------------------
	// 표준전문과 Mapping 되는 항목을 모두 정의해야 함.
	//------------------------------------------------------------
	@Override
	public String getEaiSvcCode(StandardMessage standardMessage) {
		String eaiSvcCode = null;
		String recvTranCd = getRecvTranCode(standardMessage);
		String cicsTranCd = getTranCode(standardMessage);
		// TODO : site에 맞게 조합해야 함.
		eaiSvcCode = recvTranCd + cicsTranCd +"S1";
		return eaiSvcCode;
	}
	
	@Override
	public String getTranCode(StandardMessage standardMessage) {
		return getItemValue(standardMessage, getPath(TRAN_CODE));		
	}

	@Override
	public void setTranCode(StandardMessage standardMessage, String tranCode) {
		setItemValue(standardMessage,getPath(TRAN_CODE), tranCode);
	}
	
	@Override
	public String getRecvTranCode(StandardMessage standardMessage) {
		return getItemValue(standardMessage, getPath(RECV_TRAN_CD));		
	}

	@Override
	public void setRecvTranCode(StandardMessage standardMessage, String recvTranCode) {
		setItemValue(standardMessage,getPath(RECV_TRAN_CD), recvTranCode);
	}
	//------------------------------------------------------------
	
	
	
}
