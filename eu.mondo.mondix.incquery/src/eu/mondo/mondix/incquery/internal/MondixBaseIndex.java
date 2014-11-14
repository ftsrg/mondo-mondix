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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.api.scope.IBaseIndex;
import org.eclipse.incquery.runtime.api.scope.IIndexingErrorListener;
import org.eclipse.incquery.runtime.api.scope.IInstanceObserver;
import org.eclipse.incquery.runtime.api.scope.IncQueryBaseIndexChangeListener;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherRuntimeContextListener;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.core.IMondixRelation;
import eu.mondo.mondix.core.IMondixView;
import eu.mondo.mondix.incquery.MondixScope;
import eu.mondo.mondix.incquery.viewspec.InputSpec;
import eu.mondo.mondix.incquery.viewspec.RelationSpec;
import eu.mondo.mondix.live.IChangeCallback;
import eu.mondo.mondix.live.ILiveView;

/**
 * @author Bergmann Gabor
 *
 * TODO unimplemented dummy
 */
public class MondixBaseIndex implements IBaseIndex {

	private MondixScope mondixScope;
	private IIndexingErrorListener errorListener;
	private Logger logger;
	private IMondixInstance mondixer;
	
	private Map<InputSpec, IMondixView> openMondixViews = new HashMap<InputSpec, IMondixView>();
	private Multimap<IMondixView, InputSpec> inputSpecForMondixView = HashMultimap.create();
	private ArrayList<IPatternMatcherRuntimeContextListener> patternMatcherListeners = new ArrayList<IPatternMatcherRuntimeContextListener>();

	/**
	 * @param mondixScope
	 * @param errorListener
	 * @param logger
	 */
	public MondixBaseIndex(MondixScope mondixScope,
			IIndexingErrorListener errorListener, Logger logger) {
				this.mondixScope = mondixScope;
				this.errorListener = errorListener;
				this.logger = logger;
				
				mondixer = mondixScope.getMondixer();
	}
	
	public void dispose() {
		Collection<IMondixView> values = openMondixViews.values();
		for (IMondixView mondixView : values) {
			if (mondixView instanceof ILiveView)
				((ILiveView) mondixView).removeChangeListener(changeListener);
			mondixView.dispose();
		}
		patternMatcherListeners = null;
		openMondixViews = null;
		inputSpecForMondixView = null;
		this.mondixer = null;		
	}

	public MondixScope getMondixScope() {
		return mondixScope;
	}

	
	public IMondixView toView(InputSpec typeObject) {
		IMondixView mondixView = openMondixViews.get(typeObject);
		if (mondixView == null) {
			// TODO add ViewSpec
			final RelationSpec spec = (RelationSpec)typeObject;
			final String relationName = spec.getRelationName();
			
			IMondixRelation relation = mondixer.getBaseRelationByName(relationName);
			if (relation == null)
				throw new IllegalArgumentException("Expected " + spec + " not found");
			if (relation.getColumns().size() != spec.getArity())
				throw new IllegalArgumentException("Expected " + spec + " found instead arity: " + spec.getArity());
			
			mondixView = relation.openView();
			openMondixViews.put(typeObject, mondixView);
			inputSpecForMondixView.put(mondixView, typeObject);
			
			if (mondixView instanceof ILiveView)
				((ILiveView) mondixView).addChangeListener(changeListener);
		}
		return mondixView;
	}
	public ILiveView toLiveView(InputSpec typeObject) {
		return (ILiveView) toView(typeObject);
	}
	

	public void subscribeBackendForUpdates(
			IPatternMatcherRuntimeContextListener contextListener) {
		patternMatcherListeners.add(contextListener);
	}

	public void unSubscribeBackendFromUpdates(
			IPatternMatcherRuntimeContextListener contextListener) {
		patternMatcherListeners.remove(contextListener);
	}

	

	@Override
	public <V> V coalesceTraversals(Callable<V> callable)
			throws InvocationTargetException {
		// TODO Auto-generated method stub
		try {
			return callable.call();
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}

	@Override
	public void addBaseIndexChangeListener(
			IncQueryBaseIndexChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeBaseIndexChangeListener(
			IncQueryBaseIndexChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resampleDerivedFeatures() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addIndexingErrorListener(IIndexingErrorListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeIndexingErrorListener(IIndexingErrorListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addInstanceObserver(IInstanceObserver observer,
			Object observedObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeInstanceObserver(IInstanceObserver observer,
			Object observedObject) {
		// TODO Auto-generated method stub
		return false;
	}

	
	MondixChangeListener changeListener = new MondixChangeListener();
	class MondixChangeListener implements IChangeCallback {

		@Override
		public void changed(ILiveView view, boolean inserted, List<?> changedTuple) {
			for (IPatternMatcherRuntimeContextListener listener : patternMatcherListeners) {
				for (InputSpec typeKey : inputSpecForMondixView.get(view)) {
					if (changedTuple.size() == 1)
						listener.updateUnary(inserted, changedTuple.get(0), typeKey);
					else if (changedTuple.size() == 2)
						listener.updateBinaryEdge(inserted, changedTuple.get(0), changedTuple.get(1), typeKey);
					else throw new IllegalArgumentException();
				}
			}
		}
	}
}
