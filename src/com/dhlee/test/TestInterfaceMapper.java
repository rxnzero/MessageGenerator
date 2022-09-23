package com.dhlee.test;

import com.dhlee.service.DefaultInterfaceMapper;

public class TestInterfaceMapper extends DefaultInterfaceMapper {
	//------------------------------------------------------------
	// ǥ�������� Mapping �Ǵ� �׸��� ��� �����ؾ� ��.
	// DefaultInterfaceMapper ���� Interface�� �ʿ��� �׸��� ��� �����ϰ�
	// �Ʒ����� ���� Site�� ǥ�������� ���� �׸��� Full Path�� �������Ѵ�.
	// �׸��� �߰��Ǵ� ��쿡�� Interface �� DefaultInterfaceMapper �� 
	// �����ؾ� ��.
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
