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

import eu.mondo.mondix.core.IMondixRelation;

/**
 * Change-aware guarantees to the Mondix relation concept.
 * @author Bergmann Gabor
 *
 */
public interface IChangeAwareMondixRelation<Tuple> extends IMondixRelation<Tuple> {
	@Override
	public ILiveQueryInstance<Tuple> openQueryInstance();
	@Override
	public ILiveQueryInstance<Tuple> openSeededQueryInstance(Tuple seedTuple);
}
