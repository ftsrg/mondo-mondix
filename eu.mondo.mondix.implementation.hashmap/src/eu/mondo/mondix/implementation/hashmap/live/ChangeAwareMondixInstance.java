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

public class ChangeAwareMondixInstance<Row extends AbstractRow> extends MondixInstance<Row> implements IChangeAwareMondixInstance {

	protected HashSet<IConsistencyCallback> consistencyListeners;
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
			return catalogRelation;
		else {
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
	}

	@Override
	public IChangeAwareMondixRelation getCatalogRelation() {
		return getBaseRelationByName("");
	}
	
	public void addRelation(String relationName, HashSet<Row> relation, List<String> columns) {
		relations.put(relationName, relation);
		relationColumnNames.put(relationName, columns);
		
		ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", relationName).build());
		catalogRelation.addRow(row);
	}
	
	public void removeRelation(String relationName) {
		relations.remove(relationName);
		relationColumnNames.remove(relationName);
		
		ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", relationName).build());
		catalogRelation.removeRow(row);
	}
	
	public void addRow(String relationName, Row row) {
		Set<Row> relation = relations.get(relationName);
		relation.add(row);
		
		changeAwareMondixRelations.get(relationName).addRow(row);
		
		notifyConsistencyListeners();
	}

	public void removeRow(String relationName, Row row) {
		Set<Row> relation = relations.get(relationName);
		relation.remove(row);
		
		changeAwareMondixRelations.get(relationName).removeRow(row);
		
		notifyConsistencyListeners();
	}
		
	private void notifyConsistencyListeners() {
		for(IConsistencyCallback consistencyListener : consistencyListeners) {
			consistencyListener.consistentNow(this);
		}
	}

}
