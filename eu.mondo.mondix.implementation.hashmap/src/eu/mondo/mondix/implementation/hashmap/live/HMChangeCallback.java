package eu.mondo.mondix.implementation.hashmap.live;

import java.util.List;

import eu.mondo.mondix.implementation.hashmap.util.Util;
import eu.mondo.mondix.live.IChangeCallback;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class HMChangeCallback implements IChangeCallback<List<Object>> {

	@Override
	public void changed(ILiveQueryInstance<List<Object>> query,
			boolean inserted, List<Object> changedTuple) {
		if (inserted)
			System.out.println("Query("+ query.hashCode() + "), inserted: ");
		else
			System.out.println("Query("+ query.hashCode() + "), removed: ");
		Util.printTuple(changedTuple);
		
	}

}
