/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/20
 *
 * This file is part of Jiemamy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jiemamy.utils.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class MultiBidiMap<L, R> {
	
	final ListMultimap<L, R> forwardMap = Multimaps.newListMultimap(new HashMap<L, Collection<R>>(),
			new RightListSupplier());
	
	final ListMultimap<R, L> backMap = Multimaps.newListMultimap(new HashMap<R, Collection<L>>(),
			new LeftListSupplier());
	

	public List<L> getBack(R right) {
		return backMap.get(right);
	}
	
	public List<R> getForward(L left) {
		return forwardMap.get(left);
	}
	
	public synchronized void put(L left, R right) {
		forwardMap.put(left, right);
		backMap.put(right, left);
	}
	
	public synchronized int size() {
		assert forwardMap.size() == backMap.size();
		return forwardMap.size();
	}
	

	private final class LeftListSupplier implements Supplier<List<L>> {
		
		public List<L> get() {
			return Lists.newArrayList();
		}
	}
	
	private final class RightListSupplier implements Supplier<List<R>> {
		
		public List<R> get() {
			return Lists.newArrayList();
		}
	}
}
