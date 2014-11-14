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

import eu.mondo.mondix.core.IViewDirect;

/**
 * A live mondix view that exposes its tuples through a direct interface.
 * @author Bergmann Gabor
 *
 */
public interface ILiveViewDirect<Row> extends IViewDirect<Row>, ILiveView {
	/**
	 * The given listener will be notified of changes from now on. 
	 * The change listener will have to be removed before disposing the mondix view.
	 */
	public void addChangeListener(IChangeCallbackDirect<Row> changeListener);
	
	/**
	 * The given listener will no longer be notified of changes from now on.
	 */
	public void removeChangeListener(IChangeCallbackDirect<Row> changeListener);
}
