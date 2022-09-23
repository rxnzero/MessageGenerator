package com.dhlee.test;

import com.dhlee.message.StandardItem;
import com.dhlee.message.StandardMessage;

public class DefaultInterfaceMapper implements InterfaceMapper {
	private String tranCodePath = "Header.StndCicsTrncd";
	public DefaultInterfaceMapper() {

	}
	
	public String getTranCodePath() {
		return tranCodePath;
	}

	public void setTranCodePath(String tranCodePath) {
		this.tranCodePath = tranCodePath;
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
	
	@Override
	public String getTranCode(StandardMessage standardMessage) {
		return getItemValue(standardMessage , getTranCodePath());		
	}

	@Override
	public void setTranCode(StandardMessage standardMessage, String tranCode) {
		setItemValue(standardMessage , getTranCodePath(), tranCode);
	}
}
