package com.nofatclips.androidtesting.stats;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class StatsReport {

	public final static String NEW_LINE = System.getProperty("line.separator");
	public final static String BREAK = NEW_LINE + NEW_LINE;
	public final static String TAB = "\t";

	public abstract String getReport();
	
	public String toString () {
		return getReport();
	}
	
	public static int max (int a, Integer b) {
		if (b == null) return a;
		return Math.max(a, b);
	}
	
	public static int sum (Map<String,Integer> m) {
		return sum (m.values());
	}
	
	public static int sum (Collection<Integer> c) {
		int sum = 0;
		for (Integer i: c) {
			sum+=i;
		}
		return sum;
	}
	
	public static void inc (Map<String,Integer> table, String key) {
		if (table.containsKey(key)) {
			table.put(key, table.get(key)+1);
		} else {
			table.put(key, 1);
		}
	}
	
	public static String expandMap (Map<String,Integer> map) {
		StringBuilder s = new StringBuilder();
		for (Map.Entry<String,Integer> e:map.entrySet()) {
			s.append(TAB + TAB + e.getKey() + ": " + e.getValue() + NEW_LINE);
		}
		return s.toString();
	}

	public static String expandList (List<String> list) {
		StringBuilder s = new StringBuilder();
		String separator = "";
		for (String voice: list) {
			s.append(separator + voice);
			separator = ", ";
		}
		return s.toString();		
	}
	
}
