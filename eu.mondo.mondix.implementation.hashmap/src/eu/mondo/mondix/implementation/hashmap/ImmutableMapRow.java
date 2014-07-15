package eu.mondo.mondix.implementation.hashmap;

import com.google.common.collect.ImmutableMap;

/**
 * Row using ImmutableMap as data, implementing the AbstractRow interface for the generic Mondix implementation.
 *
 */
public class ImmutableMapRow implements AbstractRow {
	
	/**
	 * Row as a Guava ImmutableMap
	 */
	ImmutableMap<String, ? extends Object> row;
	
	public ImmutableMapRow(ImmutableMap<String, ? extends Object> row) {
		this.row = row;
	}
	
	@Override
	public Object getValue(String key) {
		return row.get(key);
	}
	
	/**
	 * Two ImmutableMapRows hashCode are equal, if the ImmutableMaps are equal.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((row == null) ? 0 : row.hashCode());
		return result;
	}
	
	/**
	 * Two ImmutableMapRows are equal, iff the ImmutableMaps are equal.
	 */
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
