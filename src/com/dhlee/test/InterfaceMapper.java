package com.dhlee.test;

import com.dhlee.message.StandardMessage;

public interface InterfaceMapper {
	public String getTranCode(StandardMessage standardMessage);
	public void setTranCode(StandardMessage standardMessage, String tranCode);
}
