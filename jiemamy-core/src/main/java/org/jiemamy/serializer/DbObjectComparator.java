/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/15
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
package org.jiemamy.serializer;

import java.util.Comparator;

import org.jiemamy.model.DbObject;

/**
 * {@link DbObject}をID順に並べるコンパレータ。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DbObjectComparator implements Comparator<DbObject> {
	
	/** singleton instance */
	public static final DbObjectComparator INSTANCE = new DbObjectComparator();
	
	
	private DbObjectComparator() {
	}
	
	public int compare(DbObject o1, DbObject o2) {
		int classNameCompare = o1.getClass().getName().compareTo(o2.getClass().getName());
		return classNameCompare == 0 ? EntityComparator.INSTANCE.compare(o1, o2) : classNameCompare;
	}
	
}
