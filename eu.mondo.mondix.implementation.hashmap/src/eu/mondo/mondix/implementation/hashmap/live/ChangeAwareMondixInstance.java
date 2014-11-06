package eu.mondo.mondix.implementation.hashmap.live;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.ImmutableMapRow;
import eu.mondo.mondix.implementation.hashmap.MondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.IConsistencyCallback;

// TODO add real consistency notifications after bigger transaction units
public class ChangeAwareMondixInstance<Row extends AbstractRow> extends MondixInstance<Row> implements IChangeAwareMondixInstance {
	
	/**
	 * Clients to be notified from the consistency of the whole Mondix instance. 
	 */
	protected HashSet<IConsistencyCallback> consistencyListeners;
	
	/**
	 * Mondix relations within the instance.
	 */
	private Map<String, ChangeAwareMondixRelation<Row>> changeAwareMondixRelations;

	public ChangeAwareMondixInstance(Map<String, Set<Row>> mxRelations, Map<String, List<String>> relationColumnNames) throws Exception {
		super(mxRelations, relationColumnNames);
		this.consistencyListeners = new HashSet<IConsistencyCallback>();
		this.changeAwareMondixRelations = new HashMap<String, ChangeAwareMondixRelation<Row>>();
	}

	@Override
	public boolean isConsistent() {
		return true;
	}

	@Override
	public void addConsistencyListener(IConsistencyCallback consistencyListener) {
		consistencyListeners.add(consistencyListener);
	}

	@Override
	public void removeConsistencyListener(IConsistencyCallback consistencyListener) {
		consistencyListeners.remove(consistencyListener);
	}

	@Override
	public ChangeAwareMondixRelation<? extends AbstractRow> getBaseRelationByName(String relationName) {
		if ("".equals(relationName))
			// return catalog relation
			return catalogRelation;
		else {
			return getNonCatalogBaseRelationByName(relationName);
		}
	}

	protected ChangeAwareMondixRelation<Row> getNonCatalogBaseRelationByName(
			String relationName) {
		// lazy instantiate change-aware relations
		ChangeAwareMondixRelation<Row> changeAwareMondixRelation;
		if (changeAwareMondixRelations.get(relationName) != null) {
			changeAwareMondixRelation = changeAwareMondixRelations.get(relationName);
		} else {
			Set<Row> relation = relations.get(relationName);
			List<String> columns = relationColumnNames.get(relationName);
			changeAwareMondixRelation = new ChangeAwareMondixRelation<Row>(this, relationName, columns, relation);
			changeAwareMondixRelations.put(relationName, changeAwareMondixRelation);
		}
		return changeAwareMondixRelation;
	}

	@Override
	public IChangeAwareMondixRelation getCatalogRelation() {
		return getBaseRelationByName("");
	}
	
	/**
	 * Add a new relation to Mondix
	 * @param relationName name of the relation
	 * @param relation rows in the relation
	 * @param columns name of attributes in an ordered list
	 */
	public void addRelation(String relationName, HashSet<Row> relation, List<String> columns) {
		// sync relation and attribute names
		relations.put(relationName, relation);
		relationColumnNames.put(relationName, columns);
		
		// sync catalog relation
		ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", relationName).build());
		catalogRelation.addRow(row);
	}
	
	/**
	 * Remove a relation from Mondix
	 * @param relationName name of the relation
	 */
	public void removeRelation(String relationName) {
		// sync relation and attribute names
		relations.remove(relationName);
		relationColumnNames.remove(relationName);
		
		// sync catalog relation
		ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", relationName).build());
		catalogRelation.removeRow(row);
	}
	
	/**
	 * Add tuple to a relation.
	 * @param relationName name of the relation
	 * @param row added tuple
	 */
	public void addRow(String relationName, Row row) {
		if ("".equals(relationName))
			throw new IllegalArgumentException("Cannot directly manipulate the catalog relation!");
		
		// sync relation data
		Set<Row> relation = relations.get(relationName);
		relation.add(row);
		
		// notify relation from new data
		getNonCatalogBaseRelationByName(relationName).addRow(row);
		
		// send consistency notifications
		notifyConsistencyListeners();
	}
	
	/**
	 * Remove tuple from a relation
	 * @param relationName name of relation
	 * @param row tuple to be removed
	 */
	public void removeRow(String relationName, Row row) {
		if ("".equals(relationName))
			throw new IllegalArgumentException("Cannot directly manipulate the catalog relation!");
		
		// sync relation data
		Set<Row> relation = relations.get(relationName);
		relation.remove(row);
		
		// notify relation from deleted data
		getNonCatalogBaseRelationByName(relationName).removeRow(row);
		
		// send consistency notifications
		notifyConsistencyListeners();
	}
		
	private void notifyConsistencyListeners() {
		for(IConsistencyCallback consistencyListener : consistencyListeners) {
			consistencyListener.consistentNow(this);
		}
	}

}
