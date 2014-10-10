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
	
	/**
	 * Change-aware mondix relations that will be notified from changes.
	 */
	protected ChangeAwareMondixRelation<Row> changeAwaremondixRelation;
	
	/**
	 * Clients to be notified from query result changes.
	 */
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
	
	/**
	 * Add clients to be notified from query result changes.
	 */
	@Override
	public void addChangeListener(IChangeCallback changeListener) {
		changeCallbacks.add(changeListener);
	}
	
	/**
	 * Terminate notifying client.
	 */
	@Override
	public void removeChangeListener(IChangeCallback changeListener) {
		changeCallbacks.remove(changeListener);
	}
	
	@Override
	public IChangeAwareMondixRelation getBaseRelation() {
		return changeAwaremondixRelation;
	}
	
	/**
	 * Process notification from relations that a new row is added.
	 * @param row tuple to be added
	 */
	public void addRow(Row row) {
		// add row only if it matches with filter
		if (isMatch(row, filter)) {
			// project rows to selected attributes
			ArrayList<Object> tuple = createTuple(row, selectedColumnNames);
			tuples.add(tuple);
			notifyConsistencyCallbacks(true, tuple);
		}
	}
	
	/**
	 * Process notification from relations that a new row is deleted.
	 * @param row tuple to be deleted
	 */
	public void removeRow(Row row) {
		if (isMatch(row, filter)) {
			ArrayList<Object> tuple = createTuple(row, selectedColumnNames);
			tuples.remove(tuple);
			notifyConsistencyCallbacks(false, tuple);
		}
	}
	
	/**
	 * Notify clients from result set changes.
	 * @param inserted true, iff the tuple was inserted, otherwise removed
	 * @param changedTuple
	 */
	private void notifyConsistencyCallbacks(boolean inserted, List<?> changedTuple) {
		for(IChangeCallback changeCallback : changeCallbacks) {
			changeCallback.changed(this, inserted, changedTuple);
		}
	}
	
	/**
	 * Unregister query instance from parent relation.
	 */
	@Override
	public void dispose() {
		changeAwaremondixRelation.removeLiveQueryInstance(this);
	}
	
}
