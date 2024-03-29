/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import java.util.Comparator;

import org.jiemamy.dddbase.EntityRef;

/**
 * {@link EntityRef}をreferentのID順にするコンパレータ。
 * 
 * @version $Id$
 * @author daisuke
 */
public class EntityRefComparator implements Comparator<EntityRef<?>> {
	
	/** singleton instance */
	public static final EntityRefComparator INSTANCE = new EntityRefComparator();
	
	
	private EntityRefComparator() {
	}
	
	public int compare(EntityRef<?> o1, EntityRef<?> o2) {
		return o1.getReferentId().compareTo(o2.getReferentId());
	}
}
