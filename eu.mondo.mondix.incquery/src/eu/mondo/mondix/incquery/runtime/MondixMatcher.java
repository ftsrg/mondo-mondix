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

import org.eclipse.incquery.runtime.api.GenericPatternMatcher;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.exception.IncQueryException;

/**
 * @author Bergmann Gabor
 *
 */
public class MondixMatcher extends GenericPatternMatcher {

	protected MondixMatcher(
			IncQueryEngine engine,
			MondixQuery specification)
			throws IncQueryException {
		super(engine, specification);
	}
	
    /**
     * Initializes the pattern matcher within an existing IncQuery engine. 
     * If the pattern matcher is already constructed in the engine, only a light-weight reference is returned. 
     * The match set will be incrementally refreshed upon updates.
     * 
     * @param engine
     *            the existing EMF-IncQuery engine in which this matcher will be created.
     * @param querySpecification
     *            the query specification for which the matcher is to be constructed.
     * @throws IncQueryException
     *             if an error occurs during pattern matcher creation
     */
	public static MondixMatcher on(IncQueryEngine engine, MondixQuery querySpecification) throws IncQueryException {
		// check if matcher already exists
		MondixMatcher matcher = engine.getExistingMatcher(querySpecification);
        if (matcher == null) {
        	matcher = new MondixMatcher(engine, querySpecification);
        	// do not have to "put" it into engine.matchers, reportMatcherInitialized() will take care of it
        } 	
        return matcher;
	}
	

}
