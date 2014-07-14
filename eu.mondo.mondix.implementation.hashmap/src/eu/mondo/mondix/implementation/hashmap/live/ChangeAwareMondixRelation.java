package eu.mondo.mondix.implementation.hashmap.live;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.DefaultMondixRelation;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;

public class ChangeAwareMondixRelation<Row extends AbstractRow> extends DefaultMondixRelation<Row> implements IChangeAwareMondixRelation {

	Set<LiveQueryInstance<Row>> liveQueryInstances;
	
	public ChangeAwareMondixRelation(IMondixInstance mondixInstance, String name, List<String> columns, Set<Row> data) {
		super(mondixInstance, name, columns, data);
		liveQueryInstances = new HashSet<LiveQueryInstance<Row>>();
	}

	@Override
	public LiveQueryInstance<Row> openQueryInstance() {
		LiveQueryInstance<Row> liveQueryInstance;
		if (getArity() == 1)
			liveQueryInstance = new LiveUnaryQueryInstance<Row>(this, rows);
		else if (getArity() == 0)
			liveQueryInstance = new LiveNullaryQueryInstance<Row>(this, rows);
		else 
			liveQueryInstance = new LiveQueryInstance<Row>(this, rows);
		liveQueryInstances.add(liveQueryInstance);
		return liveQueryInstance;
	}
	
	@Override
	public LiveQueryInstance<Row> openQueryInstance(List<String> selectedColumnNames, Map<String, Object> filter) {
		LiveQueryInstance<Row> liveQueryInstance;
		if (selectedColumnNames.size() == 1)
			liveQueryInstance = new LiveUnaryQueryInstance<Row>(this, rows, selectedColumnNames, filter);
		else if (selectedColumnNames.size() == 0)
			liveQueryInstance = new LiveNullaryQueryInstance<Row>(this, rows, selectedColumnNames, filter);
		else 
			liveQueryInstance = new LiveQueryInstance<Row>(this, rows, selectedColumnNames, filter);
		liveQueryInstances.add(liveQueryInstance);
		return liveQueryInstance;
	}
	
	public void addRow(Row row) {
		rows.add(row);
		for(LiveQueryInstance<Row> liveQueryInstance : liveQueryInstances) {
			liveQueryInstance.addRow(row);
		}
	}
	public void removeRow(Row row) {
		rows.remove(row);
		for(LiveQueryInstance<Row> liveQueryInstance : liveQueryInstances) {
			liveQueryInstance.removeRow(row);
		}
	}
	
	public void removeLiveQueryInstance(LiveQueryInstance<Row> liveQueryInstance) {
		liveQueryInstances.remove(liveQueryInstance);
	}
}
