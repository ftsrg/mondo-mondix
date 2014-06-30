package eu.mondo.mondix.client;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;
import eu.mondo.mondix.implementation.hashmap.Database;
import eu.mondo.mondix.implementation.hashmap.HMMondixInstance;
import eu.mondo.mondix.implementation.hashmap.live.HMChangeAwareMondixInstance;
import eu.mondo.mondix.implementation.hashmap.live.HMChangeCallback;
import eu.mondo.mondix.implementation.hashmap.live.HMConsistencyCallback;
import eu.mondo.mondix.implementation.hashmap.util.Util;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class Main {

	public static void main(String[] args) {
		Database db = new Database();
		
		db.put(new AbstractMap.SimpleEntry<String, Integer>("John", 23));
		db.put(new AbstractMap.SimpleEntry<String, Integer>("Jane", 19));
		db.put(new AbstractMap.SimpleEntry<String, Integer>("Jeff", 23));
		
		HMMondixInstance mondixInstance = db.getMondixInstance();

//		// get keys
//		System.out.println("Keys:");
//		IMondixRelation<String> keyRelation = mondixInstance.getKeyRelation();
//		IQueryInstance<String> keyRelationQueryInstance = keyRelation.openQueryInstance();
//		for(String s : keyRelationQueryInstance.getAllTuples()) {
//			System.out.println(s);
//		}
//		System.out.println("---");
//
//		// get values
//		System.out.println("Values:");
//		IMondixRelation<Integer> valuesRelation = mondixInstance.getValuesRelation();
//		IQueryInstance<Integer> valuesRelationQueryInstance = valuesRelation.openQueryInstance();
//		for(Integer i : valuesRelationQueryInstance.getAllTuples()) {
//			System.out.println(i);
//		}
//		System.out.println("---");

		// get pairs
		System.out.println("Pairs:");
		IMondixRelation<List<Object>> pairsRelation = mondixInstance.getHMMondixRelation();
		IQueryInstance<List<Object>> pairsRelationQueryInstance = pairsRelation.openQueryInstance();
		Util.printTuples(pairsRelationQueryInstance.getAllTuples());
		System.out.println("---");

		// get seeded values for {null,23}
		System.out.println("Pairs with seed {null,23}:");
		ArrayList<Object> seedTuple = new ArrayList<Object>();
		seedTuple.add(null);
		seedTuple.add(new Integer(23));
		IQueryInstance<List<Object>> null23QueryInstance = pairsRelation.openSeededQueryInstance(seedTuple);
		Util.printTuples(null23QueryInstance.getAllTuples());
		System.out.println("---");

		// get seeded values for {Jane,null}
		System.out.println("Pairs with seed {Jane,null}:");
		ArrayList<Object> seedTuple2 = new ArrayList<Object>();
		seedTuple2.add(new String("Jane"));
		seedTuple2.add(null);
		IQueryInstance<List<Object>> janeNullQueryInstance = pairsRelation.openSeededQueryInstance(seedTuple2);
		Util.printTuples(janeNullQueryInstance.getAllTuples());
		System.out.println("---");
		
		

		System.out.println("CHANGE AWARE:");
		
		HMChangeAwareMondixInstance changeAwareMondixInstance = db.getChangeAwareMondixInstance();
		
		// get pairs
		System.out.println("Pairs:");
		IChangeAwareMondixRelation<List<Object>> changeAwareRelation = changeAwareMondixInstance.getHMChangeAwareMondixRelation();
		ILiveQueryInstance<List<Object>> changeAwareQueryInstance = changeAwareRelation.openQueryInstance();
		Util.printTuples(changeAwareQueryInstance.getAllTuples());
		System.out.println("---");
		
		// add entry and test whether duplicates are added
		changeAwareMondixInstance.put("Jenny", 27);
		Util.printTuples(changeAwareQueryInstance.getAllTuples());
		System.out.println("---");
		changeAwareMondixInstance.put("Jenny", 27);
		Util.printTuples(changeAwareQueryInstance.getAllTuples());
		System.out.println("---");
		
		// remove entry and test whether duplicates are added
		changeAwareMondixInstance.remove("Jeff");
		Util.printTuples(changeAwareQueryInstance.getAllTuples());
		System.out.println("---");
		changeAwareMondixInstance.remove("Jeff");
		Util.printTuples(changeAwareQueryInstance.getAllTuples());
		System.out.println("---");
		
		// check for consistency
		System.out.println("Change-aware Mondix instance is consistent: " + changeAwareMondixInstance.isConsistent());
		System.out.println("---");
		
		// test consistency and change callbacks
		HMConsistencyCallback consistencyListener = new HMConsistencyCallback();
		changeAwareMondixInstance.addChangeListener(consistencyListener);
		HMChangeCallback changeListener = new HMChangeCallback();
		changeAwareQueryInstance.addChangeListener(changeListener);
		changeAwareMondixInstance.put("Johanna", 23);
		changeAwareMondixInstance.removeChangeListener(consistencyListener);
		changeAwareQueryInstance.removeChangeListener(changeListener);
		System.out.println("---");
		
		
		System.out.println("CHANGE AWARE, SEEDED:");

		// get pairs
		System.out.println("Pairs:");
		ILiveQueryInstance<List<Object>> changeAwareSeededQueryInstance = changeAwareRelation.openSeededQueryInstance(seedTuple);
		Util.printTuples(changeAwareSeededQueryInstance.getAllTuples());
		System.out.println("---");
		
		// add entry and test whether duplicates are added
		changeAwareMondixInstance.remove("Jenny");
		Util.printTuples(changeAwareSeededQueryInstance.getAllTuples());
		System.out.println("---");
		changeAwareMondixInstance.remove("Jeff");
		Util.printTuples(changeAwareSeededQueryInstance.getAllTuples());
		System.out.println("---");
		
		// remove entry and test whether duplicates are added
		changeAwareMondixInstance.put("Jane", 23);
		Util.printTuples(changeAwareSeededQueryInstance.getAllTuples());
		System.out.println("---");
		changeAwareMondixInstance.put("Jane", 23);
		Util.printTuples(changeAwareSeededQueryInstance.getAllTuples());
		System.out.println("---");
		
		
	}
	
}
