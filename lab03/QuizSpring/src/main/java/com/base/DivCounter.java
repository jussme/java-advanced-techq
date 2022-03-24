package com.base;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DivCounter {
	private int divCount;
	
	@JsonProperty("_links")
	private void unpackTop(Map<String, Object> _links) {
		List<Map<String, Object>> array = (List<Map<String, Object>>) _links.get("a1:items");
		divCount = array.size();
	}
	
	public int getDivCount() {
		return divCount;
	}
}
