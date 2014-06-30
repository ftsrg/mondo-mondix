package eu.mondo.mondix.implementation.hashmap;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

import eu.mondo.mondix.implementation.hashmap.live.HMChangeAwareMondixInstance;

public class Database {
	HashMap<String, Integer> data;
	HMChangeAwareMondixInstance changeAwareMondixInstance;

	public Database() {
		data = new HashMap<String, Integer>();
		changeAwareMondixInstance = null;
	}
	
	/**
	 * Data manipulation: add entry to the HashMap
	 * @param s
	 */
	public void put(SimpleEntry<String, Integer> s) {
		// If the entry is not in the HashMap, put and notify MondixInstance.
		if ((s.getValue() != null) && (! s.getValue().equals(data.get(s.getKey())))) {
			data.put(s.getKey(), s.getValue());
			if (changeAwareMondixInstance != null)
				changeAwareMondixInstance.put(s.getKey(), s.getValue());
		}
	}

	/**
	 * Data manipulation: remove entry from the HashMap
	 * @param s
	 */
	public void remove(String s) {
		data.remove(s);
		if (changeAwareMondixInstance != null)
			changeAwareMondixInstance.remove(s);
	}

	public HMMondixInstance getMondixInstance() {
		return new HMMondixInstance(data);
	}

	public HMChangeAwareMondixInstance getChangeAwareMondixInstance() {
		if (changeAwareMondixInstance == null) changeAwareMondixInstance = new HMChangeAwareMondixInstance(data);
		return changeAwareMondixInstance;
	}

}
