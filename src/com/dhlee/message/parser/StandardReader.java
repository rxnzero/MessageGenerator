package com.dhlee.message.parser;

import com.dhlee.message.StandardMessage;

public interface StandardReader {
	public StandardMessage parse(StandardMessage message, String data) throws Exception;   
}
