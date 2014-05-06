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

/**
 * Represents a base relation for which queries can be opened.
 * @author Bergmann Gabor
 * 
 * @param Tuple the tuple type of this relation
 *
 */
public interface IMondixRelation<Tuple> {
	
	// TODO name, parameter names
	
	// TODO hint/prepare frequently seeded parameters
	
	/**
	 * Returns the indexer instance that owns and maintains this base relation.
	 */
	public IMondixInstance getIndexerInstance();
	
	/**
	 * Returns the number of parameters of this relation.
	 */
	public int getArity();
	
	/**
	 * Returns a query instance against the entire contents of the relation.
	 * <p> Result should be disposed if no longer used.
	 * <p> Same as {@link #openSeededQueryInstance(List)} parameterized by an all-null tuple.
	 */
	public IQueryInstance<Tuple> openQueryInstance();
	/**
	 * Returns a query instance seeded by the given parameter tuple.
	 * <p> Result should be disposed if no longer used.
	 * <p> Guaranteed to return {@link IUnaryQueryInstance} or {@link INullaryQueryInstance} 
	 * 	if 1 respectively 0 free variables remain.
	 */
	public IQueryInstance<Tuple> openSeededQueryInstance(Tuple seedTuple);

}
