package eu.mondo.mondix.implementation.hashmap.live;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.implementation.hashmap.AbstractRow;
import eu.mondo.mondix.implementation.hashmap.DefaultMondixRelation;
import eu.mondo.mondix.live.IChangeAwareMondixRelation;

// TODO move add/remove row methods to subclass (not all change-aware relations need be like this)
public class ChangeAwareMondixRelation<Row extends AbstractRow> extends DefaultMondixRelation<Row> implements IChangeAwareMondixRelation {
	
	/**
	 * Store live instances to notify them from changes.
	 */
	Set<LiveView<Row>> liveViews;
	
	public ChangeAwareMondixRelation(IMondixInstance mondixInstance, String name, List<String> columns, Set<Row> data) {
		super(mondixInstance, name, columns, data);
		liveViews = new HashSet<LiveView<Row>>();
	}
	
	@Override
	public LiveView<Row> openView() {
		LiveView<Row> liveView;
		if (getArity() == 1)
			liveView = new LiveUnaryView<Row>(this, rows);
		else if (getArity() == 0)
			liveView = new LiveNullaryView<Row>(this, rows);
		else 
			liveView = new LiveView<Row>(this, rows);
		liveViews.add(liveView);
		return liveView;
	}
	
	@Override
	public LiveView<Row> openView(List<String> selectedColumnNames, Map<String, Object> filter) {
		LiveView<Row> liveView;
		if (selectedColumnNames.size() == 1)
			liveView = new LiveUnaryView<Row>(this, rows, selectedColumnNames, filter);
		else if (selectedColumnNames.size() == 0)
			liveView = new LiveNullaryView<Row>(this, rows, selectedColumnNames, filter);
		else 
			liveView = new LiveView<Row>(this, rows, selectedColumnNames, filter);
		liveViews.add(liveView);
		return liveView;
	}
	
	/**
	 * Forward changes to views.
	 * @param row added tuple
	 */
	public void addRow(Row row) {
		rows.add(row);
		for(LiveView<Row> liveView : liveViews) {
			liveView.addRow(row);
		}
	}
	
	/**
	 * Forward changes to views.
	 * @param row removed tuple
	 */
	public void removeRow(Row row) {
		rows.remove(row);
		for(LiveView<Row> liveView : liveViews) {
			liveView.removeRow(row);
		}
	}
	
	/**
	 * Terminate notificating the given view.
	 * @param liveView
	 */
	public void removeLiveView(LiveView<Row> liveView) {
		liveViews.remove(liveView);
	}
}
