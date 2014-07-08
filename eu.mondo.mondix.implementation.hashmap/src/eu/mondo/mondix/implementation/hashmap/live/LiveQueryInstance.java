package eu.mondo.mondix.implementation.hashmap.live;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.MondixQueryInstance;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;
import eu.mondo.mondix.live.IChangeCallback;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class LiveQueryInstance<Row extends AbstractRow> extends MondixQueryInstance<Row> implements ILiveQueryInstance {

	protected ChangeAwareMondixRelation<Row> changeAwaremondixRelation;
	
	protected Set<IChangeCallback> changeCallbacks;

	public LiveQueryInstance(ChangeAwareMondixRelation<Row> changeAwaremondixRelation, Set<Row> rows) {
		super(changeAwaremondixRelation, rows);
		this.changeAwaremondixRelation = changeAwaremondixRelation;
		this.changeCallbacks = new HashSet<IChangeCallback>();
	}

	public LiveQueryInstance(ChangeAwareMondixRelation<Row> changeAwaremondixRelation, Set<Row> rows,
			List<String> selectedColumnNames, Map<String, Object> filter) {
		super(changeAwaremondixRelation, rows, selectedColumnNames, filter);
		this.changeAwaremondixRelation = changeAwaremondixRelation;
		this.changeCallbacks = new HashSet<IChangeCallback>();
	}

	@Override
	public void addChangeListener(IChangeCallback changeListener) {
		changeCallbacks.add(changeListener);
	}

	@Override
	public void removeChangeListener(IChangeCallback changeListener) {
		changeCallbacks.remove(changeListener);
	}
	
	@Override
	public IChangeAwareMondixRelation getBaseRelation() {
		return changeAwaremondixRelation;
	}

	public void addRow(Row row) {
		if (isMatching(row, filter)) {
			ArrayList<Object> tuple = createTuple(selectedColumnNames, row);
			tuples.add(tuple);
			notifyConsistencyCallbacks(true, tuple);
		}
	}

	public void removeRow(Row row) {
		if (isMatching(row, filter)) {
			ArrayList<Object> tuple = createTuple(selectedColumnNames, row);
			tuples.remove(tuple);
			notifyConsistencyCallbacks(false, tuple);
		}
	}

	private void notifyConsistencyCallbacks(boolean inserted, List<?> changedTuple) {
		for(IChangeCallback changeCallback : changeCallbacks) {
			changeCallback.changed(this, inserted, changedTuple);
		}
	}

	@Override
	public void dispose() {
		changeAwaremondixRelation.removeLiveQueryInstance(this);
	}
	
}
