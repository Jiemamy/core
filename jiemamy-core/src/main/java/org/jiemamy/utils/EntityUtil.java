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

import com.google.common.collect.Lists;

import org.jiemamy.Entity;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public final class EntityUtil {
	
	public static <E extends Entity>ArrayList<E> cloneEntityList(Collection<E> org) {
		ArrayList<E> clone = Lists.newArrayListWithExpectedSize(org.size());
		for (E e : org) {
			clone.add(e);
		}
		return clone;
	}
	
	public static <E>ArrayList<E> cloneValueList(Collection<E> org) {
		return Lists.newArrayList(org);
	}
	
	private EntityUtil() {
	}
}
