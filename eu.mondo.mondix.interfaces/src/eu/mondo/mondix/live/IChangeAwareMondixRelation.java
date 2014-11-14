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
import java.util.Map;

import eu.mondo.mondix.core.IMondixRelation;

/**
 * Change-aware guarantees to the Mondix relation concept.
 * <p> mondix views are live queries.
 * @author Bergmann Gabor
 *
 */
public interface IChangeAwareMondixRelation extends IMondixRelation {
	@Override
	public ILiveView openView();
	@Override
	public ILiveView openView(List<String> selectedColumnNames, Map<String, Object> filter);
}
