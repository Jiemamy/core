/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/24
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
package org.jiemamy.model.constraint;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.column.ColumnModel;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class ForeignKey {
	
	private String name;
	
	private List<Referecing> referencings = Lists.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 */
	public ForeignKey() {
		// noop
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name キー名
	 */
	public ForeignKey(String name) {
		this.name = name;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @return
	 */
	public DefaultForeignKeyConstraintModel build() {
		return build(UUID.randomUUID());
	}
	
	public DefaultForeignKeyConstraintModel build(UUID id) {
		Validate.notNull(id);
		DefaultForeignKeyConstraintModel fk = new DefaultForeignKeyConstraintModel(id);
		fk.setName(name);
		for (Referecing referenceing : referencings) {
			fk.addReferencing(referenceing.from, referenceing.to);
		}
		return fk;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param from
	 * @param to
	 * @return this
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ForeignKey referencing(ColumnModel from, ColumnModel to) {
		Validate.notNull(from);
		Validate.notNull(to);
		return referencing(from.toReference(), to.toReference());
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @param from
	 * @param to
	 * @return this
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ForeignKey referencing(EntityRef<? extends ColumnModel> from, EntityRef<? extends ColumnModel> to) {
		Validate.notNull(from);
		Validate.notNull(to);
		referencings.add(new Referecing(from, to));
		return this;
	}
	

	private static class Referecing {
		
		private final EntityRef<? extends ColumnModel> from;
		
		private final EntityRef<? extends ColumnModel> to;
		

		Referecing(EntityRef<? extends ColumnModel> from, EntityRef<? extends ColumnModel> to) {
			this.from = from;
			this.to = to;
		}
		
	}
}
