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

import eu.mondo.mondix.core.IMondixView;

/**
 * A mondix view that can deliver live info about its changes through callbacks. 
 * @author Bergmann Gabor
 *
 */
public interface ILiveView extends IMondixView {
	// TODO Unary/Nullary specializations?
	
	@Override
	public IChangeAwareMondixRelation getBaseRelation();
	/**
	 * The given listener will be notified of changes from now on. 
	 * The change listener will have to be removed before disposing the mondix view.
	 */
	public void addChangeListener(IChangeCallback changeListener);
	
	/**
	 * The given listener will no longer be notified of changes from now on.
	 */
	public void removeChangeListener(IChangeCallback changeListener);

}
