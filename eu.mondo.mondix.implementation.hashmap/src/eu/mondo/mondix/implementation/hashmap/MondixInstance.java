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

/**
 * A static Mondix instance working with HashMap based relations.
 *
 * @param <Row> Any type implementing the AbstractRow interface containing immutable objects.
 */
public class MondixInstance<Row extends AbstractRow> implements IMondixInstance {
	
	/**
	 * Change-aware catalog relation containing the name of relations under the name attribute.
	 */
	protected ChangeAwareMondixRelation<ImmutableMapRow> catalogRelation;
	
	/**
	 * Set-based storing of relations.
	 */
	protected Map<String, Set<Row>> relations;
	
	/**
	 * Initialize Mondix instance with relations and fill up catalog with the name of relations.
	 * @param relations Initial input data.
	 * @throws Exception An Exception is thrown, when the input relation contains "" as relation name, as it is reserved for the catalog.
	 */
	public MondixInstance(Map<String, Set<Row>>  relations) throws Exception {
		// save relations and set this as base indexer
		this.relations = relations;
		
		// check for reserved empty string used for catalog
		if (relations.containsKey("")) {
			throw new Exception("Empty string as relation name is reserved for the catalog relation!");
		}
		
		// create catalog relation using ImmutableMapRow as the default row type
		List<String> catalogColumns = new ArrayList<String>();
		catalogColumns.add("name");
		
		Set<ImmutableMapRow> catalogData = new HashSet<ImmutableMapRow>();
		for(String relationName : relations.keySet()) {
			ImmutableMapRow row = new ImmutableMapRow(ImmutableMap.<String, String>builder().put("name", relationName).build());
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
			List<String> columns = null;
			if (relation.iterator().hasNext())
				columns = relation.iterator().next().getColumns();
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
		// This catalog contains one attribute, in other case selectedColumnNames parameter can be used with empty filter.
		IQueryInstance queryInstance = catalogRelation.openQueryInstance();
		return (IUnaryQueryInstance) queryInstance;
	}
}
