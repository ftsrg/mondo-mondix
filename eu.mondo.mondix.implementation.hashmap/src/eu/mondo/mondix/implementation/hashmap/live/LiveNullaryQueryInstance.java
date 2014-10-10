package eu.mondo.mondix.implementation.hashmap.live;

import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.INullaryQueryInstance;
import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.MondixNullaryQueryInstance;

public class LiveNullaryQueryInstance<Row extends AbstractRow> extends LiveQueryInstance<Row> implements INullaryQueryInstance {

	public LiveNullaryQueryInstance(ChangeAwareMondixRelation<Row> changeAwaremondixRelation, Set<Row> rows) {
		super(changeAwaremondixRelation, rows);
	}

	public LiveNullaryQueryInstance(ChangeAwareMondixRelation<Row> changeAwaremondixRelation, Set<Row> rows, 
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(changeAwaremondixRelation, rows, selectedColumnNames, filter);
	}
	
	/**
	 * Reduce implementation to the basic Mondix case.
	 */
	@Override
	public boolean isTrue() {
		return MondixNullaryQueryInstance.isTrue(tuples);
	}

}
