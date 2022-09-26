package com.dhlee.service;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;

public class DefaultInterfaceMapper implements InterfaceMapper {
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

	
	private String tranCodePath = "Header.StndCicsTrncd"; // 표준전문에서의 항목 full.path
	
	@Override
	public String getTranCode(StandardMessage standardMessage) {
		return getItemValue(standardMessage , getTranCodePath());		
	}

	@Override
	public void setTranCode(StandardMessage standardMessage, String tranCode) {
		setItemValue(standardMessage , getTranCodePath(), tranCode);
	}
	public String getTranCodePath() {
		return tranCodePath;
	}

	public void setTranCodePath(String tranCodePath) {
		this.tranCodePath = tranCodePath;
	}
	
	

	//------------------------------------------------------------
	
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


	
	
}
