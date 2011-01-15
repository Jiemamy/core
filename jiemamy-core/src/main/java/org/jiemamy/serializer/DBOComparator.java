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

import org.jiemamy.model.DatabaseObjectModel;

/**
 * {@link DatabaseObjectModel}をID順に並べるコンパレータ。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DBOComparator implements Comparator<DatabaseObjectModel> {
	
	public int compare(DatabaseObjectModel o1, DatabaseObjectModel o2) {
		int nameCompare = o1.getClass().getName().compareTo(o2.getClass().getName());
		if (nameCompare == 0) {
			return o1.getId().compareTo(o2.getId());
		}
		return nameCompare;
	}
	
}