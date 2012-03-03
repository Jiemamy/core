/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.TableNotFoundException;
import org.jiemamy.model.table.TooManyTablesFoundException;

/**
 * {@link JmConstraint}のデフォルト抽象実装クラス。
 * 
 * @author daisuke
 */
public abstract class JmConstraint extends AbstractEntity {
	
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
	public JmConstraint(UUID id) {
		super(id);
	}
	
	@Override
	public JmConstraint clone() {
		JmConstraint clone = (JmConstraint) super.clone();
		return clone;
	}
	
	/**
	 * テーブルの集合の中からこの制約が属するテーブルを返す。
	 * 
	 * @param tables 候補となるテーブルの集合
	 * @return この制約が属するテーブル
	 * @throws TableNotFoundException 該当するテーブルが見つからなかった場合
	 * @throws TooManyTablesFoundException 該当するテーブルが複数見つかった場合
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public JmTable findDeclaringTable(Iterable<? extends JmTable> tables) {
		Validate.notNull(tables);
		Iterable<? extends JmTable> c = Iterables.filter(tables, new Predicate<JmTable>() {
			
			public boolean apply(JmTable table) {
				Validate.notNull(table);
				return table.getConstraints().contains(JmConstraint.this);
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
	
	/**
	 * 遅延評価可能性モデルを取得する。
	 * 
	 * @return 遅延評価可能性モデル、未設定の場合は{@code null}
	 * @since 0.3
	 */
	public JmDeferrability getDeferrability() {
		return deferrability;
	}
	
	/**
	 * 説明を取得する。
	 * 
	 * @return 説明、未設定の場合は{@code null}
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名。未設定の場合は{@code null}
	 */
	
	public String getLogicalName() {
		return logicalName;
	}
	
	/**
	 * 物理名を取得する。
	 * 
	 * @return 物理名、未設定の場合は{@code null}
	 * @since 0.3
	 */
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
	public EntityRef<? extends JmConstraint> toReference() {
		return new EntityRef<JmConstraint>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{name=" + name + "}";
	}
}
