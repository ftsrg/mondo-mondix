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
 * A view with potential parameter substitutions (seed). 
 * 
 * <p>The view contents are obtained from a base relation by row filtering (seeding) and column projection.
 * <p>Performance guarantee: query operations should be quick (execution time proportional to result size). 
 * 	A potential one-time initialization cost can be associated with obtaining the mondix view.
 * <p>The mondix view should be disposed if no longer used, and should not be used after disposal.
 * 
 * <p>See also interface extensions {@link IUnaryView}, {@link INullaryView}, {@link IViewDirect}
 * 
 * @author Bergmann Gabor
 *
 */
public interface IMondixView {
	
	/**
	 * The base relation which is seeded by this mondix view.
	 */
	IMondixRelation getBaseRelation();
	
	
	/**
	 * Returns the list of column names that are selected from the original base relation.
	 * @return the ordered, unique list of columns that should be returned for tuples in this relation (can be empty). 
	 */
	List<String> getSelectedColumnNames();
	
	/**
	 * Returns the filter applied by this view. 
	 * <p> The results is filtered to those tuples only that take the given values at the given columns.
	 * 
	 * @return a map from zero or more column names to concrete values. 
	 */
	Map<String, Object> getFilter();
	
	
	
	/**
	 * Indicate that this mondix view will no longer be needed, and any supporting index structures can be disposed unless used elsewhere.
	 */
	public void dispose();
	
	
	/**
	 * Core functionality: returns the number of tuples of the base relation that are in the result set of this (seeded) mondix view.
	 */
	public int getCountOfTuples();
	
	
	/**
	 * Core functionality: returns all tuples formed by selected columns of the base relation that are in the result set of this (filtered) mondix view.
	 * <p> Each tuple will be returned as a list of values, one for each selected columns (see {@link #getSelectedColumnNames()}).
	 * <p> Returned tuples must be distinct.
	 * <p> Invoking this method may involve copying the indexer contents into such a list-based row format. 
	 * This copying is necessary for cross-network transfer anyway; more efficient solutions may be available for local clients if the {@link IViewDirect} interface extension is provided.
	 */
	public Iterable<? extends List<?>> getAllTuples();
	
	

}
