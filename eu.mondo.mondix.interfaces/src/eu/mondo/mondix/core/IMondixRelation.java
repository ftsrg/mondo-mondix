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
 * Represents a base relation for which queries can be opened.
 * @author Bergmann Gabor
 * 
 * @param Tuple the tuple type of this relation
 *
 */
public interface IMondixRelation {
	
	// TODO hint/prepare frequently filtered columns
	
	/**
	 * Returns the indexer instance that owns and maintains this base relation.
	 */
	public IMondixInstance getIndexerInstance();
	
	/**
	 * Returns the name of this relation that uniquely identifies it within the indexer instance.
	 */
	public String getName();
	
	/**
	 * Returns an ordered list of column names.
	 */
	public List<String> getColumns();
	
	/**
	 * Returns the number of parameters/columns of this relation.
	 * <p> Equivalent to {@link #getColumns()}.size()
	 */
	public int getArity();
	
	/**
	 * Returns a mondix view against the entire contents of the relation.
	 * <p> Result should be disposed if no longer used.
	 * <p> Same as {@link #openView(List, Map)} parameterized by (null, null).
	 */
	public IMondixView openView();
	/**
	 * Returns a mondix view filtered to the given values that only returns the selected columns.
	 * <p> Result should be disposed if no longer used.
	 * <p> Guaranteed to return {@link IUnaryView} or {@link INullaryView} 
	 * 	if 1 respectively 0 columns are selected.
	 * 
	 * @param selectedColumnNames the ordered, unique list of columns that should be returned for tuples in this relation (can be empty). 
	 * 	If null is given, all columns are selected in the original order (see {@link #getColumns()}). 
	 * @param filter a map from zero or more column names to concrete values. 
	 *  The view will be filtered to those tuples only that take the given values at the given columns.
	 *  If null is given, no filtering is done (equivalently to an empty map).
	 */
	public IMondixView openView(List<String> selectedColumnNames, Map<String, Object> filter);

}
