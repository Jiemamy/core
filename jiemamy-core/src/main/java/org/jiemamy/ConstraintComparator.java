/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections15.list.UnmodifiableList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.model.attribute.constraint.CheckConstraintModel;
import org.jiemamy.model.attribute.constraint.ConstraintModel;
import org.jiemamy.model.attribute.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.attribute.constraint.UniqueKeyConstraintModel;
import org.jiemamy.model.dbo.TableModel;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * 属性の出力順を整列させるコンパレータ。
 * 
 * <p>PK, UK, FK, CHECKの順にならべ、同種内では元の順序を維持する。</p>
 * 
 * @author daisuke
 */
public class ConstraintComparator implements Comparator<ConstraintModel> {
	
	static final List<Class<? extends ConstraintModel>> ORDER;
	
	static {
		List<Class<? extends ConstraintModel>> order = CollectionsUtil.newArrayList();
		order.add(PrimaryKeyConstraintModel.class);
		order.add(UniqueKeyConstraintModel.class);
		order.add(ForeignKeyConstraintModel.class);
		order.add(CheckConstraintModel.class);
		ORDER = UnmodifiableList.decorate(order);
	}
	

	private static int getOrder(Class<?>[] interfaces) {
		for (Class<?> interf : interfaces) {
			int index = ORDER.indexOf(interf);
			if (index != ArrayUtils.INDEX_NOT_FOUND) {
				return index;
			}
		}
		return -1;
	}
	

	private final List<ConstraintModel> originalOrder;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param tableModel ソート対象の属性が保持されているテーブル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ConstraintComparator(TableModel tableModel) {
		Validate.notNull(tableModel);
		originalOrder = CollectionsUtil.newArrayList(tableModel.getConstraints());
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
		int i1 = getOrder(o1.getClass().getInterfaces());
		int i2 = getOrder(o2.getClass().getInterfaces());
		if (i1 != i2) {
			return i1 - i2;
		}
		
		return originalOrder.indexOf(o1) - originalOrder.indexOf(o2);
	}
}
