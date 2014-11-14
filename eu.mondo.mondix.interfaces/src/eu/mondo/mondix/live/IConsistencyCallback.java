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

/**
 * A callback interface for change-aware mondix instances to notify that they are in a consistent state.
 * @author Bergmann Gabor
 */
public interface IConsistencyCallback {
	/**
	 * This callback is called when the given indexer is in a consistent state.
	 * 
	 * <p> Guarantees: <ul>
	 * <li> At callback time, all queries of the mondix instance return mutually consistent results. 
	 * <li> Between the current consistency callback and the previous one (if any), 
	 *  all {@link IChangeCallback} listeners of all live queries have been delivered a set of change notifications 
	 *  that is equivalent to the delta between the current consistent state and the previous one. 
	 *  <li> If the model has changed since the previous consistency callback 
	 *   in a way to influence the result of 
	 *   	any open {@link ILiveView} with {@link IChangeCallback} listeners attached,
	 *   this consistency callback is guaranteed to be called eventually.
	 *  <li> Changing the model is permitted during this callback, though it may lead to an inconsistent state again.
	 * </ul> 
	 * 
	 * TODO: deliver a version/revision number, e.g. a monotonously increasing sequence ID of the consistency point?
	 */
	public void consistentNow(IChangeAwareMondixInstance mondixInstance);
}
