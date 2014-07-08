package eu.mondo.mondix.implementation.hashmap.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.implementation.hashmap.ImmutableMapRow;
import eu.mondo.mondix.implementation.hashmap.MondixInstance;
import eu.mondo.mondix.implementation.hashmap.live.ChangeAwareMondixInstance;

public class Database {
	/**
	 * Relations identified by string, row values identified by string as attribute.
	 */
	protected Map<String, Set<ImmutableMapRow>> relations;
	
	protected MondixInstance<ImmutableMapRow> mondixInstance;
	protected ChangeAwareMondixInstance<ImmutableMapRow> changeAwaremondixInstance;

	public Database() {
		relations = new HashMap<String, Set<ImmutableMapRow>>();
		mondixInstance = null;
		changeAwaremondixInstance = null;
	}
	
	/**
	 * Add a relation to the database.
	 * @param name identifies the relation
	 */
	public void addRelation(String name, HashSet<ImmutableMapRow> ages) {
		relations.put(name, ages);
	}
	
	/**
	 * Remove a relation from the database.
	 * @param name identifies the relation
	 */
	public void removeRelation(String name) {
		relations.remove(name);
	}
	
	/**
	 * Add a row to a relation.
	 * @param relationName name of the relation
	 * @param row row to be added
	 */
	public void addRow(String relationName, ImmutableMapRow row) {
		Set<ImmutableMapRow> relation = relations.get(relationName);
		if (relation != null) {
			relation.add(row);
		}
	}

	/**
	 * Remove a row from a relation.
	 * @param relationName name of the relation
	 * @param row row to be removed
	 */
	public void removeRow(String relationName, ImmutableMap<String, Object> row) {
		Set<ImmutableMapRow> relation = relations.get(relationName);
		if (relation != null) {
			relation.remove(row);
		}
	}
	

	public IMondixInstance getMondixInstance() throws Exception {
		mondixInstance = new MondixInstance<ImmutableMapRow>(relations);
		return mondixInstance;
	}

	public ChangeAwareMondixInstance<ImmutableMapRow> getChangeAwareMondixInstance() throws Exception {
		if (changeAwaremondixInstance == null) {
			changeAwaremondixInstance = new ChangeAwareMondixInstance<ImmutableMapRow>(relations);
		}
		return changeAwaremondixInstance;
	}
	
}
