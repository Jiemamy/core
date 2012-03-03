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
package org.jiemamy.model.column;

import java.util.NoSuchElementException;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.AbstractOrderedEntity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.parameter.ParameterMap;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.TableNotFoundException;
import org.jiemamy.model.table.TooManyTablesFoundException;

/**
 * カラム定義モデル。Artemisにおける{@link JmColumn}の実装クラス。
 * 
 * @author daisuke
 */
public final class JmColumn extends AbstractOrderedEntity {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明文 */
	private String description;
	
	/** 型記述子 */
	private DataType dataType;
	
	/** デフォルト値 */
	private String defaultValue;
	
	private boolean notNull;
	
	private ParameterMap params = new ParameterMap();
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public JmColumn() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public JmColumn(UUID id) {
		super(id);
	}
	
	@Override
	public JmColumn clone() {
		JmColumn clone = (JmColumn) super.clone();
		clone.params = params.clone();
		return clone;
	}
	
	/**
	 * テーブルの集合の中からこのカラムが属するテーブルを返す。
	 * 
	 * @param tables 候補となるテーブルの集合
	 * @return このカラムが属するテーブル
	 * @throws TableNotFoundException 該当するテーブルが見つからなかった場合
	 * @throws TooManyTablesFoundException 該当するテーブルが複数見つかった場合
	 * @throws IllegalArgumentException 引数に{@code null}または{@code null}要素を与えた場合
	 */
	public JmTable findDeclaringTable(Iterable<? extends JmTable> tables) {
		Validate.notNull(tables);
		Iterable<? extends JmTable> c = Iterables.filter(tables, new Predicate<JmTable>() {
			
			public boolean apply(JmTable table) {
				Validate.notNull(table);
				return table.getColumns().contains(JmColumn.this);
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
	 * 型記述子を取得する。
	 * 
	 * @return 型記述子. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public DataType getDataType() {
		if (dataType == null) {
			return null;
		}
		return dataType.clone();
	}
	
	/**
	 * デフォルト値を取得する。
	 * 
	 * @return デフォルト値. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * 説明文を取得する。
	 * 
	 * @return 説明文. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 論理名を取得する。
	 * 
	 * @return 論理名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getLogicalName() {
		return logicalName;
	}
	
	/**
	 * 物理名を取得する。
	 * 
	 * @return 物理名. 未設定の場合は{@code null}
	 * @since 0.3
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * キーに対応するパラメータの値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return パラメータの値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>T getParam(ColumnParameterKey<T> key) {
		return params.get(key);
	}
	
	public ParameterMap getParams() {
		return params.clone();
	}
	
	/**
	 * somethingを取得する。 TODO for daisuke
	 * @return the notNull
	 */
	public boolean isNotNull() {
		return notNull;
	}
	
	/**
	 * パラメータを追加する。
	 * 
	 * @param key キー
	 * @param value 値
	 * @param <T> 値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>void putParam(ColumnParameterKey<T> key, T value) {
		params.put(key, value);
	}
	
	/**
	 * パラメータを削除する。
	 * 
	 * @param key キー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void removeParam(ColumnParameterKey<?> key) {
		params.remove(key);
	}
	
	/**
	 * データタイプを設定する
	 * 
	 * @param dataType データタイプ
	 * @since 0.3
	 */
	public void setDataType(DataType dataType) {
		if (dataType == null) {
			this.dataType = null;
		} else {
			this.dataType = dataType.clone();
		}
	}
	
	/**
	 * デフォルト値を設定する
	 * 
	 * @param defaultValue デフォルト値
	 * @since 0.3
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * 説明文を設定する。
	 * 
	 * @param description 説明文
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
	 * @param name 物理名
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param notNull the notNull to set
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	
	@Override
	public EntityRef<? extends JmColumn> toReference() {
		return new EntityRef<JmColumn>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{name=" + name + ", type=" + dataType + "}";
	}
	
	/**
	 * {@link ParameterMap} を取得する。
	 * 
	 * <p>このメソッドは内部で保持している {@link ParameterMap} オブジェクトの参照を返すことにより
	 * 内部表現を暴露していることに注意すること。</p>
	 * 
	 * @return {@link ParameterMap}の内部参照
	 */
	ParameterMap breachEncapsulationOfParams() {
		return params;
	}
}
