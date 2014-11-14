package eu.mondo.mondix.implementation.hashmap.live;

import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.INullaryView;
import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.MondixNullaryView;

public class LiveNullaryView<Row extends AbstractRow> extends LiveView<Row> implements INullaryView {

	public LiveNullaryView(ChangeAwareMondixRelation<Row> changeAwaremondixRelation, Set<Row> rows) {
		super(changeAwaremondixRelation, rows);
	}

	public LiveNullaryView(ChangeAwareMondixRelation<Row> changeAwaremondixRelation, Set<Row> rows, 
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(changeAwaremondixRelation, rows, selectedColumnNames, filter);
	}
	
	/**
	 * Reduce implementation to the basic Mondix case.
	 */
	@Override
	public boolean isTrue() {
		return MondixNullaryView.isTrue(tuples);
	}

}
