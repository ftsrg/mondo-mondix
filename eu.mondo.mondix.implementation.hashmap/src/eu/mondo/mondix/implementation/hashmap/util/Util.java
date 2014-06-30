package eu.mondo.mondix.implementation.hashmap.util;

import java.util.List;

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

}
