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
 * A query with potential parameter substitutions (seed). 
 * 
 * <p>The query results are obtained from a base relation by seeding it with a query.
 * 
 * @author Bergmann Gabor
 *
 */
public interface IQueryInstance<Tuple> {
	
	/**
	 * The base relation which is seeded by this query instance.
	 */
	IMondixRelation<Tuple> getBaseRelation();
	
	/**
	 * Returns the tuple with which the main relation is seeded. 
	 * 
	 * <p> The tuple has the same arity as the base relation. 
	 * Each element is either null, indicating a free parameter, of a non-null value, indicating a substitution.
	 * 
	 */
	Tuple getSeedTuple();
	
	/**
	 * Indicate that this query instance will no longer be needed, and any supporting index structures can be disposed unless used elsewhere.
	 */
	public void dispose();
	
	
	/**
	 * Core functionality: returns the number of tuples of the base relation that are in the result set of this (seeded) query instance.
	 */
	public int getCountOfTuples();
	
	/**
	 * Core functionality: returns all tuples of the base relation that are in the result set of this (seeded) query instance.
	 * <p> Seeded parameters will be included in the tuple. 
	 */
	public Iterable<? extends Tuple> getAllTuples();
	
	/**
	 * Core functionality: returns all tuples of free variables that are in the result set of this (seeded) query instance.
	 * <p> Seeded parameters will NOT be included in the tuple, only free parameters. 
	 * <p> TODO: optional?
	 */
	public Iterable<? extends Tuple> getAllValuesOfFreeParameters();

}
