package com.dhlee.message.filter;

import java.io.Serializable;

import com.dhlee.message.StandardMessage;

public interface MessageFilter extends Serializable {
	public StandardMessage doFilter(StandardMessage message); 
}
