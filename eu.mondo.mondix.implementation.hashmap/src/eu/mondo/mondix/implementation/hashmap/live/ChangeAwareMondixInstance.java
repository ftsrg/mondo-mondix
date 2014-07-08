package eu.mondo.mondix.implementation.hashmap.live;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.ImmutableMapRow;
import eu.mondo.mondix.implementation.hashmap.MondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.IConsistencyCallback;

public class ChangeAwareMondixInstance<Row extends AbstractRow> extends MondixInstance<Row> implements IChangeAwareMondixInstance {

	protected HashSet<IConsistencyCallback> consistencyListeners;
	private Set<ChangeAwareMondixRelation<Row>> changeAwareMondixRelations;

	public ChangeAwareMondixInstance(Map<String, Set<Row>> mxRelations) throws Exception {
		super(mxRelations);
		this.consistencyListeners = new HashSet<IConsistencyCallback>();
		this.changeAwareMondixRelations = new HashSet<ChangeAwareMondixRelation<Row>>();
	}

	@Override
	public boolean isConsistent() {
		return true;
	}

	@Override
	public void addChangeListener(IConsistencyCallback consistencyListener) {
		consistencyListeners.add(consistencyListener);
	}

	@Override
	public void removeChangeListener(IConsistencyCallback consistencyListener) {
		consistencyListeners.remove(consistencyListener);
	}

	@Override
	public ChangeAwareMondixRelation<? extends AbstractRow> getBaseRelationByName(String relationName) {
		if ("".equals(relationName))
			return catalogRelation;
		else {
			Set<Row> relation = relations.get(relationName);
			List<String> columns = relation.iterator().next().getColumns();
			ChangeAwareMondixRelation<Row> changeAwareMondixRelation = new ChangeAwareMondixRelation<Row>(this, relationName, columns, relation);
			changeAwareMondixRelations.add(changeAwareMondixRelation);
			return changeAwareMondixRelation;
		}
	}

	@Override
	public IChangeAwareMondixRelation getCatalogRelation() {
		return getBaseRelationByName("");
	}
	
	public void addRelation(String name, HashSet<ImmutableMapRow> ages) {
		throw new UnsupportedOperationException("live catalogs currently not supported");
	}
	
	public void removeRelation(String name) {
		throw new UnsupportedOperationException("live catalogs currently not supported");
	}
	
	public void addRow(String relationName, Row row) {
		relations.get(relationName).add(row);
		for(ChangeAwareMondixRelation<Row> changeAwareMondixRelation : changeAwareMondixRelations) {
			changeAwareMondixRelation.addRow(row);
		}
		notifyConsistencyListeners();
	}

	public void removeRow(String relationName, Row row) {
		relations.get(relationName).remove(row);
		for(ChangeAwareMondixRelation<Row> changeAwareMondixRelation : changeAwareMondixRelations) {
			changeAwareMondixRelation.removeRow(row);;
		}
		notifyConsistencyListeners();
	}
		
	private void notifyConsistencyListeners() {
		for(IConsistencyCallback consistencyListener : consistencyListeners) {
			consistencyListener.consistentNow(this);
		}
	}

}
