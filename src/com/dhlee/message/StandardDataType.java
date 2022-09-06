package com.dhlee.message;

public enum StandardDataType {
	STRING(0), NUMBER(1);
	
	private final int value;

	StandardDataType(int value) { this.value = value; }

    public int getValue() { return value; }
}
