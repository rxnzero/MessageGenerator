package com.dhlee.test;

import com.dhlee.service.DefaultInterfaceMapper;

public class TestInterfaceMapper extends DefaultInterfaceMapper {
	//------------------------------------------------------------
	// 표준전문과 Mapping 되는 항목을 모두 정의해야 함.
	// DefaultInterfaceMapper 에서 Interface에 필요한 항목을 모두 정의하고
	// 아래에는 실제 Site의 표준전문에 대한 항목의 Full Path를 재정의한다.
	// 항목이 추가되는 경우에는 Interface 와 DefaultInterfaceMapper 를 
	// 수정해야 함.
	//------------------------------------------------------------
	private String tranCodePath = "Common.TranInfo.StndCicsTrncd";
	public String getTranCodePath() {
		return tranCodePath;
	}
	public void setTranCodePath(String tranCodePath) {
		this.tranCodePath = tranCodePath;
	}
	//------------------------------------------------------------
	
	public TestInterfaceMapper() {
		
	}
}
