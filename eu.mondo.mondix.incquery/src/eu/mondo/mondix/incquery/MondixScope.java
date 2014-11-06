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

package eu.mondo.mondix.incquery;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.api.IncQueryEngine;
import org.eclipse.incquery.runtime.api.scope.IEngineContext;
import org.eclipse.incquery.runtime.api.scope.IIndexingErrorListener;
import org.eclipse.incquery.runtime.api.scope.IncQueryScope;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.incquery.internal.MondixEngineContext;

/**
 * @author Bergmann Gabor
 *
 */
public class MondixScope extends IncQueryScope {
	
	IMondixInstance mondixer;
	
	public MondixScope(IMondixInstance mondixer) {
		super();
		this.mondixer = mondixer;
	}
	
	public IMondixInstance getMondixer() {
		return mondixer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mondixer == null) ? 0 : mondixer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MondixScope))
			return false;
		MondixScope other = (MondixScope) obj;
		if (mondixer == null) {
			if (other.mondixer != null)
				return false;
		} else if (!mondixer.equals(other.mondixer))
			return false;
		return true;
	}

	@Override
	protected IEngineContext createEngineContext(IncQueryEngine engine,
			IIndexingErrorListener errorListener, Logger logger) {
		return new MondixEngineContext(this, engine, errorListener, logger);
	}

}
