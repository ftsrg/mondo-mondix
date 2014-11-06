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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherRuntimeContext;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherRuntimeContextListener;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IQueryInstance;
import eu.mondo.mondix.incquery.runtime.MondixPatternMatcherContext;
import eu.mondo.mondix.incquery.viewspec.RelationSpec;

/**
 * @author Bergmann Gabor
 *
 */
public class MondixPatternMatcherRuntimeContext extends MondixPatternMatcherContext implements
		IPatternMatcherRuntimeContext {

	private IncQueryEngine engine;
	private Logger logger;
	private MondixBaseIndex baseIndex;
	private IMondixInstance mondixer;

	public MondixPatternMatcherRuntimeContext(IncQueryEngine engine,
			Logger logger, MondixBaseIndex baseIndex) {
				this.engine = engine;
				this.logger = logger;
				this.baseIndex = baseIndex;
				
				mondixer = this.baseIndex.getMondixScope().getMondixer();
	}
	
	IQueryInstance toQueryInstance(Object typeObject) {
		IQueryInstance iQueryInstance = openQueryInstances.get(typeObject);
		if (iQueryInstance == null) {
			// TODO add ViewSpec
			final RelationSpec spec = (RelationSpec)typeObject;
			final String relationName = spec.getRelationName();
			
			IMondixRelation relation = mondixer.getBaseRelationByName(relationName);
			if (relation == null)
				throw new IllegalArgumentException("Expected " + spec + " not found");
			if (relation.getColumns().size() != spec.getArity())
				throw new IllegalArgumentException("Expected " + spec + " found instead arity: " + spec.getArity());
			
			iQueryInstance = relation.openQueryInstance();
		}
		return iQueryInstance;
	}
	Map<Object, IQueryInstance> openQueryInstances = new HashMap<Object, IQueryInstance>();
	

	public void dispose() {
		Collection<IQueryInstance> values = openQueryInstances.values();
		for (IQueryInstance iQueryInstance : values) {
			iQueryInstance.dispose();
		}
		
		openQueryInstances = null;
		engine = null;
		baseIndex = null;
		logger = null;
		this.mondixer = null;
	}

	
	
	@Override
	public void subscribeBackendForUpdates(
			IPatternMatcherRuntimeContextListener contextListener) {
		// TODO add change-awareness

	}

	@Override
	public void unSubscribeBackendFromUpdates(
			IPatternMatcherRuntimeContextListener contextListener) {
		// TODO add change-awareness
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
