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

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import org.apache.commons.lang.Validate;

import org.jiemamy.TableNotFoundException;
import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.model.table.TooManyTablesFoundException;

/**
 * {@link ConstraintModel}のデフォルト抽象実装クラス。
 * 
 * @author daisuke
 */
public abstract class DefaultConstraintModel extends AbstractEntity implements ConstraintModel {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明 */
	private String description;
	
	/** 遅延評価可能性モデル */
	private DeferrabilityModel deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultConstraintModel(UUID id) {
		super(id);
	}
	
	@Override
	public DefaultConstraintModel clone() {
		DefaultConstraintModel clone = (DefaultConstraintModel) super.clone();
		return clone;
	}
	
	public TableModel findDeclaringTable(Collection<? extends TableModel> tables) {
		Validate.noNullElements(tables);
		Collection<? extends TableModel> c = Collections2.filter(tables, new Predicate<TableModel>() {
			
			public boolean apply(TableModel tableModel) {
				return tableModel.getConstraints().contains(DefaultConstraintModel.this);
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
	
	public DeferrabilityModel getDeferrability() {
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
	public void setDeferrability(DeferrabilityModel deferrability) {
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
	public EntityRef<? extends DefaultConstraintModel> toReference() {
		return new DefaultEntityRef<DefaultConstraintModel>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{name=" + name + "}";
	}
}
