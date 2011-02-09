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
package org.jiemamy.model.column;

import java.util.NoSuchElementException;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.AbstractOrderedEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
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
public final class SimpleJmColumn extends AbstractOrderedEntity implements JmColumn {
	
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
	
	private ParameterMap params = new ParameterMap();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは{@code UUID.randomUUID()}を用いて自動生成する。</p>
	 */
	public SimpleJmColumn() {
		this(UUID.randomUUID());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public SimpleJmColumn(UUID id) {
		super(id);
	}
	
	@Override
	public SimpleJmColumn clone() {
		SimpleJmColumn clone = (SimpleJmColumn) super.clone();
		clone.params = params.clone();
		return clone;
	}
	
	public JmTable findDeclaringTable(Iterable<? extends JmTable> tables) {
		Validate.notNull(tables);
		Iterable<? extends JmTable> c = Iterables.filter(tables, new Predicate<JmTable>() {
			
			public boolean apply(JmTable table) {
				Validate.notNull(table);
				return table.getColumns().contains(SimpleJmColumn.this);
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
	
	public DataType getDataType() {
		if (dataType == null) {
			return null;
		}
		return dataType.clone();
	}
	
	public String getDefaultValue() {
		return defaultValue;
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
	
	public <T>T getParam(ColumnParameterKey<T> key) {
		return params.get(key);
	}
	
	public ParameterMap getParams() {
		return params.clone();
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
	
	@Override
	public EntityRef<? extends SimpleJmColumn> toReference() {
		return new DefaultEntityRef<SimpleJmColumn>(this);
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
