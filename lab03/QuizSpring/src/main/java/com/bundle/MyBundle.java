package com.bundle;

import java.util.ListResourceBundle;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MyBundle extends ListResourceBundle{
	protected final Map<String, String> bundle = new TreeMap<>();

	@Override
	protected Object[][] getContents() {
		var tab = new Object[bundle.size()][2];
		//TODO streams?
		int it = 0;
		for(Entry<String, String> entry : bundle.entrySet()) {
			tab[it][0] = entry.getKey();
			tab[it++][1] = entry.getValue();
		}
		return tab;
	}

}
