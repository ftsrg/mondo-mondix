package eu.mondo.mondix.implementation.hashmap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IUnaryView;

// TODO pre-cache getValues() ?
public class MondixUnaryView<Row extends AbstractRow> extends
		MondixView<Row> implements IUnaryView {

	public MondixUnaryView(IMondixRelation mondixRelation,	Set<Row> rows) {
		super(mondixRelation, rows);
	}

	public MondixUnaryView(IMondixRelation mondixRelation, Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(mondixRelation, rows, selectedColumnNames, filter);
	}

	@Override
	public Iterable<? extends Object> getValues() {
		return getValues(tuples);
	}
	
	/**
	 * Unbox unary tuples.
	 * @param tuples input tuple set
	 * @return unboxed objects
	 */
	public static Iterable<? extends Object> getValues(Set<List<Object>> tuples) {
		HashSet<Object> values = new HashSet<Object>();
		for(List<Object> tuple : tuples) {
			Object value = tuple.get(0);
			values.add(value);
		}
		return values;
	}

}
