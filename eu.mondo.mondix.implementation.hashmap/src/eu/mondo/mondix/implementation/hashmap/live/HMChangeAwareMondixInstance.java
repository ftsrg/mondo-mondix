package eu.mondo.mondix.implementation.hashmap.live;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.mondo.mondix.implementation.hashmap.HMMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.IConsistencyCallback;

public class HMChangeAwareMondixInstance extends HMMondixInstance implements IChangeAwareMondixInstance {

	Set<IConsistencyCallback> consistencyCallbacks;
	HMChangeAwareMondixRelation changeAwareMondixRelation;
	
	public HMChangeAwareMondixInstance(HashMap<String, Integer> data) {
		super(data);
		consistencyCallbacks = new HashSet<IConsistencyCallback>();
		changeAwareMondixRelation = null;
	}
	
	public IChangeAwareMondixRelation<List<Object>> getHMChangeAwareMondixRelation() {
		if (changeAwareMondixRelation == null)
			changeAwareMondixRelation = new HMChangeAwareMondixRelation(this, data);
		return changeAwareMondixRelation;
	}
	
	

	@Override
	public boolean isConsistent() {
		return true;
	}

	@Override
	public void addChangeListener(IConsistencyCallback consistencyListener) {
		consistencyCallbacks.add(consistencyListener);
	}

	@Override
	public void removeChangeListener(IConsistencyCallback consistencyListener) {
		consistencyCallbacks.remove(consistencyListener);
	}

	public void put(String key, Integer value) {
		changeAwareMondixRelation.put(key, value);
		notifyConsistencyCallbacks();
	}

	public void remove(String key) {
		changeAwareMondixRelation.remove(key);
		notifyConsistencyCallbacks();
	}
	
	private void notifyConsistencyCallbacks() {
		for(IConsistencyCallback consistencyCallback : consistencyCallbacks) {
			consistencyCallback.consistentNow(this);
		}
	}

}
