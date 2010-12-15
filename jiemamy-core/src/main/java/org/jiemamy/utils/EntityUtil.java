/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.jiemamy.Entity;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public final class EntityUtil {
	
	public static <E extends Entity>HashSet<E> cloneEntityHashSet(Collection<E> collection) {
		HashSet<E> cloneCollection = Sets.newHashSetWithExpectedSize(collection.size());
		for (E element : collection) {
			@SuppressWarnings("unchecked")
			E cloneElement = (E) element.clone();
			cloneCollection.add(cloneElement);
		}
		return cloneCollection;
	}
	
	public static <E extends Entity>LinkedHashSet<E> cloneEntityLinkedHashSet(Collection<E> collection) {
		LinkedHashSet<E> cloneCollection = Sets.newLinkedHashSet();
		for (E element : collection) {
			@SuppressWarnings("unchecked")
			E cloneElement = (E) element.clone();
			cloneCollection.add(cloneElement);
		}
		return cloneCollection;
	}
	
	public static <E extends Entity>ArrayList<E> cloneEntityList(Collection<E> collection) {
		ArrayList<E> cloneCollection = Lists.newArrayListWithExpectedSize(collection.size());
		for (E element : collection) {
			@SuppressWarnings("unchecked")
			E cloneElement = (E) element.clone();
			cloneCollection.add(cloneElement);
		}
		return cloneCollection;
	}
	
	public static <K, V extends Entity>HashMap<K, V> cloneEntityMap(Map<K, V> map) {
		HashMap<K, V> cloneMap = Maps.newHashMapWithExpectedSize(map.size());
		for (Entry<K, V> element : map.entrySet()) {
			@SuppressWarnings("unchecked")
			V cloneValue = (V) element.getValue().clone();
			cloneMap.put(element.getKey(), cloneValue);
		}
		return cloneMap;
	}
	
	public static <E>ArrayList<E> cloneValueList(Collection<E> collection) {
		return Lists.newArrayList(collection);
	}
	
	private EntityUtil() {
	}
}
