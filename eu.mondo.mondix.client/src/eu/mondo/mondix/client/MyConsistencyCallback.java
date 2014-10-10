package eu.mondo.mondix.client;

import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IConsistencyCallback;

public class MyConsistencyCallback implements IConsistencyCallback {

	int numberOfCallbacks = 0;
	
	@Override
	public void consistentNow(IChangeAwareMondixInstance mondixInstance) {
		numberOfCallbacks++;
	}
	
	public int getNumberOfCallbacks() {
		return numberOfCallbacks;
	}

}
