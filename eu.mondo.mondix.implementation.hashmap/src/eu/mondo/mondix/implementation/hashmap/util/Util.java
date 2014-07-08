package eu.mondo.mondix.implementation.hashmap.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

	public static void printTuples(Iterable<? extends List<Object>> ts) {
		for(List<Object> t : ts) {
			printTuple(t);
		}
	}
	
	public static void printTuple(List<Object> t) {
		System.out.println("Tuple:");
		int i = 0;
		for(Object v : t) {
			if (v instanceof String)
				System.out.println("  Value[" + i + "]: " + (String)v);
			else if (v instanceof Integer)
				System.out.println("  Value[" + i + "]: " + (Integer)v);
			i++;
		}
	}
	
	public static Map<String, Object> createNullFilter(List<String> columnNames) {
		HashMap<String, Object> nullFilter = new HashMap<String, Object>();
		for(String columnName : columnNames) {
			nullFilter.put(columnName, null);
		}
		return nullFilter;
	}
}
