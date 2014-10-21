package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;

//TODO generalize construction with Set<Row> -->> Iterable<? extends Row> 
//TODO explain in Javadoc how this constructor parameter may already be prefiltered (e.g. based on another indexer)
//TODO share index contents between query instances
//TODO instead of current Row interface, do something along the lines of RowInterpreter, and/or map filter condition to column indices

public class MondixQueryInstance<Row extends AbstractRow> implements IQueryInstance {
	
	protected IMondixRelation mondixRelation;
	protected Map<String, Object> filter;
	protected List<String> selectedColumnNames;
	protected HashSet<List<Object>> tuples;
	
	public MondixQueryInstance(IMondixRelation mondixRelation, Set<Row> rows) {
		this.mondixRelation = mondixRelation;
		selectedColumnNames = mondixRelation.getColumns();
		filter = new HashMap<String, Object>();
		
		// create result tuples for all columns
		tuples = new HashSet<List<Object>>();
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
		
		tuples = new HashSet<List<Object>>();
		for(Row row : rows) {
			// selected columns for matching tuples
			if (isMatch(row, filter)) {
				ArrayList<Object> tuple = createTuple(row, selectedColumnNames);
				tuples.add(tuple);
			}
		}
	}
	
	/**
	 * Project input tuple.
	 * @param row input tuple
	 * @param selectedColumnNames projected attribute names
	 * @return projected tuple
	 */
	protected ArrayList<Object> createTuple(AbstractRow row, List<String> selectedColumnNames) {
		ArrayList<Object> tuple = new ArrayList<Object>();
		for(String selectedColumnName : selectedColumnNames) {
			Object value = row.getValue(selectedColumnName);
			tuple.add(value);
		}
		return tuple;
	}
	
	/**
	 * Test whether row is matching with the filter.
	 * @param row input tuple
	 * @param filter bound attributes
	 * @return true, iff bound attribute values are the same in the input tuple
	 */
	protected boolean isMatch(AbstractRow row, Map<String, Object> filter) {
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
