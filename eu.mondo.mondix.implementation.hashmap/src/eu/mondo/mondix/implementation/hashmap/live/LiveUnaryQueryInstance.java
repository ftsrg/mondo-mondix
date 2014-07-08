package eu.mondo.mondix.implementation.hashmap.live;

import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IUnaryQueryInstance;
import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.MondixUnaryQueryInstance;

public class LiveUnaryQueryInstance<Row extends AbstractRow> extends LiveQueryInstance<Row> implements
		IUnaryQueryInstance {

	public LiveUnaryQueryInstance(ChangeAwareMondixRelation<Row> changeAwaremondixRelation,	Set<Row> rows) {
		super(changeAwaremondixRelation, rows);
	}

	public LiveUnaryQueryInstance(ChangeAwareMondixRelation<Row> changeAwaremondixRelation,	Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(changeAwaremondixRelation, rows, selectedColumnNames, filter);
	}

	@Override
	public Iterable<? extends Object> getValues() {
		return MondixUnaryQueryInstance.getValues(tuples);
	}

}
