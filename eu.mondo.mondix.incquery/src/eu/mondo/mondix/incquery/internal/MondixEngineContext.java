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

package eu.mondo.mondix.incquery.internal;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.api.scope.IEngineContext;
import org.eclipse.incquery.runtime.api.scope.IIndexingErrorListener;
import org.eclipse.incquery.runtime.exception.IncQueryException;

import eu.mondo.mondix.incquery.MondixScope;

/**
 * @author Bergmann Gabor
 *
 */
public class MondixEngineContext implements IEngineContext {

	private MondixScope mondixScope;
	private IncQueryEngine engine;
	private IIndexingErrorListener errorListener;
	private Logger logger;

	private MondixPatternMatcherRuntimeContext matcherContext;
	MondixBaseIndex baseIndex;
	
	
	public MondixEngineContext(MondixScope mondixScope, IncQueryEngine engine,
			IIndexingErrorListener errorListener, Logger logger) 
	{
				this.mondixScope = mondixScope;
				this.engine = engine;
				this.errorListener = errorListener;
				this.logger = logger;
	}

	@Override
	public MondixBaseIndex getBaseIndex() throws IncQueryException {
		if (baseIndex == null) {
			//final NavigationHelper navigationHelper = getNavHelper();
			baseIndex = new MondixBaseIndex(mondixScope, errorListener, logger);
			
			// TODO proper deferring?
		}
		return baseIndex;
	}

	@Override
	public void initializeBackends(IQueryBackendInitializer initializer)
			throws IncQueryException {
 	   // TODO proper deferring?
 	   if (matcherContext == null) 
		   matcherContext = new MondixPatternMatcherRuntimeContext(engine, logger, getBaseIndex());
	   
	   initializer.initializeWith(matcherContext);
	}

	@Override
	public void dispose() {
		if (matcherContext != null) matcherContext.dispose();
		//if (navHelper != null) navHelper.dispose();
		
		this.baseIndex = null;
		this.mondixScope = null;
		this.engine = null;
		this.errorListener = null;
		this.logger = null;
	}

}
