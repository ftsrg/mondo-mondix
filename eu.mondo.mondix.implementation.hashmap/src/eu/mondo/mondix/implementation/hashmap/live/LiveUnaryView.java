package eu.mondo.mondix.implementation.hashmap.live;

import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IUnaryView;
import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.MondixUnaryView;

public class LiveUnaryView<Row extends AbstractRow> extends LiveView<Row> implements
		IUnaryView {

	public LiveUnaryView(ChangeAwareMondixRelation<Row> changeAwaremondixRelation,	Set<Row> rows) {
		super(changeAwaremondixRelation, rows);
	}

	public LiveUnaryView(ChangeAwareMondixRelation<Row> changeAwaremondixRelation,	Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(changeAwaremondixRelation, rows, selectedColumnNames, filter);
	}
	
	@Override
	public Iterable<? extends Object> getValues() {
		return MondixUnaryView.getValues(tuples);
	}

}
