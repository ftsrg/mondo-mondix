package eu.mondo.mondix.implementation.hashmap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IQueryInstance;

public class DefaultMondixRelation<Row extends AbstractRow> extends AbstractRelation<Row> {
	
	protected Set<Row> rows;
	
	public DefaultMondixRelation(IMondixInstance mondixInstance, String name, List<String> columns, Set<Row> data) {
		super(mondixInstance, name, columns);
		rows = new HashSet<Row>(data);
	}

	@Override
	public IQueryInstance openQueryInstance() {
		if (getArity() == 1)
			return new MondixUnaryQueryInstance<Row>(this, rows);
		else if (getArity() == 0)
			return new MondixNullaryQueryInstance<Row>(this, rows);
		return new MondixQueryInstance<Row>(this, rows);
	}

	@Override
	public IQueryInstance openQueryInstance(List<String> selectedColumnNames, Map<String, Object> filter) {
		if (getArity() == 1)
			return new MondixUnaryQueryInstance<Row>(this, rows, selectedColumnNames, filter);
		else if (getArity() == 0)
			return new MondixNullaryQueryInstance<Row>(this, rows, selectedColumnNames, filter);
		return new MondixQueryInstance<Row>(this, rows, selectedColumnNames, filter);
	}

}
