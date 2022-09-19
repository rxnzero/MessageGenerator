package com.dhlee.message.layout;

import java.util.ArrayList;

import com.dhlee.message.StandardItem;

public interface LayoutReader {
	public ArrayList<StandardItem> parse(String fileParh) throws Exception;
}
