package com.dhlee.message;

public interface StandardReader {
	public StandardMessage parse(StandardMessage message, String data) throws Exception;   
}
