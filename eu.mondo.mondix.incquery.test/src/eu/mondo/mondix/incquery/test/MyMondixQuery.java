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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.incquery.runtime.exception.IncQueryException;
import org.eclipse.incquery.runtime.matchers.psystem.PBody;
import org.eclipse.incquery.runtime.matchers.psystem.PVariable;
import org.eclipse.incquery.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.TypeBinary;
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.TypeUnary;
import org.eclipse.incquery.runtime.matchers.psystem.queries.PParameter;

import eu.mondo.mondix.incquery.runtime.MondixQuery;
import eu.mondo.mondix.incquery.viewspec.RelationSpec;

/**
 * @author Bergmann Gabor
 *
 */
public class MyMondixQuery extends MondixQuery {
	
	public MyMondixQuery() {
		ensureInitialized();
	}

	@Override
	public String getFullyQualifiedName() {
		return getClass().getCanonicalName();
	}

	@Override
	public List<PParameter> getParameters() {
		return Arrays.asList(
				new PParameter("x"),
				new PParameter("y")
		);
	}

	@Override
	protected Set<PBody> doGetContainedBodies() throws IncQueryException {
		Set<PBody> bodies = new LinkedHashSet<>();
		
		{
			PBody body = new PBody(this);
			
			PVariable x = body.getOrCreateVariableByName("x");
			PVariable y = body.getOrCreateVariableByName("y");
			body.setExportedParameters(Arrays.<ExportedParameter>asList(
					new ExportedParameter(body, x, x.getName()),
					new ExportedParameter(body, y, y.getName())
			));
			
			new TypeBinary(body, CONTEXT, x, y, new RelationSpec("fooBinary", 2), "fooBinary");
			new TypeUnary(body, x, new RelationSpec("fooUnary", 1), "fooUnary");
			
			bodies.add(body);
		}
		
		return bodies;
	}





}
