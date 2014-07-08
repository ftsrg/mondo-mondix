package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

public class ImmutableMapRow implements AbstractRow {

	ImmutableMap<String, ? extends Object> row;
	
	public ImmutableMapRow(ImmutableMap<String, ? extends Object> row) {
		this.row = row;
	}
	
	@Override
	public Object getValue(String key) {
		return row.get(key);
	}
	
	@Override
	public ArrayList<String> getColumns() {
		return new ArrayList<String>(row.keySet());
	}
}
