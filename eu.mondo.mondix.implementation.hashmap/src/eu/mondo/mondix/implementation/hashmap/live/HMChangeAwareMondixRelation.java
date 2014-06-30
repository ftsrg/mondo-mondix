package eu.mondo.mondix.implementation.hashmap.live;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.implementation.hashmap.HMMondixRelation;
import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class HMChangeAwareMondixRelation extends HMMondixRelation
	implements IChangeAwareMondixRelation<List<Object>> {

	protected IChangeAwareMondixInstance baseChangeAwareMondixInstance;
	protected Set<HMLiveQueryInstance> liveQueryInstances;
	
	public HMChangeAwareMondixRelation(HMChangeAwareMondixInstance baseChangeAwareMondixInstance, HashMap<String, Integer> data) {
		super(baseChangeAwareMondixInstance, data);
		this.baseChangeAwareMondixInstance = baseChangeAwareMondixInstance;
		liveQueryInstances = new HashSet<HMLiveQueryInstance>();
	}
	
	@Override
	public IMondixInstance getIndexerInstance() {
		return baseChangeAwareMondixInstance;
	}

	@Override
	public ILiveQueryInstance<List<Object>> openQueryInstance() {
		HMLiveQueryInstance liveQueryInstance = new HMLiveQueryInstance(this, tupleSet);
		liveQueryInstances.add(liveQueryInstance);
		return liveQueryInstance;
	}

	@Override
	public ILiveQueryInstance<List<Object>> openSeededQueryInstance(List<Object> seedTuple) {
		HMLiveQueryInstance liveQueryInstance = new HMLiveQueryInstance(this, tupleSet, seedTuple);
		liveQueryInstances.add(liveQueryInstance);
		return liveQueryInstance;
	}
	
	/**
	 * Disable notifications.
	 * @param liveQueryInstance
	 */
	public void closeQueryInstance(ILiveQueryInstance<List<Object>> liveQueryInstance) {
		liveQueryInstances.remove(liveQueryInstance);
	}

	/**
	 * Convert HashMap key,value to List<Object> tuple and update query instance.
	 * @param key
	 * @param value
	 */
	public void put(String key, Integer value) {
		for(HMLiveQueryInstance liveQueryInstance : liveQueryInstances) {
			ArrayList<Object> tuple = new ArrayList<Object>();
			tuple.add(key);
			tuple.add(value);
			liveQueryInstance.put(tuple);
		}
	}

	public void remove(String key) {
		for(HMLiveQueryInstance liveQueryInstance : liveQueryInstances) {
			ArrayList<Object> tuple = new ArrayList<Object>();
			tuple.add(key);
			tuple.add(null);
			liveQueryInstance.remove(tuple);
		}
	}

}
