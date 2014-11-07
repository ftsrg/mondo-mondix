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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherRuntimeContext;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherRuntimeContextListener;

import eu.mondo.mondix.core.IQueryInstance;
import eu.mondo.mondix.incquery.runtime.MondixPatternMatcherContext;
import eu.mondo.mondix.incquery.viewspec.InputSpec;

/**
 * @author Bergmann Gabor
 *
 */
public class MondixPatternMatcherRuntimeContext extends MondixPatternMatcherContext implements
		IPatternMatcherRuntimeContext {

	private IncQueryEngine engine;
	private Logger logger;
	private MondixBaseIndex baseIndex;

	public MondixPatternMatcherRuntimeContext(IncQueryEngine engine,
			Logger logger, MondixBaseIndex baseIndex) {
				this.engine = engine;
				this.logger = logger;
				this.baseIndex = baseIndex;
				
	}

	public void dispose() {
		engine = null;
		baseIndex = null;
		logger = null;
	}

	protected IQueryInstance toQueryInstance(Object typeObject) {
		return baseIndex.toQueryInstance((InputSpec) typeObject);
	}
	
	
	@Override
	public void subscribeBackendForUpdates(
			IPatternMatcherRuntimeContextListener contextListener) {
		baseIndex.subscribeBackendForUpdates(contextListener);
	}

	@Override
	public void unSubscribeBackendFromUpdates(
			IPatternMatcherRuntimeContextListener contextListener) {
		baseIndex.unSubscribeBackendFromUpdates(contextListener);
	}

	@Override
	public Object ternaryEdgeTarget(Object relation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object ternaryEdgeSource(Object relation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateAllUnaries(ModelElementCrawler crawler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateAllTernaryEdges(ModelElementCrawler crawler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateAllBinaryEdges(ModelElementPairCrawler crawler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateDirectUnaryInstances(Object typeObject,
			ModelElementCrawler crawler) {
		IQueryInstance queryInstance = toQueryInstance(typeObject);
		for (List<?> tuple : queryInstance.getAllTuples()) {
			crawler.crawl(tuple.get(0));
		}
	}


	@Override
	public void enumerateDirectTernaryEdgeInstances(Object typeObject,
			ModelElementCrawler crawler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateDirectBinaryEdgeInstances(Object typeObject,
			ModelElementPairCrawler crawler) {
		IQueryInstance queryInstance = toQueryInstance(typeObject);
		for (List<?> tuple : queryInstance.getAllTuples()) {
			crawler.crawl(tuple.get(0), tuple.get(1));
		}
	}

	@Override
	public void enumerateAllUnaryInstances(Object typeObject,
			ModelElementCrawler crawler) {
		enumerateDirectUnaryInstances(typeObject, crawler);
	}

	@Override
	public void enumerateAllTernaryEdgeInstances(Object typeObject,
			ModelElementCrawler crawler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateAllBinaryEdgeInstances(Object typeObject,
			ModelElementPairCrawler crawler) {
		enumerateDirectBinaryEdgeInstances(typeObject, crawler);
	}

	@Override
	public void enumerateAllUnaryContainments(ModelElementPairCrawler crawler) {
        throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateAllInstantiations(ModelElementPairCrawler crawler) {
        throw new UnsupportedOperationException();
	}

	@Override
	public void enumerateAllGeneralizations(ModelElementPairCrawler crawler) {
        throw new UnsupportedOperationException();
	}

	@Override
	public void modelReadLock() {
	}

	@Override
	public void modelReadUnLock() {
	}

	@Override
	public <V> V coalesceTraversals(Callable<V> callable)
			throws InvocationTargetException {
		return baseIndex.coalesceTraversals(callable);
	}



}
