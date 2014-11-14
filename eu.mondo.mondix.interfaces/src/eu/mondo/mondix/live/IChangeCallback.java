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

import java.util.List;


/**
 * A callback interface for live mondix views to deliver information about their changes.
 * @author Bergmann Gabor
 */
public interface IChangeCallback {
	/**
	 * Called upon each elementary change to the live view's results.
	 * @param inserted true if tuple was inserted, false if it was removed
	 * 
	 * <p> WARNING: model MUST NOT be accessed within the callback. 
	 * <p> WARNING: consistency of query results not guaranteed at callback time. 
	 */
	public void changed(ILiveView view, boolean inserted, List<?> changedTuple);

}
