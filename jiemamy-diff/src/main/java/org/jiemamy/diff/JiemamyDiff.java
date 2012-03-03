/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2012/01/23
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
package org.jiemamy.diff;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.table.JmTable;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyDiff {
	
	private final JiemamyContext ctxLeft;
	
	private final JiemamyContext ctxRight;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param ctxLeft 
	 * @param ctxRight 
	 */
	public JiemamyDiff(JiemamyContext ctxLeft, JiemamyContext ctxRight) {
		Validate.notNull(ctxLeft);
		Validate.notNull(ctxRight);
		this.ctxLeft = ctxLeft;
		this.ctxRight = ctxRight;
	}
	
	public List<DiffElement> diff() {
		List<DiffElement> result = Lists.newArrayList();
		Set<DbObject> objSetLeft = ctxLeft.getDbObjects();
		Set<DbObject> objSetRight = ctxRight.getDbObjects();
		
		FooBar<DbObject> foobar = foobar(objSetLeft, objSetRight);
		for (DbObject remove : foobar.removes) {
			if (remove instanceof JmTable == false) {
				continue;
			}
			result.add(new RemoveTable((JmTable) remove));
		}
		for (DbObject add : foobar.adds) {
			if (add instanceof JmTable == false) {
				continue;
			}
			result.add(new AddTable((JmTable) add));
		}
		for (Pair<DbObject> p1 : foobar.pairs) {
			if (p1.left instanceof JmTable == false) {
				continue;
			}
			JmTable leftTable = (JmTable) p1.left;
			JmTable rightTable = (JmTable) p1.right;
			
			List<JmColumn> leftColumns = leftTable.getColumns();
			List<JmColumn> rightColumns = rightTable.getColumns();
			FooBar<JmColumn> foobarColumn = foobar(leftColumns, rightColumns);
			for (JmColumn remove : foobarColumn.removes) {
				result.add(new RemoveColumn(leftTable, remove));
			}
			for (JmColumn add : foobarColumn.adds) {
				result.add(new AddColumn(leftTable, add));
			}
			for (Pair<JmColumn> p2 : foobarColumn.pairs) {
				JmColumn leftColumn = p2.left;
				JmColumn rightColumn = p2.right;
				if (leftColumn.getDataType().equals(rightColumn.getDataType()) == false) {
					result.add(new ChangeDataType(leftTable, leftColumn, rightColumn.getDataType()));
				}
				if (leftColumn.getName().equals(rightColumn.getName()) == false) {
					result.add(new RenameColumn(leftTable, leftColumn, rightColumn.getName()));
				}
			}
			if (leftTable.getName().equals(rightTable.getName()) == false) {
				result.add(new RenameTable(leftTable, rightTable.getName()));
			}
		}
		
		return result;
	}
	
	private <T>FooBar<T> foobar(Collection<T> objSetLeft, Collection<T> objSetRight) {
		FooBar<T> result = new FooBar<T>();
		
		for (T objLeft : objSetLeft) {
			if (objSetRight.contains(objLeft)) {
				T objRight = null;
				for (T o : objSetRight) {
					if (o.equals(objLeft)) {
						objRight = o;
						break;
					}
				}
				result.pairs.add(new Pair<T>(objLeft, objRight));
			} else {
				result.removes.add(objLeft);
			}
		}
		for (T objRight : objSetRight) {
			if (objSetLeft.contains(objRight) == false) {
				result.adds.add(objRight);
			}
		}
		
		return result;
	}
	
	
	private static class FooBar<T> {
		
		List<Pair<T>> pairs = Lists.newArrayList();
		
		List<T> adds = Lists.newArrayList();
		
		List<T> removes = Lists.newArrayList();
	}
	
	private static class Pair<T> {
		
		final T left;
		
		final T right;
		
		
		Pair(T left, T right) {
			this.left = left;
			this.right = right;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Pair<?> other = (Pair<?>) obj;
			if (left == null) {
				if (other.left != null) {
					return false;
				}
			} else if (!left.equals(other.left)) {
				return false;
			}
			if (right == null) {
				if (other.right != null) {
					return false;
				}
			} else if (!right.equals(other.right)) {
				return false;
			}
			return true;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((left == null) ? 0 : left.hashCode());
			result = prime * result + ((right == null) ? 0 : right.hashCode());
			return result;
		}
	}
}
