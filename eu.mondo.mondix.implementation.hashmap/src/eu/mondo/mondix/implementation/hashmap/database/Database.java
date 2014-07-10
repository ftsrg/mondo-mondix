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

/**
 * Database containing HashMap-based relations.
 *
 */
public class Database {
	/**
	 * Relations identified by string, row values identified by string as attribute.
	 */
	protected Map<String, Set<ImmutableMap<String, Object>>> databaseRelations;
	
	/**
	 * Relations, required by the generic Mondix implementation.
	 */
	protected Map<String, Set<ImmutableMapRow>> mondixRelations;
	
	/**
	 * Store change-aware Mondix instance to propagate changes.
	 */
	protected ChangeAwareMondixInstance<ImmutableMapRow> changeAwareMondixInstance;
	
	/**
	 * Create a database containing HashMap-based relations, where rows are
	 * immutable maps: keys are the attributes (String), and values are data (Object).
	 */
	public Database() {
		databaseRelations = new HashMap<String, Set<ImmutableMap<String, Object>>>();
		mondixRelations = new HashMap<String, Set<ImmutableMapRow>>();
		changeAwareMondixInstance = null;
	}
	
	/**
	 * Add a relation to the database.
	 * @param name identifies the relation
	 */
	public void addRelation(String name, HashSet<ImmutableMap<String, Object>> ages) {
		// Handle database operations.
		databaseRelations.put(name, ages);
		
		// Synchronize Mondix data structures.
		HashSet<ImmutableMapRow> agesMondix = new HashSet<ImmutableMapRow>();
		for(ImmutableMap<String, Object> age : ages) {
			agesMondix.add(new ImmutableMapRow(age));
		}
		mondixRelations.put(name, agesMondix);
		if (changeAwareMondixInstance != null)
			changeAwareMondixInstance.addRelation(name, agesMondix);
	}
	
	/**
	 * Remove a relation from the database.
	 * @param name identifies the relation
	 */
	public void removeRelation(String name) {
		// Handle database operations.
		databaseRelations.remove(name);
		
		// Synchronize Mondix data structures.
		mondixRelations.remove(name);
		if (changeAwareMondixInstance != null)
			changeAwareMondixInstance.removeRelation(name);
	}
	
	/**
	 * Add a row to a relation.
	 * @param relationName name of the relation
	 * @param row row to be added
	 */
	public void addRow(String relationName, ImmutableMap<String, Object> row) {
		// Handle database operations.
		Set<ImmutableMap<String, Object>> relation = databaseRelations.get(relationName);
		if (relation != null) {
			relation.add(row);
		}
		
		// Synchronize Mondix data structures.
		if (relation != null) {
			Set<ImmutableMapRow> mondixRelation = mondixRelations.get(relationName);
			if (mondixRelation != null) {
				ImmutableMapRow mondixRow = new ImmutableMapRow(row);
				mondixRelation.add(mondixRow);
				if (changeAwareMondixInstance != null)
					changeAwareMondixInstance.addRow(relationName, mondixRow);
			}
		}
	}

	/**
	 * Remove a row from a relation, and notifies the change-aware 
	 * Mondix instance if exists one.
	 * 
	 * @param relationName name of the relation
	 * @param row row to be removed
	 */
	public void removeRow(String relationName, ImmutableMap<String, Object> row) {
		// Handle database operations.
		Set<ImmutableMap<String, Object>> relation = databaseRelations.get(relationName);
		if (relation != null) {
			relation.remove(row);
		}
		
		// Synchronize Mondix data structures.
		if (relation != null) {
			Set<ImmutableMapRow> mondixRelation = mondixRelations.get(relationName);
			if (mondixRelation != null) {
				ImmutableMapRow mondixRow = new ImmutableMapRow(row);
				
				if (changeAwareMondixInstance != null)
					changeAwareMondixInstance.removeRow(relationName, mondixRow);
			}
		}
	}
	
	/**
	 * Creates a non change-aware Mondix instance for the actual data.
	 * @return a new instance of a Mondix relation.
	 * @throws Exception
	 */
	public IMondixInstance getMondixInstance() throws Exception {
		MondixInstance<ImmutableMapRow> mondixInstance = new MondixInstance<ImmutableMapRow>(mondixRelations);
		return mondixInstance;
	}
	
	/**
	 * Creates a change-aware Mondix instance for the actual data.
	 * 
	 * @return a singleton instance of a change-aware mondix implementation.
	 * @throws Exception
	 */
	public ChangeAwareMondixInstance<ImmutableMapRow> getChangeAwareMondixInstance() throws Exception {
		if (changeAwareMondixInstance == null) {
			changeAwareMondixInstance = new ChangeAwareMondixInstance<ImmutableMapRow>(mondixRelations);
		}
		return changeAwareMondixInstance;
	}
	
}
