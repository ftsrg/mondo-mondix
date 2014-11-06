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
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.api.scope.IBaseIndex;
import org.eclipse.incquery.runtime.api.scope.IIndexingErrorListener;
import org.eclipse.incquery.runtime.api.scope.IInstanceObserver;
import org.eclipse.incquery.runtime.api.scope.IncQueryBaseIndexChangeListener;

import eu.mondo.mondix.incquery.MondixScope;

/**
 * @author Bergmann Gabor
 *
 * TODO unimplemented dummy
 */
public class MondixBaseIndex implements IBaseIndex {

	private MondixScope mondixScope;
	private IIndexingErrorListener errorListener;
	private Logger logger;

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
	}

	public MondixScope getMondixScope() {
		return mondixScope;
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

}
