/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/03/30
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
package org.jiemamy.dialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.SimpleDbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.SimpleJmColumn;
import org.jiemamy.model.constraint.SimpleJmNotNullConstraint;
import org.jiemamy.model.constraint.SimpleJmPrimaryKeyConstraint;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.SimpleJmTable;
import org.jiemamy.model.view.JmView;
import org.jiemamy.model.view.SimpleJmView;
import org.jiemamy.utils.sql.metadata.ColumnMeta;
import org.jiemamy.utils.sql.metadata.ColumnMeta.Nullable;
import org.jiemamy.utils.sql.metadata.PrimaryKeyMeta;
import org.jiemamy.utils.sql.metadata.TableMeta;
import org.jiemamy.utils.sql.metadata.TypeSafeDatabaseMetaData;
import org.jiemamy.utils.sql.metadata.TypeSafeResultSet;
import org.jiemamy.utils.visitor.AbstractCollectionVisitor;
import org.jiemamy.utils.visitor.AbstractTypeSafeResultSetVisitor;
import org.jiemamy.utils.visitor.ForEachUtil;

/**
 * {@link DbObjectImportVisitor}のデフォルト実装クラス。
 * 
 * @author daisuke
 */
public class DefaultDatabaseObjectImportVisitor extends AbstractCollectionVisitor<TableMeta, Void, SQLException>
		implements DbObjectImportVisitor {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultDatabaseObjectImportVisitor.class);
	
	private static final String[] DEFAULT_TYPES = new String[] {
		"TABLE",
		"VIEW"
	};
	
	/** DBメタデータ */
	private TypeSafeDatabaseMetaData meta;
	
	/** 書き込み先モデル */
	private JiemamyContext context;
	
	/**
	 * 読み込み対象のエンティティ型リスト
	 * 
	 * 例えば "TABLE" とか "VIEW" とか。
	 */
	private Collection<String> entityTypes;
	
	/**
	 * 読み込み対象のエンティティ名のリスト
	 * 
	 * ただし、空リストだった場合、全てのエンティティを読むことを意味する。
	 */
	private Collection<String> selectedEntities;
	
	private ParseMetadataConfig config;
	
	private final Dialect dialect;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect {@link Dialect}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultDatabaseObjectImportVisitor(Dialect dialect) {
		Validate.notNull(dialect);
		this.dialect = dialect;
	}
	
	public void intialize(TypeSafeDatabaseMetaData meta, JiemamyContext context, ParseMetadataConfig config) {
		Validate.notNull(meta);
		Validate.notNull(context);
		Validate.notNull(config);
		
		this.meta = meta;
		this.context = context;
		this.config = config;
		String[] entityTypesValue = config.getEntityTypes();
		if (entityTypesValue == null) {
			entityTypesValue = DEFAULT_TYPES;
		}
		entityTypes = Arrays.asList(entityTypesValue);
		String[] selectedEntitiesValue = config.getSelectedEntities();
		if (ArrayUtils.isEmpty(selectedEntitiesValue)) {
			selectedEntities = null;
		} else {
			selectedEntities = Arrays.asList(selectedEntitiesValue);
		}
	}
	
	public Void visit(TableMeta tableMeta) throws SQLException {
		Validate.notNull(tableMeta);
		if (entityTypes.contains(tableMeta.tableType)
				&& (selectedEntities == null || selectedEntities.contains(tableMeta.tableName))) {
			DbObject dbObject = createDbObject(tableMeta);
			if (dbObject != null) {
				context.store(dbObject);
			}
		}
		return null;
	}
	
	/**
	 * {@link DatabaseMetaData}から得られた結果を読み込み、{@link JmView}を生成する。
	 * 
	 * @param tableMeta 読み込み対象エンティティの情報
	 * @return 読み込んだ結果生成された{@link JmView}
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected DbObject createDbObject(TableMeta tableMeta) throws SQLException {
		Validate.notNull(tableMeta);
		SimpleDbObject result = null;
		
		logger.debug("type = " + tableMeta.tableType + "(" + tableMeta.tableName + ")");
		if ("TABLE".equals(tableMeta.tableType)) {
			result = createTable(tableMeta.tableName);
		} else if ("VIEW".equals(tableMeta.tableType)) {
			result = createView(tableMeta.tableName);
		}
		
		if (result == null) {
			return null;
		}
		
		if (StringUtils.isEmpty(tableMeta.remarks) == false) {
			result.setDescription(tableMeta.remarks);
		}
		return result;
	}
	
	/**
	 * {@link DatabaseMetaData}から得られる情報を読み込み、{@link JmTable}を生成する。
	 * 
	 * @param tableName 読み込み対象テーブル名
	 * @return 読み込んだ結果生成された{@link JmTable}
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SimpleJmTable createTable(String tableName) throws SQLException {
		Validate.notNull(tableName);
		
		SimpleJmTable table = new SimpleJmTable();
		table.setName(tableName);
		
		TypeSafeResultSet<ColumnMeta> columnsResult = null;
		TypeSafeResultSet<PrimaryKeyMeta> keysResult = null;
		try {
			columnsResult = getMeta().getColumns("", config.getSchema(), tableName, "%");
			ForEachUtil.accept(columnsResult, new ColumnMetaVisitor(table));
			keysResult = getMeta().getPrimaryKeys("", config.getSchema(), tableName);
			ForEachUtil.accept(keysResult, new PrimaryKeyMetaVisitor(table));
		} finally {
			closeQuietly(columnsResult);
			closeQuietly(keysResult);
		}
		return table;
	}
	
	/**
	 * {@link DatabaseMetaData}から得られる情報を読み込み、{@link JmView}を生成する。
	 * 
	 * @param viewName 読み込み対象ビュー名
	 * @return 読み込んだ結果生成された{@link JmView}
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SimpleJmView createView(String viewName) throws SQLException {
		Validate.notNull(viewName);
		// UNDONE ビューの定義SQLを取得する一般的な手段が分からない。
		// v0.1, v0.2では実は実装できなかった所。
		String definition = "VIEW DEFINITION (not implemented)";
		SimpleJmView view = new SimpleJmView();
		view.setName(viewName);
		view.setDefinition(definition);
		
		return view;
	}
	
	/**
	 * {@link TypeSafeDatabaseMetaData}を取得する。
	 * 
	 * @return {@link TypeSafeDatabaseMetaData}
	 */
	protected TypeSafeDatabaseMetaData getMeta() {
		return meta;
	}
	
	private void closeQuietly(TypeSafeResultSet<?> resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}
	

	private class ColumnMetaVisitor extends AbstractTypeSafeResultSetVisitor<ColumnMeta, List<JmColumn>, SQLException> {
		
		private final SimpleJmTable table;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param table テーブル
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		private ColumnMetaVisitor(SimpleJmTable table) {
			Validate.notNull(table);
			this.table = table;
		}
		
		public List<JmColumn> visit(ColumnMeta element) {
			Validate.notNull(element);
			
			SimpleJmColumn column = new SimpleJmColumn();
			column.setName(element.columnName);
			column.setDefaultValue(element.columnDef);
			
			RawTypeCategory category = RawTypeCategory.fromSqlType(element.dataType);
			SimpleRawTypeDescriptor typeRef = new SimpleRawTypeDescriptor(category, element.typeName);
			SimpleDataType dataType = new SimpleDataType(typeRef);
			Map<TypeParameterKey<?>, Necessity> typeParameterSpecs = dialect.getTypeParameterSpecs(typeRef);
			for (TypeParameterKey<?> key : typeParameterSpecs.keySet()) {
				if (key.equals(TypeParameterKey.SIZE)) {
					dataType.putParam(TypeParameterKey.SIZE, element.columnSize);
				} else if (key.equals(TypeParameterKey.PRECISION)) {
					dataType.putParam(TypeParameterKey.PRECISION, element.columnSize);
				} else if (key.equals(TypeParameterKey.SCALE)) {
					dataType.putParam(TypeParameterKey.SCALE, element.decimalDigits);
				}
			}
			column.setDataType(dataType);
			
			table.store(column);
			
			if (element.nullable == Nullable.NO_NULLS) {
				SimpleJmNotNullConstraint notNullConstraint = new SimpleJmNotNullConstraint();
				notNullConstraint.setColumn(column.toReference());
				table.store(notNullConstraint);
			}
			
			// TODO check制約のインポート
			
			finalResult.add(column);
			return null;
		}
		
		@Override
		protected void init() {
			finalResult = Lists.newArrayList();
		}
	}
	
	private static class PrimaryKeyMetaVisitor extends
			AbstractTypeSafeResultSetVisitor<PrimaryKeyMeta, Boolean, SQLException> {
		
		private SimpleJmPrimaryKeyConstraint primaryKey;
		
		private final SimpleJmTable table;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param table テーブル
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		private PrimaryKeyMetaVisitor(SimpleJmTable table) {
			Validate.notNull(table);
			this.table = table;
			primaryKey = (SimpleJmPrimaryKeyConstraint) table.getPrimaryKey();
		}
		
		public Boolean visit(PrimaryKeyMeta element) {
			Validate.notNull(element);
			
			if (primaryKey == null) {
				primaryKey = new SimpleJmPrimaryKeyConstraint();
			}
			
			for (JmColumn column : table.getColumns()) {
				if (column.getName().equals(element.columnName)) {
					primaryKey.addKeyColumn(column.toReference());
					break;
				}
			}
			table.store(primaryKey);
			return null;
		}
	}
	
}
