package eu.mondo.mondix.implementation.hashmap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.INullaryView;

public class MondixNullaryView<Row extends AbstractRow> extends
		MondixView<Row> implements INullaryView {

	public MondixNullaryView(IMondixRelation mondixRelation, Set<Row> rows) {
		super(mondixRelation, rows);
	}

	public MondixNullaryView(IMondixRelation mondixRelation, Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(mondixRelation, rows, selectedColumnNames, filter);
	}

	@Override
	public boolean isTrue() {
		return isTrue(tuples);
	}

	public static boolean isTrue(HashSet<List<Object>> tuples) {
		return tuples.size() > 0;
	}

}
