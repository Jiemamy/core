/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/06/09
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

import java.util.NoSuchElementException;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.TableNotFoundException;
import org.jiemamy.model.table.TooManyTablesFoundException;

/**
 * {@link JmConstraint}のデフォルト抽象実装クラス。
 * 
 * @author daisuke
 */
public abstract class SimpleJmConstraint extends AbstractEntity implements JmConstraint {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明 */
	private String description;
	
	/** 遅延評価可能性モデル */
	private JmDeferrability deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public SimpleJmConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmConstraint clone() {
		SimpleJmConstraint clone = (SimpleJmConstraint) super.clone();
		return clone;
	}
	
	public JmTable findDeclaringTable(Iterable<? extends JmTable> tables) {
		Validate.notNull(tables);
		Iterable<? extends JmTable> c = Iterables.filter(tables, new Predicate<JmTable>() {
			
			public boolean apply(JmTable table) {
				Validate.notNull(table);
				return table.getConstraints().contains(SimpleJmConstraint.this);
			}
		});
		
		try {
			return Iterables.getOnlyElement(c);
		} catch (NoSuchElementException e) {
			throw new TableNotFoundException("contains " + this + " in " + tables);
		} catch (IllegalArgumentException e) {
			throw new TooManyTablesFoundException(c);
		}
	}
	
	public JmDeferrability getDeferrability() {
		return deferrability;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 遅延評価可能性モデルを設定する。
	 * 
	 * @param deferrability 遅延評価可能性モデル
	 */
	public void setDeferrability(JmDeferrability deferrability) {
		this.deferrability = deferrability;
	}
	
	/**
	 * 説明を設定する。
	 * 
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 論理名を設定する。
	 * 
	 * @param logicalName 論理名
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	/**
	 * 物理名を設定する。
	 * 
	 * @param name 物理名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public EntityRef<? extends SimpleJmConstraint> toReference() {
		return new DefaultEntityRef<SimpleJmConstraint>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{name=" + name + "}";
	}
}
