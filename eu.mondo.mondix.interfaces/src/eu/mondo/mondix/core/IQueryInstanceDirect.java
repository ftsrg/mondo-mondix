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

/**
 * A specialized version of {@link IQueryInstance} that offers direct access to the internal memory representation of the indexer to 
 *   avoid copying result tuples into the standard format. This may provide better performance if the indexer is accessed locally. 
 * 
 * @author Bergmann Gabor
 * @param <Row> the class that represents a tuple/row of the query results. 
 *   Can be any POJO - clients should extract values using the provided tuple interpreter. 
 *
 */
public interface IQueryInstanceDirect<Row> extends IQueryInstance {
	/**
	 * Core functionality: returns all tuples formed by selected columns of the base relation that are in the result set of this (filtered) query instance.
	 * <p> Each tuple will be returned in the native tuple/row format of the indexer. 
	 * <p> Invoking this method avoids copying the results into the standard list format. 
	 */
	public Iterable<? extends Row> getAllTuplesDirect();
	
	/**
	 * Returns an adapter object to interpret internal row representations.
	 */
	public RowInterpreter<Row> getRowInterpreter();
	
	/**
	 * An adapter object to interpret the internal row representation of a {@link IQueryInstanceDirect}.
	 * 
	 * @author Bergmann Gabor
	 *
	 * @param <Row> the class that represents a tuple/row of the query results. 
	 *   Can be any POJO - clients should extract values using the provided tuple interpreter. 
	 */
	public static interface RowInterpreter<Row> {
		/**
		 * Returns the value of the given row at the given position (according to the ordering in the list of selected columns).
		 */
		public Object getValueAtPosition(Row row, int position);
		/**
		 * Returns the value of the given column in the given row.
		 */
		public Object getValueOfColumn(Row row, String columnName);
		
	}
}
