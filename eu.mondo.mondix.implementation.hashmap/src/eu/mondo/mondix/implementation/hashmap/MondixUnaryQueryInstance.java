package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IUnaryQueryInstance;

public class MondixUnaryQueryInstance<Row extends AbstractRow> extends
		MondixQueryInstance<Row> implements IUnaryQueryInstance {

	public MondixUnaryQueryInstance(IMondixRelation mondixRelation,	Set<Row> rows) {
		super(mondixRelation, rows);
	}

	public MondixUnaryQueryInstance(IMondixRelation mondixRelation, Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(mondixRelation, rows, selectedColumnNames, filter);
	}

	@Override
	public Iterable<? extends Object> getValues() {
		return getValues(tuples);
	}
	
	public static Iterable<? extends Object> getValues(ArrayList<List<Object>> tuples) {
		HashSet<Object> values = new HashSet<Object>();
		for(List<Object> tuple : tuples) {
			Object value = tuple.get(0);
			values.add(value);
		}
		return values;
	}

}
