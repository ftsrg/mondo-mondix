package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;
import eu.mondo.mondix.implementation.hashmap.util.Util;

public class MondixQueryInstance<Row extends AbstractRow> implements IQueryInstance {

	protected IMondixRelation mondixRelation;
	protected Map<String, Object> filter;
	protected List<String> selectedColumnNames;
	protected ArrayList<List<Object>> tuples;

	public MondixQueryInstance(IMondixRelation mondixRelation, Set<Row> rows) {
		this.mondixRelation = mondixRelation;
		selectedColumnNames = mondixRelation.getColumns();
		filter = Util.createNullFilter(selectedColumnNames);
		
		// create result tuples for all columns
		tuples = new ArrayList<List<Object>>();
		for(Row row : rows) {
			ArrayList<Object> tuple = new ArrayList<Object>();
			for(String column : mondixRelation.getColumns()) {
				Object value = row.getValue(column);
				tuple.add(value);
			}
			tuples.add(tuple);
		}

	}
	
	public MondixQueryInstance(IMondixRelation mondixRelation, Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		this.mondixRelation = mondixRelation;
		this.selectedColumnNames = new ArrayList<String>(selectedColumnNames);
		this.filter = new HashMap<String, Object>(filter);
		
		tuples = new ArrayList<List<Object>>();
		for(Row row : rows) {
			// selected columns for matching tuples
			if (isMatching(row, filter)) {
				ArrayList<Object> tuple = createTuple(selectedColumnNames, row);
				tuples.add(tuple);
			}
		}
	}

	protected ArrayList<Object> createTuple(List<String> selectedColumnNames, AbstractRow row) {
		ArrayList<Object> tuple = new ArrayList<Object>();
		for(String selectedColumnName : selectedColumnNames) {
			Object value = row.getValue(selectedColumnName);
			tuple.add(value);
		}
		return tuple;
	}

	protected boolean isMatching(AbstractRow row, Map<String, Object> filter) {
		boolean isMatch = true;
		for(Entry<String, Object> filterEntry : filter.entrySet()) {
			Object value = row.getValue(filterEntry.getKey());
			if ((filterEntry.getValue() != null) && (! filterEntry.getValue().equals(value))) {
				isMatch = false;
				break;
			}
		}

		return isMatch;
	}

	@Override
	public IMondixRelation getBaseRelation() {
		return mondixRelation;
	}

	@Override
	public List<String> getSelectedColumnNames() {
		return selectedColumnNames;
	}

	@Override
	public Map<String, Object> getFilter() {
		return filter;
	}

	@Override
	public void dispose() {
	}

	@Override
	public int getCountOfTuples() {
		return tuples.size();
	}

	@Override
	public Iterable<? extends List<?>> getAllTuples() {
		return tuples;
	}

}
