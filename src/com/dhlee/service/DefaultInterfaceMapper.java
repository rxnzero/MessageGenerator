package com.dhlee.service;

import java.util.HashMap;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;

public class DefaultInterfaceMapper implements InterfaceMapper {
	HashMap<String, String> itemPathMap = new HashMap<String, String>();
	
	public void initPathMap(HashMap<String, String> map) {
		itemPathMap = map;
	}
	
	private String getPath(String key) {
		return itemPathMap.get(key);
	}
	
	
	public DefaultInterfaceMapper() {

	}

	private String getItemValue(StandardMessage standardMessage, String path) {
		if(standardMessage == null) return null;
		return standardMessage.findItemValue(path);		
	}

	private void setItemValue(StandardMessage standardMessage, String path, String value) {
		if(standardMessage == null) {
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
		String cicsTranCd = getTranCode(standardMessage);
		// TODO : site에 맞게 조합해야 함.
		eaiSvcCode = cicsTranCd +"S1";
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

	//------------------------------------------------------------
	
	
}
