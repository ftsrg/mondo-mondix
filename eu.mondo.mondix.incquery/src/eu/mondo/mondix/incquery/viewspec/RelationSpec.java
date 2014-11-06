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

package eu.mondo.mondix.incquery.viewspec;

/**
 * @author Bergmann Gabor
 *
 */
public class RelationSpec {
	
	private String relationName;
	private int arity;
	/**
	 * @param relationName
	 * @param arity
	 */
	public RelationSpec(String relationName, int arity) {
		super();
		this.relationName = relationName;
		this.arity = arity;
	}
	public String getRelationName() {
		return relationName;
	}
	public int getArity() {
		return arity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arity;
		result = prime * result
				+ ((relationName == null) ? 0 : relationName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RelationSpec))
			return false;
		RelationSpec other = (RelationSpec) obj;
		if (arity != other.arity)
			return false;
		if (relationName == null) {
			if (other.relationName != null)
				return false;
		} else if (!relationName.equals(other.relationName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RelationSpec [relationName=" + relationName + ", arity="
				+ arity + "]";
	}

	
	
	
}
