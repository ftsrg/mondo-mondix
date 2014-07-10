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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((row == null) ? 0 : row.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmutableMapRow other = (ImmutableMapRow) obj;
		if (row == null) {
			if (other.row != null)
				return false;
		} else if (!row.equals(other.row))
			return false;
		return true;
	}
	
}
