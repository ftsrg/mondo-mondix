package eu.mondo.mondix.implementation.hashmap;

import java.util.List;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;

public abstract class AbstractRelation<Row> implements IMondixRelation {
	protected IMondixInstance mondixInstance;
	protected String name;
	protected List<String> columns;

	public AbstractRelation(IMondixInstance mondixInstance, String name, List<String> columns) {
		this.mondixInstance = mondixInstance;
		this.name = name;
		this.columns = columns;
	}
	
	@Override
	public IMondixInstance getIndexerInstance() {
		return mondixInstance;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getColumns() {
		return columns;
	}

	@Override
	public int getArity() {
		return columns.size();
	}

	public void setIndexerInstance(IMondixInstance mondixInstance) {
		this.mondixInstance = mondixInstance;
	}

}
