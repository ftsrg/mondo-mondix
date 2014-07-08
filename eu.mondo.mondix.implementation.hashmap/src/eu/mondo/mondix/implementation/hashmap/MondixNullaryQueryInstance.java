package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.INullaryQueryInstance;

public class MondixNullaryQueryInstance<Row extends AbstractRow> extends
		MondixQueryInstance<Row> implements INullaryQueryInstance {

	public MondixNullaryQueryInstance(IMondixRelation mondixRelation, Set<Row> rows) {
		super(mondixRelation, rows);
	}

	public MondixNullaryQueryInstance(IMondixRelation mondixRelation, Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(mondixRelation, rows, selectedColumnNames, filter);
	}

	@Override
	public boolean isTrue() {
		return isTrue(tuples);
	}

	public static boolean isTrue(ArrayList<List<Object>> tuples) {
		return tuples.size() > 0;
	}

}
