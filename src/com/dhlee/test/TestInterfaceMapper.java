package com.dhlee.test;

public class TestInterfaceMapper extends DefaultInterfaceMapper {
	private String tranCodePath = "Common.TranInfo.StndCicsTrncd";
	
	public TestInterfaceMapper() {
	
	}

	public String getTranCodePath() {
		return tranCodePath;
	}

	public void setTranCodePath(String tranCodePath) {
		this.tranCodePath = tranCodePath;
	}
}
