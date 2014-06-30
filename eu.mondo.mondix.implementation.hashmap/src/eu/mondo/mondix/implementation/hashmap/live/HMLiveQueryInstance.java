package eu.mondo.mondix.implementation.hashmap.live;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.mondo.mondix.implementation.hashmap.HMQueryInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.IChangeCallback;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class HMLiveQueryInstance 
		extends HMQueryInstance
		implements ILiveQueryInstance<List<Object>> {

	protected HMChangeAwareMondixRelation baseRelation;
	protected Set<IChangeCallback<List<Object>>> changeCallbacks;

	public HMLiveQueryInstance(HMChangeAwareMondixRelation baseRelation, Set<List<Object>> data) {
		super(baseRelation, data);
		this.baseRelation = baseRelation;
		changeCallbacks = new HashSet<IChangeCallback<List<Object>>>();
	}

	public HMLiveQueryInstance(HMChangeAwareMondixRelation baseRelation, Set<List<Object>> data, List<Object> seedTuple) {
		super(baseRelation, data, seedTuple);
		this.baseRelation = baseRelation;
		changeCallbacks = new HashSet<IChangeCallback<List<Object>>>();
	}
	
	@Override
	public IChangeAwareMondixRelation<java.util.List<Object>> getBaseRelation() {
		return baseRelation;
	};

	@Override
	public void addChangeListener(IChangeCallback<List<Object>> changeListener) {
		changeCallbacks.add(changeListener);
	}

	@Override
	public void removeChangeListener(
			IChangeCallback<List<Object>> changeListener) {
		changeCallbacks.remove(changeListener);
	}

	public void put(List<Object> tuple) {
		List<Object> tupleFreeParameters = createTupleFreeParameters(tuple, nullTuple);
		if ((! tupleSet.contains(tuple)) && (tupleSetFreeParameters != null)) {
			tupleSet.add(tuple);
			tupleSetFreeParameters.add(tupleFreeParameters);
			callbackChangeListeners(true, tuple);
		}
	}

	public void remove(List<Object> tuple) {
		Set<List<Object>> toRemove = new HashSet<List<Object>>();
		for(List<Object> t : tupleSet) {
			List<Object> tupleFreeParameters = createTupleFreeParameters(t, tuple);
			if (tupleFreeParameters != null) {
				toRemove.add(t);
				callbackChangeListeners(false, t);
			}
		}
		tupleSet.removeAll(toRemove);
	}
	
	private void callbackChangeListeners(boolean inserted, List<Object> changedTuple) {
		for(IChangeCallback<List<Object>> changeCallback : changeCallbacks) {
			changeCallback.changed(this, inserted, changedTuple);
		}
	}
	
	/**
	 * Required to dispose live query instances after use.
	 */
	@Override
	public void dispose() {
		baseRelation.closeQueryInstance(this);
	}
	
	
}
