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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.commons.lang.ArrayUtils;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.KeyConstraintModel;
import org.jiemamy.model.constraint.LocalKeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.UniqueKeyConstraintModel;
import org.jiemamy.model.constraint.ValueConstraintModel;

/**
 * 属性の出力順を整列させるコンパレータ。
 * 
 * <p>PK, UK, FK, CHECKの順にならべ、同種内では元の順序を維持する。</p>
 * 
 * @author daisuke
 */
public class ConstraintComparator implements Comparator<ConstraintModel> {
	
	private static final ImmutableList<Class<? extends ConstraintModel>> ORDER;
	
	static {
		List<Class<? extends ConstraintModel>> order = Lists.newArrayList();
		order.add(PrimaryKeyConstraintModel.class);
		order.add(UniqueKeyConstraintModel.class);
		order.add(LocalKeyConstraintModel.class);
		order.add(ForeignKeyConstraintModel.class);
		order.add(KeyConstraintModel.class);
		order.add(NotNullConstraintModel.class);
		order.add(CheckConstraintModel.class);
		order.add(ValueConstraintModel.class);
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
	
	public int compare(ConstraintModel o1, ConstraintModel o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		}
		
		if (o1 instanceof PrimaryKeyConstraintModel && o2 instanceof PrimaryKeyConstraintModel) {
			return 0;
		} else if (o1 instanceof UniqueKeyConstraintModel && o2 instanceof UniqueKeyConstraintModel) {
			UniqueKeyConstraintModel u1 = (UniqueKeyConstraintModel) o1;
			UniqueKeyConstraintModel u2 = (UniqueKeyConstraintModel) o2;
			
			List<EntityRef<? extends ColumnModel>> kc1 = u1.getKeyColumns();
			List<EntityRef<? extends ColumnModel>> kc2 = u2.getKeyColumns();
			Collections.sort(kc1, EntityRefComparator.INSTANCE);
			Collections.sort(kc2, EntityRefComparator.INSTANCE);
			if (kc1.equals(kc2)) {
				return 0;
			} else {
				int i1 = getOrder(getAncestorIntefaces(o1.getClass()));
				int i2 = getOrder(getAncestorIntefaces(o2.getClass()));
				if (i1 != i2) {
					return i1 - i2;
				} else {
					return o1.getId().compareTo(o2.getId());
				}
			}
		}
		
		int i1 = getOrder(getAncestorIntefaces(o1.getClass()));
		int i2 = getOrder(getAncestorIntefaces(o2.getClass()));
		if (i1 != -1 && i2 != -1) {
			if (i1 != i2) {
				return i1 - i2;
			}
		} else if (i1 == -1) {
			return 1;
		} else if (i2 == -1) {
			return -1;
		} else {
			return o1.getClass().getName().compareTo(o2.getClass().getName());
		}
		
		if (o1 instanceof ForeignKeyConstraintModel) {
			ForeignKeyConstraintModel f1 = (ForeignKeyConstraintModel) o1;
			ForeignKeyConstraintModel f2 = (ForeignKeyConstraintModel) o2;
			if (f1.getKeyColumns().equals(f2.getKeyColumns())
					&& f1.getReferenceColumns().equals(f2.getReferenceColumns())) {
				return 0;
			} else {
				return o1.getId().compareTo(o2.getId());
			}
		} else if (o1 instanceof NotNullConstraintModel && o2 instanceof NotNullConstraintModel) {
			NotNullConstraintModel n1 = (NotNullConstraintModel) o1;
			NotNullConstraintModel n2 = (NotNullConstraintModel) o2;
			return EntityRefComparator.INSTANCE.compare(n1.getColumnRef(), n2.getColumnRef());
		} else if (o1 instanceof CheckConstraintModel && o2 instanceof CheckConstraintModel) {
			CheckConstraintModel c1 = (CheckConstraintModel) o1;
			CheckConstraintModel c2 = (CheckConstraintModel) o2;
			return c1.getExpression().compareTo(c2.getExpression());
		}
		throw new IllegalArgumentException();
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
