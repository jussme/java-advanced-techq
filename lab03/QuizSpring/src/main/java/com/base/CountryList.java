package com.base;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CountryList {
	private final static Pattern CODE_PATTERN = Pattern.compile("iso_alpha\\d:(\\w+)/");
	private List<String> codes = new LinkedList<>();
	private List<String> countries = new LinkedList<>();
	
	@JsonProperty("_links")
	private void unpackTop(Map<String, Object> _links) {
		List<Map<String, Object>> array = (List<Map<String, Object>>) _links.get("country:items");
		for(Map<String, Object> map : array) {
			var matcher = CODE_PATTERN.matcher((String) map.get("href"));
			matcher.find();
			var code = matcher.group(1);
			countries.add((String) map.get("name"));
			codes.add(code);
		}
	}
	
	public List<String> getCountries() {
		return countries;
	}
	
	public List<String> getCodes() {
		return codes;
	}
}
