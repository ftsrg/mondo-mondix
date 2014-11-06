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

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.incquery.runtime.matchers.context.IPatternMatcherContext;

import eu.mondo.mondix.incquery.viewspec.RelationSpec;

/**
 * @author Bergmann Gabor
 *
 */
public class MondixPatternMatcherContext implements IPatternMatcherContext {

	public final static MondixPatternMatcherContext STATIC_INSTANCE = new MondixPatternMatcherContext();
    private Logger logger = Logger.getLogger(MondixPatternMatcherContext.class);
	
	@Override
	public EdgeInterpretation edgeInterpretation() {
		return EdgeInterpretation.BINARY;
	}

	@Override
	public GeneralizationQueryDirection allowedGeneralizationQueryDirection() {
		return GeneralizationQueryDirection.SUPERTYPE_ONLY_SMART_NOTIFICATIONS;
	}

	@Override
	public boolean isUnaryType(Object typeObject) {
		// TODO add ViewSpec
		return 1 == ((RelationSpec)typeObject).getArity();
	}
	@Override
	public boolean isBinaryEdgeType(Object typeObject) {
		// TODO add ViewSpec
		return 2 == ((RelationSpec)typeObject).getArity();
	}
	@Override
	public boolean isTernaryEdgeType(Object typeObject) {
		return false; // toQueryInstance(typeObject).getSelectedColumnNames().size() == 3;
	}
	
	@Override
	public Object ternaryEdgeTargetType(Object typeObject) {
		return null;
	}

	@Override
	public Object ternaryEdgeSourceType(Object typeObject) {
		return null;
	}

	@Override
	public Object binaryEdgeTargetType(Object typeObject) {
		return null;
	}

	@Override
	public Object binaryEdgeSourceType(Object typeObject) {
		return null;
	}


	@Override
	public Collection<? extends Object> enumerateDirectUnarySubtypes(
			Object typeObject) {
		return null;
	}

	@Override
	public Collection<? extends Object> enumerateDirectUnarySupertypes(
			Object typeObject) {
		return null;
	}


	@Override
	public boolean isTernaryEdgeMultiplicityToOne(Object typeObject) {
		return false;
	}

	@Override
	public boolean isTernaryEdgeMultiplicityOneTo(Object typeObject) {
		return false;
	}

	@Override
	public Collection<? extends Object> enumerateDirectTernaryEdgeSubtypes(
			Object typeObject) {
		return null;
	}

	@Override
	public Collection<? extends Object> enumerateDirectTernaryEdgeSupertypes(
			Object typeObject) {
		return null;
	}

	@Override
	public boolean isBinaryEdgeMultiplicityToOne(Object typeObject) {
		return false;
	}

	@Override
	public boolean isBinaryEdgeMultiplicityOneTo(Object typeObject) {
		return false;
	}

	@Override
	public Collection<? extends Object> enumerateDirectBinaryEdgeSubtypes(
			Object typeObject) {
		return null;
	}

	@Override
	public Collection<? extends Object> enumerateDirectBinaryEdgeSupertypes(
			Object typeObject) {
		return null;
	}

	@Override
	public Collection<? extends Object> enumerateDirectSupertypes(
			Object typeObject) {
		return null;
	}

	@Override
	public Collection<? extends Object> enumerateDirectSubtypes(
			Object typeObject) {
		return null;
	}

	@Override
	public void logFatal(String message) {
		logger.fatal(message);
	}

	@Override
	public void logFatal(String message, Throwable cause) {
		logger.fatal(message, cause);
	}

	@Override
	public void logError(String message) {
		logger.error(message);
	}

	@Override
	public void logError(String message, Throwable cause) {
		logger.error(message, cause);
	}

	@Override
	public void logWarning(String message) {
		logger.warn(message);
	}

	@Override
	public void logWarning(String message, Throwable cause) {
		logger.warn(message, cause);
	}

	@Override
	public void logDebug(String message) {
		logger.debug(message);
	}
	
	@Override
	public String printType(Object typeObject) {
		return typeObject.toString();
	}


}
