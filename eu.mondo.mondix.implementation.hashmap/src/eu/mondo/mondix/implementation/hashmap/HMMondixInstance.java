package eu.mondo.mondix.implementation.hashmap;

import java.util.HashMap;
import java.util.List;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;

/**
 * HashMap data to List<Object> tuples Mondix relation implementation.
 *
 */
public class HMMondixInstance implements IMondixInstance {

	protected HashMap<String, Integer> data;

	public HMMondixInstance(HashMap<String, Integer> data) {
		this.data = data;
	}

	// List<Object> n-tuple, 2-tuple in this case
	public IMondixRelation<List<Object>> getHMMondixRelation() {
		return new HMMondixRelation(this, data);
	}
	
}
