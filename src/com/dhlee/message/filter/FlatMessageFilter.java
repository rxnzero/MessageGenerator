package com.dhlee.message.filter;

import com.dhlee.message.StandardMessage;

public class FlatMessageFilter implements MessageFilter {

	@Override
	public StandardMessage doFilter(StandardMessage message) {
		try {
			int totalSize = message.getBytesDataLength();
			message.setLlData(String.valueOf(totalSize));
		} catch (Exception e) {
			;
		}
		return message;
	}
	 
}
