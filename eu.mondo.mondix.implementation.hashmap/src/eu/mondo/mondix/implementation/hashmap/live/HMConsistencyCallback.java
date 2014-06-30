package eu.mondo.mondix.implementation.hashmap.live;

import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IConsistencyCallback;

public class HMConsistencyCallback implements IConsistencyCallback {

	@Override
	public void consistentNow(IChangeAwareMondixInstance mondixInstance) {
		System.out.println(this.getClass().getSimpleName() + ": MondixInstance(" + mondixInstance.hashCode() + ") became consistent.");
	}

}
