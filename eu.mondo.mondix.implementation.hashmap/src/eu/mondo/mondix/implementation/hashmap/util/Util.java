package eu.mondo.mondix.implementation.hashmap.util;

import java.util.List;

public class Util {
	/**
	 * Print tuples to stdout.
	 * @param tuples iterable collection of tuples to be printed
	 */
	public static void printTuples(Iterable<? extends List<Object>> tuples) {
		for(List<Object> t : tuples) {
			printTuple(t);
		}
	}
	
	/**
	 * Print one tuple to stdout.
	 * @param tuple to be printed
	 */
	public static void printTuple(List<Object> tuple) {
		System.out.println("Tuple:");
		int i = 0;
		for(Object v : tuple) {
			if (v instanceof String)
				System.out.println("  Value[" + i + "]: " + (String)v);
			else if (v instanceof Integer)
				System.out.println("  Value[" + i + "]: " + (Integer)v);
			i++;
		}
	}
	
}
