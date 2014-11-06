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
import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.ImmutableMap;

import eu.mondo.mondix.core.IMondixInstance;
import eu.mondo.mondix.implementation.hashmap.ImmutableMapRow;
import eu.mondo.mondix.implementation.hashmap.live.ChangeAwareMondixInstance;

/**
 * @author Bergmann Gabor
 *
 */
public class MyMondixModel {
	
	ChangeAwareMondixInstance<ImmutableMapRow> mondixer;

	public IMondixInstance getMondixer() {
		return mondixer;
	}
	
	

	public MyMondixModel() {
		try {
			mondixer = new ChangeAwareMondixInstance<>(new HashMap(), new HashMap());
			
			mondixer.addRelation("fooUnary", new HashSet<ImmutableMapRow>(), Arrays.asList("XCol"));
			mondixer.addRow("fooUnary", new ImmutableMapRow(ImmutableMap.of("XCol", "x1")));
			mondixer.addRow("fooUnary", new ImmutableMapRow(ImmutableMap.of("XCol", "x2")));
			
			
			mondixer.addRelation("fooBinary", new HashSet<ImmutableMapRow>(), Arrays.asList("XCol", "YCol"));
			mondixer.addRow("fooBinary", new ImmutableMapRow(ImmutableMap.of("XCol", "x1", "YCol", "y1")));
			mondixer.addRow("fooBinary", new ImmutableMapRow(ImmutableMap.of("XCol", "x1", "YCol", "y2")));
			mondixer.addRow("fooBinary", new ImmutableMapRow(ImmutableMap.of("XCol", "x2", "YCol", "y1")));
			mondixer.addRow("fooBinary", new ImmutableMapRow(ImmutableMap.of("XCol", "x2", "YCol", "y3")));
			mondixer.addRow("fooBinary", new ImmutableMapRow(ImmutableMap.of("XCol", "x3", "YCol", "y2")));
			mondixer.addRow("fooBinary", new ImmutableMapRow(ImmutableMap.of("XCol", "x3", "YCol", "y4")));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
