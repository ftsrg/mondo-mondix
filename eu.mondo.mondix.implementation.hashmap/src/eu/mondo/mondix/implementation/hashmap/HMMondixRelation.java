package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;

/*
 * HashMap -> List<Object> tuple set relation
 */
public class HMMondixRelation implements IMondixRelation<List<Object>> {

	protected Set<List<Object>> tupleSet;
	protected HMMondixInstance hmMondixInstance;
	protected int arity;
	
	public HMMondixRelation(HMMondixInstance hmMondixInstance, HashMap<String, Integer> data) {
		this.hmMondixInstance = hmMondixInstance;
		arity = 2;
		
		// convert HashMap input data to tuple set results
		tupleSet = new HashSet<List<Object>>();
		for(Entry<String, Integer> e : data.entrySet()) {
			ArrayList<Object> t = new ArrayList<Object>();
			t.add(e.getKey());
			t.add(e.getValue());
			tupleSet.add(t);
		}
	}

	@Override
	public IMondixInstance getIndexerInstance() {
		return hmMondixInstance;
	}

	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public IQueryInstance<List<Object>> openQueryInstance() {
		return new HMQueryInstance(this, tupleSet);
	}

	@Override
	public IQueryInstance<List<Object>> openSeededQueryInstance(List<Object> seedTuple) {
		return new HMQueryInstance(this, tupleSet, seedTuple);
	}

}
