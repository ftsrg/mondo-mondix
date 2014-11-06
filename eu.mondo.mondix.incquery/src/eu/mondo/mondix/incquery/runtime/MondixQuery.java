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

package eu.mondo.mondix.incquery.runtime;

import org.eclipse.incquery.runtime.api.GenericQuerySpecification;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.api.scope.IncQueryScope;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherContext;

import eu.mondo.mondix.incquery.MondixScope;

/**
 * @author Bergmann Gabor
 *
 */
public abstract class MondixQuery extends
		GenericQuerySpecification<MondixMatcher> 
{
	protected static final IPatternMatcherContext CONTEXT = MondixPatternMatcherContext.STATIC_INSTANCE;

	@Override
	public Class<? extends IncQueryScope> getPreferredScopeClass() {
		return MondixScope.class;
	}

	@Override
	protected MondixMatcher instantiate(IncQueryEngine engine) throws IncQueryException {
		return MondixMatcher.on(engine, this);
	}

}