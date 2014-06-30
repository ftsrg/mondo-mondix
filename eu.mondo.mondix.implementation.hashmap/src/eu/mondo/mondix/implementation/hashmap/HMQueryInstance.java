package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;

/**
 * HashMap QueryInstance implementation for the Mondix interface
 *
 */
public class HMQueryInstance implements IQueryInstance<List<Object>> {

	protected IMondixRelation<List<Object>> baseRelation;
	protected Set<List<Object>> tupleSet;
	protected Set<List<Object>> tupleSetFreeParameters;
	protected List<Object> nullTuple;
	
	public HMQueryInstance(IMondixRelation<List<Object>> baseRelation, Set<List<Object>> data) {
		this.baseRelation = baseRelation;
		this.tupleSet = new HashSet<List<Object>>();
		this.tupleSetFreeParameters = tupleSet;
		for(List<Object> t : data) {
			tupleSet.add(new ArrayList<Object>(t));
		}
		
		// initialize null seed to reflect arity
		nullTuple = createNullTuple(baseRelation.getArity());
	}
	
	public HMQueryInstance(HMMondixRelation baseRelation, Set<List<Object>> data, List<Object> seedTuple) {
		this.baseRelation = baseRelation;
		this.nullTuple = seedTuple;
	
		// filter data based on seed and prepare tuples containing only the free parameters
		tupleSet = new HashSet<List<Object>>();
		tupleSetFreeParameters = new HashSet<List<Object>>();
		for(List<Object> tuple : data) {
			List<Object> tupleFreeParameters = createTupleFreeParameters(tuple, seedTuple);
			if (tupleFreeParameters != null) {
				tupleSet.add(tuple);
				tupleSetFreeParameters.add(tupleFreeParameters);
			}
		}
	}

	@Override
	public IMondixRelation<List<Object>> getBaseRelation() {
		return baseRelation;
	}

	@Override
	public List<Object> getSeedTuple() {
		return nullTuple;
	}

	@Override
	public void dispose() {
	}

	@Override
	public int getCountOfTuples() {
		return tupleSet.size();
	}

	@Override
	public Iterable<? extends List<Object>> getAllTuples() {
		return new ArrayList<List<Object>>(tupleSet);
	}

	@Override
	public Iterable<? extends List<Object>> getAllValuesOfFreeParameters() {
		return new ArrayList<List<Object>>(tupleSetFreeParameters);
	}
	
	/**
	 * Create an n-tuple containing only null values.
	 * @param n
	 * @return
	 */
	protected List<Object> createNullTuple(int n) {
		nullTuple = new ArrayList<Object>();
		for(int i = 0; i < n; i++)
			nullTuple.add(null);
		return nullTuple;
	}
	
	/**
	 * 
	 * @param tuple
	 * @param seedTuple 
	 * @return null when tuple does not match with seedTuple, otherwise the tuple containing only the free variables (where the seed contained null) is returned.
	 */
	protected List<Object> createTupleFreeParameters(List<Object> tuple, List<Object> seedTuple) {
		List<Object> tupleFreeParameters = new ArrayList<Object>();
		for(int i = 0; i < seedTuple.size(); i++) {
			Object seedVar = seedTuple.get(i);
			Object tupleVar = tuple.get(i);
			if ((seedVar != null) && (! seedVar.equals(tupleVar))) {
				return null;
			} else if (seedVar == null) {
				tupleFreeParameters.add(tupleVar);
			}
		}
		return tupleFreeParameters;
	}

}
