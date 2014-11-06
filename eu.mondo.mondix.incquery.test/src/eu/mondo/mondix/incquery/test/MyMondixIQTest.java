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

package eu.mondo.mondix.incquery.test;


import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.junit.Assert;
import org.junit.Test;

import eu.mondo.mondix.incquery.MondixScope;
import eu.mondo.mondix.incquery.runtime.MondixMatcher;

/**
 * @author Bergmann Gabor
 *
 */
public class MyMondixIQTest {
	
	@Test
	public void test() throws IncQueryException {
		MyMondixModel model = new MyMondixModel();
		MondixScope scope = new MondixScope(model.getMondixer());
		
		IncQueryEngine engine = IncQueryEngine.on(scope);
		MyMondixQuery query = new MyMondixQuery();
		MondixMatcher matcher = engine.getMatcher(query);
		
		Assert.assertEquals(4, matcher.countMatches());
		Assert.assertTrue(matcher.hasMatch(query.newMatch("x1", "y1")));
		Assert.assertTrue(matcher.hasMatch(query.newMatch("x1", "y2")));
		Assert.assertTrue(matcher.hasMatch(query.newMatch("x2", "y1")));
		Assert.assertTrue(matcher.hasMatch(query.newMatch("x2", "y3")));
	}

}
