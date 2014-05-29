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

package eu.mondo.mondix.live;

import eu.mondo.mondix.core.IMondixInstance;

/**
 * Base relations published by a change-aware Mondix indexer instance may be {@link IChangeAwareMondixRelation}s. 
 * The indexer additionally publishes information whether it is currently in a consistent state.
 * 
 * @author Bergmann Gabor
 *
 */
public interface IChangeAwareMondixInstance extends IMondixInstance {	
	/**
	 * Returns true iff the entire mondix instance is in a consistent state.
	 * 
	 * <p> Guarantees if true is returned: <ul>
	 * <li> All queries of the mondix instance return mutually consistent results. 
	 * <li> Changing the model must be tolerated at this point.
	 * </ul> 
	 * 
	 * <p> See {@link IConsistencyCallback} for more guarantees. 
	 */
	// TODO move to regular Mondix Instance?
	public boolean isConsistent();
	
	/**
	 * The given listener will be notified of consistency points from now on. 
	 * <p> See {@link IConsistencyCallback} for guarantees. 
	 */
	public void addChangeListener(IConsistencyCallback consistencyListener);
	
	/**
	 * The given listener will no longer be notified of consistency points from now on.
	 */
	public void removeChangeListener(IConsistencyCallback consistencyListener);

}
