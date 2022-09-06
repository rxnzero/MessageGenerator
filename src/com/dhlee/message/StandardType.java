package com.dhlee.message;

public enum StandardType {
	MESSAGE(0), FIELD(1), GROUP(2), ARRAY(3);
	
	private final int value;

	StandardType(int value) { this.value = value; }

    public int getValue() { return value; }
}
