/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/06
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.commons.lang.ArrayUtils;

import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.constraint.JmConstraint;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmNotNullConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.JmUniqueKeyConstraint;

/**
 * 制約の出力順を整列させるコンパレータ。
 * 
 * <p>PK, UK, FK, CHECKの順にならべ、同種内では元の順序を維持する。</p>
 * 
 * @author daisuke
 */
public class ConstraintComparator implements Comparator<JmConstraint> {
	
	/** singleton instance */
	public static final ConstraintComparator INSTANCE = new ConstraintComparator();
	
	private static final ImmutableList<Class<? extends JmConstraint>> ORDER;
	
	static {
		List<Class<? extends JmConstraint>> order = Lists.newArrayList();
		order.add(JmPrimaryKeyConstraint.class);
		order.add(JmUniqueKeyConstraint.class);
//		order.add(JmLocalKeyConstraint.class);
		order.add(JmForeignKeyConstraint.class);
//		order.add(JmKeyConstraint.class);
		order.add(JmNotNullConstraint.class);
		order.add(JmCheckConstraint.class);
//		order.add(JmValueConstraint.class);
		ORDER = ImmutableList.copyOf(order);
	}
	
	
	private static int getOrder(Class<?>[] interfaces) {
		for (Class<?> interf : interfaces) {
			int index = ORDER.indexOf(interf);
			if (index != ArrayUtils.INDEX_NOT_FOUND) {
				return index;
			}
		}
		return ArrayUtils.INDEX_NOT_FOUND;
	}
	
	private ConstraintComparator() {
	}
	
	public int compare(JmConstraint o1, JmConstraint o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		}
		
		int i1 = getOrder(getAncestorIntefaces(o1.getClass()));
		int i2 = getOrder(getAncestorIntefaces(o2.getClass()));
		if (i1 != -1 && i2 != -1) {
			if (i1 != i2) {
				return i1 - i2;
			}
		} else if (i1 == -1 && i2 != -1) {
			return 1;
		} else if (i1 != -1 && i2 == -1) {
			return -1;
		}
		
		int compareName = o1.getClass().getName().compareTo(o2.getClass().getName());
		if (compareName != 0) {
			return compareName;
		}
		
		return o1.getId().compareTo(o2.getId());
	}
	
	private Class<?>[] getAncestorIntefaces(Class<?> clazz) {
		if (clazz == null) {
			return new Class<?>[0];
		}
		List<? super Class<?>> collector = Lists.newArrayList();
		Class<?>[] interfaces = clazz.getInterfaces();
		collector.addAll(Arrays.asList(interfaces));
		for (Class<?> iface : interfaces) {
			collector.addAll(Arrays.asList(getAncestorIntefaces(iface)));
		}
		if (clazz != Object.class) {
			collector.addAll(Arrays.asList(getAncestorIntefaces(clazz.getSuperclass())));
		}
		return collector.toArray(new Class<?>[collector.size()]);
	}
}
