/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;

import org.jiemamy.dddbase.Entity;

/**
 * {@link Entity}をID順に並べるコンパレータ。
 * 
 * @param <ID> IDの型
 * @version $Id$
 * @author daisuke
 */
public class EntityComparator<ID extends Comparable<ID> & Serializable> implements Comparator<Entity> {
	
	/** singleton instance */
	public static final EntityComparator<UUID> INSTANCE = new EntityComparator<UUID>();
	
	
	private EntityComparator() {
	}
	
	public int compare(Entity o1, Entity o2) {
		return o1.getId().compareTo(o2.getId());
	}
	
}
