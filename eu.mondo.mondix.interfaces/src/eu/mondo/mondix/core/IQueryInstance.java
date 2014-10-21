/*******************************************************************************
 * Copyright (c) 2004-2014 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package eu.mondo.mondix.core;

import java.util.List;
import java.util.Map;


/**
 * A query with potential parameter substitutions (seed). 
 * 
 * <p>The query results are obtained from a base relation by seeding it with a query.
 * <p>Performance guarantee: query operations should be quick (execution time proportional to result size). 
 * 	A potential one-time initialization cost can be associated with obtaining the query instance.
 * <p>The query instance should be disposed if no longer used, and should not be used after disposal.
 * 
 * <p>See also interface extensions {@link IUnaryQueryInstance}, {@link INullaryQueryInstance}, {@link IQueryInstanceDirect}
 * 
 * @author Bergmann Gabor
 *
 */
public interface IQueryInstance {
	
	/**
	 * The base relation which is seeded by this query instance.
	 */
	IMondixRelation getBaseRelation();
	
	
	/**
	 * Returns the list of column names that are selected from the original base relation.
	 * @return the ordered, unique list of columns that should be returned for tuples in this relation (can be empty). 
	 */
	List<String> getSelectedColumnNames();
	
	/**
	 * Returns the filter applied by this query. 
	 * <p> The query is filtered to those tuples only that take the given values at the given columns.
	 * 
	 * @return a map from zero or more column names to concrete values. 
	 */
	Map<String, Object> getFilter();
	
	
	
	/**
	 * Indicate that this query instance will no longer be needed, and any supporting index structures can be disposed unless used elsewhere.
	 */
	public void dispose();
	
	
	/**
	 * Core functionality: returns the number of tuples of the base relation that are in the result set of this (seeded) query instance.
	 */
	public int getCountOfTuples();
	
	
	/**
	 * Core functionality: returns all tuples formed by selected columns of the base relation that are in the result set of this (filtered) query instance.
	 * <p> Each tuple will be returned as a list of values, one for each selected columns (see {@link #getSelectedColumnNames()}).
	 * <p> Returned tuples must be distinct.
	 * <p> Invoking this method may involve copying the indexer contents into such a list-based row format. 
	 * This copying is necessary for cross-network transfer anyway; more efficient solutions may be available for local clients if the {@link IQueryInstanceDirect} interface extension is provided.
	 */
	public Iterable<? extends List<?>> getAllTuples();
	
	

}
