package com.dhlee.test;

import com.dhlee.message.StandardMessage;
import com.dhlee.service.DefaultInterfaceMapper;

public class TestInterfaceMapper extends DefaultInterfaceMapper {
	//------------------------------------------------------------
	// �����ǰ� �ʿ��� �׸� ���� ���� �߰�
	//------------------------------------------------------------
	@Override
	public String getEaiSvcCode(StandardMessage standardMessage) {
		String eaiSvcCode = null;
		String cicsTranCd = getTranCode(standardMessage);
		// TODO : site�� �°� �����ؾ� ��.
		eaiSvcCode = cicsTranCd +"S01";
		return eaiSvcCode;
	}	
	//------------------------------------------------------------
	
	public TestInterfaceMapper() {
		
	}
}
