package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;
import eu.mondo.mondix.core.IUnaryQueryInstance;
import eu.mondo.mondix.implementation.hashmap.live.ChangeAwareMondixRelation;


public class MondixInstance<Row extends AbstractRow> implements IMondixInstance {
	
	protected ChangeAwareMondixRelation<ImmutableMapRow> catalogRelation;
	protected Map<String, Set<Row>> relations;
	
	public MondixInstance(Map<String, Set<Row>>  relations) throws Exception {
		// save relations and set this as base indexer
		this.relations = relations;
		
		// check for reserved empty string used for catalog
		if (relations.containsKey("")) {
			throw new Exception("Empty string as relation name is reserved for the catalog relation!");
		}
		
		// create catalog relation
		List<String> catalogColumns = new ArrayList<String>();
		catalogColumns.add("name");
		
		Set<ImmutableMapRow> catalogData = new HashSet<ImmutableMapRow>();
		for(String attributeName : relations.keySet()) {
			ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", attributeName).build());
			catalogData.add(row);
		}
		ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", "").build());
		catalogData.add(row);
		
		catalogRelation = new ChangeAwareMondixRelation<ImmutableMapRow>(this, "", catalogColumns, catalogData);
	}
	
	@Override
	public IMondixRelation getBaseRelationByName(String relationName) {
		if ("".equals(relationName))
			return catalogRelation;
		else {
			Set<Row> relation = relations.get(relationName);
			List<String> columns = relation.iterator().next().getColumns();
			return new DefaultMondixRelation<Row>(this, relationName, columns, relation);
		}
		
	}
	
	@Override
	public IMondixRelation getCatalogRelation() {
		return getBaseRelationByName("");
	}
	
	@Override
	public IUnaryQueryInstance getPublishedRelationNames() {
		IMondixRelation catalogRelation = getCatalogRelation();
		IQueryInstance queryInstance = catalogRelation.openQueryInstance();
		return (IUnaryQueryInstance) queryInstance;
	}
}
