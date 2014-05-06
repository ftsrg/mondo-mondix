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
 * A Mondix indexer that offers a number of base relations ({@link IMondixRelation}).
 * 
 * <p> Currently, no uniform way is provided to discover published base relations. 
 * 
 * @author Bergmann Gabor
 */
public interface IMondixInstance {
	
	// TODO: make published base relations discoverable 
	// 		by mandating a "relation of relations" (that can be change-aware etc.)

}
