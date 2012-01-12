/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/10
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JmMetadata;
import org.jiemamy.SqlFacet;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.ColumnParameterKey;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.constraint.JmConstraint;
import org.jiemamy.model.constraint.JmDeferrability;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmNotNullConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.JmUniqueKeyConstraint;
import org.jiemamy.model.dataset.JmDataSet;
import org.jiemamy.model.dataset.JmRecord;
import org.jiemamy.model.datatype.DataType;
import org.jiemamy.model.datatype.LiteralType;
import org.jiemamy.model.index.JmIndex;
import org.jiemamy.model.index.JmIndexColumn;
import org.jiemamy.model.index.JmIndexColumn.SortOrder;
import org.jiemamy.model.parameter.ParameterKey;
import org.jiemamy.model.script.JmAroundScript;
import org.jiemamy.model.script.Position;
import org.jiemamy.model.sql.Identifier;
import org.jiemamy.model.sql.Keyword;
import org.jiemamy.model.sql.Literal;
import org.jiemamy.model.sql.Separator;
import org.jiemamy.model.sql.SimpleSqlStatement;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.model.sql.Token;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.view.JmView;
import org.jiemamy.script.ScriptException;
import org.jiemamy.script.ScriptString;
import org.jiemamy.utils.ConstraintComparator;
import org.jiemamy.utils.DbObjectDependencyCalculator;
import org.jiemamy.utils.collection.ListUtil;

/**
 * {@link SqlEmitter}の標準実装クラス。
 * 
 * @author daisuke
 */
public class DefaultSqlEmitter implements SqlEmitter {
	
	private final TokenResolver tokenResolver;
	
	private static Logger logger = LoggerFactory.getLogger(DefaultSqlEmitter.class);
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect {@link Dialect}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DefaultSqlEmitter(Dialect dialect) {
		this(dialect, new DefaultTokenResolver());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param dialect {@link Dialect}
	 * @param tokenResolver {@link TokenResolver}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected DefaultSqlEmitter(Dialect dialect, TokenResolver tokenResolver) {
		Validate.notNull(dialect);
		Validate.notNull(tokenResolver);
		this.tokenResolver = tokenResolver;
	}
	
	public List<SqlStatement> emit(JiemamyContext context, EmitConfig config) {
		Validate.notNull(context);
		Validate.notNull(config);
		Validate.isTrue(context.hasFacet(SqlFacet.class));
		Validate.isTrue(config.getDataSetIndex() < context.getDataSets().size());
		List<SqlStatement> result = Lists.newArrayList();
		
		// THINK RootBeginScript と CreateSchemaStatement、どっちが先？
		
		emitScript(context, Position.BEGIN, result);
		
		JmMetadata metadata = context.getMetadata();
		if (metadata != null && StringUtils.isEmpty(metadata.getSchemaName()) == false
				&& config.emitCreateSchemaStatement()) {
			if (config.emitDropStatements()) {
				result.add(emitDropSchemaStatement(metadata.getSchemaName()));
			}
			
			result.add(emitCreateSchemaStatement(metadata.getSchemaName()));
		}
		
		if (config.emitDropStatements()) {
			List<DbObject> dropList = DbObjectDependencyCalculator.getSortedEntityList(context);
			ListUtil.reverse(dropList);
			for (DbObject dbObject : dropList) {
				result.add(emitDropDbObjectStatement(dbObject));
			}
		}
		
		for (DbObject dbObject : DbObjectDependencyCalculator.getSortedEntityList(context)) {
			Boolean disabled = dbObject.getParam(ParameterKey.DISABLED);
			if (disabled != null && disabled) {
				continue;
			}
			
			emitScript(context, dbObject, Position.BEGIN, result);
			result.add(emitCreateDbObjectStatement(context, dbObject));
			emitScript(context, dbObject, Position.END, result);
		}
		
		int dataSetIndex = config.getDataSetIndex();
		if (dataSetIndex >= 0 && dataSetIndex < context.getDataSets().size()) {
			JmDataSet dataSet = context.getDataSets().get(dataSetIndex);
			
			result.add(new SimpleSqlStatement(Keyword.of("BEGIN"), Separator.SEMICOLON));
			for (DbObject dbObject : DbObjectDependencyCalculator.getSortedEntityList(context)) {
				Boolean disabled = dbObject.getParam(ParameterKey.DISABLED);
				if (disabled != null && disabled) {
					continue;
				}
				if (dbObject instanceof JmTable) {
					JmTable table = (JmTable) dbObject;
					List<JmRecord> records = dataSet.getRecords().get(table.toReference());
					if (records != null) {
						for (JmRecord record : records) {
							result.add(emitInsertStatement(context, table, record));
						}
					}
				}
				
			}
			result.add(new SimpleSqlStatement(Keyword.of("COMMIT"), Separator.SEMICOLON));
		}
		
		emitScript(context, Position.END, result);
		
		return result;
	}
	
	/**
	 * カラム定義を出力する。
	 * 
	 * @param context コンテキスト
	 * @param table テーブル
	 * @param column カラム
	 * @param tokenResolver {@link TokenResolver}
	 * @return トークンシーケンス
	 */
	protected List<Token> emitColumn(JiemamyContext context, JmTable table, JmColumn column, TokenResolver tokenResolver) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Identifier.of(column.getName()));
		tokens.addAll(tokenResolver.resolve(column.getDataType()));
		
		if (StringUtils.isEmpty(column.getDefaultValue()) == false) {
			tokens.add(Keyword.DEFAULT);
			tokens.add(Literal.of(column.getDefaultValue(), LiteralType.FRAGMENT));
		}
		
		JmNotNullConstraint nn = table.getNotNullConstraintFor(column.toReference());
		if (nn != null) {
			if (StringUtils.isEmpty(nn.getName()) == false) {
				tokens.add(Keyword.CONSTRAINT);
				tokens.add(Identifier.of(nn.getName()));
			}
			tokens.add(Keyword.NOT);
			tokens.add(Keyword.NULL);
		}
		
		return tokens;
	}
	
	/**
	 * DDLを出力する。
	 * 
	 * @param context コンテキスト
	 * @param dbObject 対象{@link DbObject}
	 * @return DDL
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitCreateDbObjectStatement(JiemamyContext context, DbObject dbObject) {
		Validate.notNull(dbObject);
		DbObjectEmitStrategy strategy = DbObjectEmitStrategy.fromDbObject(dbObject);
		if (strategy == null) {
			// TODO くるしまぎれｗ
			logger.warn("strategy for {} is not found.", dbObject.getClass().getName());
			return new SimpleSqlStatement(Keyword.of("-- " + dbObject.toString()));
		}
		return strategy.emit(context, dbObject, this, tokenResolver);
	}
	
	/**
	 * CREATE INDEX文を出力する。
	 * 
	 * @param context コンテキスト
	 * @param table インデックスをつけるテーブル
	 * @param index 対象インデックス
	 * @return CREATE INDEX文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitCreateIndexStatement(JiemamyContext context, JmTable table, JmIndex index) {
		Validate.notNull(table);
		Validate.notNull(index);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.CREATE);
		if (index.isUnique()) {
			tokens.add(Keyword.UNIQUE);
		}
		tokens.add(Keyword.INDEX);
		tokens.add(Identifier.of(index.getName()));
		tokens.add(Keyword.ON);
		tokens.add(Identifier.of(table.getName()));
		tokens.add(Separator.LEFT_PAREN);
		
		List<JmIndexColumn> indexColumns = index.getIndexColumns();
		for (JmIndexColumn indexColumn : indexColumns) {
			tokens.addAll(emitIndexColumnClause(context, indexColumn));
			tokens.add(Separator.COMMA);
		}
		
		if (index.getIndexColumns().isEmpty() == false) {
			tokens.remove(tokens.size() - 1);
		}
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Separator.SEMICOLON);
		return new SimpleSqlStatement(tokens);
	}
	
	/**
	 * CREATE SCHEMA文を出力する。
	 * 
	 * @param schemaName スキーマ名
	 * @return CREATE SCHEMA文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitCreateSchemaStatement(String schemaName) {
		Validate.notNull(schemaName);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.CREATE);
		tokens.add(Keyword.SCHEMA);
		tokens.add(Identifier.of(schemaName));
		tokens.add(Separator.SEMICOLON);
		return new SimpleSqlStatement(tokens);
	}
	
	/**
	 * {@link DbObject}のDROP文を出力する。
	 * 
	 * @param dbObject DROP対象{@link DbObject}
	 * @return DROP文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitDropDbObjectStatement(DbObject dbObject) {
		Validate.notNull(dbObject);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.DROP);
		tokens.addAll(tokenResolver.resolve(dbObject));
		tokens.add(Identifier.of(dbObject.getName()));
		tokens.add(Separator.SEMICOLON);
		return new SimpleSqlStatement(tokens);
	}
	
	/**
	 * インデックスのDROP文を出力する。
	 * 
	 * @param index 対象インデックス
	 * @return DROP文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitDropIndexStatement(JmIndex index) {
		Validate.notNull(index);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.DROP);
		tokens.add(Keyword.INDEX);
		tokens.add(Identifier.of(index.getName()));
		tokens.add(Separator.SEMICOLON);
		return new SimpleSqlStatement(tokens);
	}
	
	/**
	 * スキーマのDROP文を出力する。
	 * 
	 * @param schemaName スキーマ名
	 * @return DROP文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitDropSchemaStatement(String schemaName) {
		Validate.notNull(schemaName);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.DROP);
		tokens.add(Keyword.SCHEMA);
		tokens.add(Identifier.of(schemaName));
		tokens.add(Separator.SEMICOLON);
		return new SimpleSqlStatement(tokens);
	}
	
	/**
	 * インデックスカラムの定義句を出力する。
	 * 
	 * @param context コンテキスト
	 * @param indexColumn 対象インデックスカラム
	 * @return トークンシーケンス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected List<Token> emitIndexColumnClause(JiemamyContext context, JmIndexColumn indexColumn) {
		Validate.notNull(context);
		Validate.notNull(indexColumn);
		List<Token> tokens = Lists.newArrayList();
		UUIDEntityRef<? extends JmColumn> columnRef = indexColumn.getColumnRef();
		JmColumn column = context.resolve(columnRef);
		tokens.add(Identifier.of(column.getName()));
		SortOrder sortOrder = indexColumn.getSortOrder();
		tokens.addAll(tokenResolver.resolve(sortOrder));
		return tokens;
	}
	
	/**
	 * INSERT文を出力する。
	 * 
	 * @param context コンテキスト
	 * @param table 対象テーブル
	 * @param record 対象レコード
	 * @return INSERT文
	 */
	protected SqlStatement emitInsertStatement(JiemamyContext context, JmTable table, JmRecord record) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.INSERT);
		tokens.add(Keyword.INTO);
		tokens.add(Identifier.of(table.getName()));
		
		List<Token> columnList = Lists.newArrayList();
		List<Token> dataList = Lists.newArrayList();
		columnList.add(Separator.LEFT_PAREN);
		dataList.add(Separator.LEFT_PAREN);
		
		List<JmColumn> columns = table.getColumns();
		int size = 0;
		for (JmColumn column : columns) {
			if (record.getValues().containsKey(column.toReference()) == false) {
				continue;
			}
			
			DataType dataType = column.getDataType();
			
			String value;
			try {
				ScriptString ss = record.getValues().get(column.toReference());
				Map<String, Object> env = Maps.newHashMap();
				// TODO env-objectを整備
				value = ss.process(env);
			} catch (ClassNotFoundException e) {
				logger.error("", e);
				continue;
			} catch (ScriptException e) {
				logger.error("", e);
				continue;
			}
			
			Literal dataLiteral;
			if (value == null) {
				dataLiteral = Literal.NULL;
			} else {
				dataLiteral = Literal.of(value, dataType.getRawTypeDescriptor().getCategory().getLiteralType());
			}
			
			columnList.add(Identifier.of(column.getName()));
			columnList.add(Separator.COMMA);
			
			dataList.add(dataLiteral);
			dataList.add(Separator.COMMA);
			size++;
		}
		if (size > 0) {
			columnList.remove(columnList.size() - 1);
			dataList.remove(dataList.size() - 1);
		}
		columnList.add(Separator.RIGHT_PAREN);
		dataList.add(Separator.RIGHT_PAREN);
		
		tokens.addAll(columnList);
		tokens.add(Keyword.VALUES);
		tokens.addAll(dataList);
		tokens.add(Separator.SEMICOLON);
		
		return new SimpleSqlStatement(tokens);
	}
	
	private void emitScript(JiemamyContext context, DbObject dbObject, Position position, List<SqlStatement> result) {
		SqlFacet facet = context.getFacet(SqlFacet.class);
		JmAroundScript aroundScript = facet.getAroundScriptFor(dbObject.toReference());
		
		if (aroundScript == null) {
			return;
		}
		
		try {
			String beginScriptResilt = aroundScript.process(context, position, dbObject);
			if (StringUtils.isEmpty(beginScriptResilt) == false) {
				result.add(new SimpleSqlStatement(Literal.of(beginScriptResilt, LiteralType.FRAGMENT)));
			}
		} catch (ClassNotFoundException e) {
			result.add(new SimpleSqlStatement(Literal.of(
					"-- ERROR: Cannot resolve " + aroundScript.getScriptEngineClassName(position) + "\n",
					LiteralType.FRAGMENT)));
		} catch (ScriptException e) {
			result.add(new SimpleSqlStatement(Literal.of(
					"-- ERROR: script execution failed " + aroundScript.getScriptEngineClassName(position) + "\n",
					LiteralType.FRAGMENT)));
		}
	}
	
	private void emitScript(JiemamyContext context, Position position, List<SqlStatement> result) {
		SqlFacet facet = context.getFacet(SqlFacet.class);
		JmAroundScript aroundScript = facet.getUniversalAroundScript();
		
		if (aroundScript == null) {
			return;
		}
		
		try {
			String beginScriptResilt = aroundScript.process(context, position, context);
			if (StringUtils.isEmpty(beginScriptResilt) == false) {
				result.add(new SimpleSqlStatement(Literal.of(beginScriptResilt, LiteralType.FRAGMENT)));
			}
		} catch (ClassNotFoundException e) {
			result.add(new SimpleSqlStatement(Literal.of(
					"-- ERROR: Cannot resolve " + aroundScript.getScriptEngineClassName(position) + "\n",
					LiteralType.FRAGMENT)));
		} catch (ScriptException e) {
			result.add(new SimpleSqlStatement(Literal.of(
					"-- ERROR: script execution failed " + aroundScript.getScriptEngineClassName(position) + "\n",
					LiteralType.FRAGMENT)));
		}
	}
	
	
	/**
	 * 制約の出力戦略列挙型。
	 * 
	 * @author daisuke
	 */
	protected enum ConstraintEmitStrategy {
		
		/** 主キーの出力戦略 */
		PK(JmPrimaryKeyConstraint.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, JmConstraint constraint, TokenResolver tokenResolver) {
				JmPrimaryKeyConstraint primaryKey = (JmPrimaryKeyConstraint) constraint;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(primaryKey, tokens);
				tokens.add(Keyword.PRIMARY);
				tokens.add(Keyword.KEY);
				addColumnList(context, tokens, primaryKey.getKeyColumns());
				return tokens;
			}
			
		},
		
		/** ユニークキーの出力戦略 */
		UK(JmUniqueKeyConstraint.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, JmConstraint constraint, TokenResolver tokenResolver) {
				JmUniqueKeyConstraint uniqueKey = (JmUniqueKeyConstraint) constraint;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(uniqueKey, tokens);
				tokens.add(Keyword.UNIQUE);
				tokens.add(Keyword.KEY);
				addColumnList(context, tokens, uniqueKey.getKeyColumns());
				return tokens;
			}
			
		},
		
		/** 外部キーの出力戦略 */
		FK(JmForeignKeyConstraint.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, JmConstraint constraint, TokenResolver tokenResolver) {
				JmForeignKeyConstraint foreignKey = (JmForeignKeyConstraint) constraint;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(foreignKey, tokens);
				tokens.add(Keyword.FOREIGN);
				tokens.add(Keyword.KEY);
				addColumnList(context, tokens, foreignKey.getKeyColumns());
				tokens.add(Keyword.REFERENCES);
				
				JmTable referenceEntity = foreignKey.findReferenceTable(context.getTables());
				tokens.add(Identifier.of(referenceEntity.getName()));
				addColumnList(context, tokens, foreignKey.getReferenceColumns());
				
				if (foreignKey.getMatchType() != null) {
					tokens.addAll(tokenResolver.resolve(foreignKey.getMatchType()));
				}
				if (foreignKey.getOnDelete() != null) {
					tokens.add(Keyword.ON);
					tokens.add(Keyword.DELETE);
					tokens.addAll(tokenResolver.resolve(foreignKey.getOnDelete()));
				}
				if (foreignKey.getOnUpdate() != null) {
					tokens.add(Keyword.ON);
					tokens.add(Keyword.UPDATE);
					tokens.addAll(tokenResolver.resolve(foreignKey.getOnUpdate()));
				}
				if (foreignKey.getDeferrability() != null) {
					JmDeferrability deferrability = foreignKey.getDeferrability();
					if (deferrability.isDeferrable() == false) {
						tokens.add(Keyword.NOT);
					}
					tokens.add(Keyword.DEFERRABLE);
					if (deferrability.getInitiallyCheckTime() != null) {
						tokens.addAll(tokenResolver.resolve(deferrability.getInitiallyCheckTime()));
					}
				}
				return tokens;
			}
		},
		
		/** CHECK制約の出力戦略 */
		TABLE_CHECK(JmCheckConstraint.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, JmConstraint constraint, TokenResolver tokenResolver) {
				JmCheckConstraint checkConstraint = (JmCheckConstraint) constraint;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(checkConstraint, tokens);
				tokens.add(Keyword.CHECK);
				tokens.add(Separator.LEFT_PAREN);
				tokens.add(Literal.of(checkConstraint.getExpression(), LiteralType.FRAGMENT));
				tokens.add(Separator.RIGHT_PAREN);
				return tokens;
			}
			
		};
		
		/**
		 * 属性オブジェクトから、その出力戦略を取得する。
		 * 
		 * @param constraint 出力対象の属性
		 * @return 出力戦略
		 */
		public static ConstraintEmitStrategy fromAttribute(JmConstraint constraint) {
			for (ConstraintEmitStrategy strategy : values()) {
				if (strategy.clazz == constraint.getClass()) {
					return strategy;
				}
				for (Class<?> c : constraint.getClass().getInterfaces()) {
					if (strategy.clazz == c) {
						return strategy;
					}
				}
			}
			return null;
		}
		
		private static void addColumnList(JiemamyContext context, List<Token> tokens,
				List<UUIDEntityRef<? extends JmColumn>> columnRefs) {
			tokens.add(Separator.LEFT_PAREN);
			for (UUIDEntityRef<? extends JmColumn> columnRef : columnRefs) {
				JmColumn column = context.resolve(columnRef);
				tokens.add(Identifier.of(column.getName()));
				tokens.add(Separator.COMMA);
			}
			
			if (columnRefs.isEmpty() == false) {
				tokens.remove(tokens.size() - 1);
			}
			tokens.add(Separator.RIGHT_PAREN);
		}
		
		private static void addConstraintNameDefinition(JmConstraint constraint, List<Token> tokens) {
			if (StringUtils.isEmpty(constraint.getName()) == false) {
				tokens.add(Keyword.CONSTRAINT);
				tokens.add(Identifier.of(constraint.getName()));
			}
		}
		
		
		private final Class<? extends JmConstraint> clazz;
		
		
		ConstraintEmitStrategy(Class<? extends JmConstraint> clazz) {
			this.clazz = clazz;
		}
		
		/**
		 * 制約からトークンシーケンスを出力する。
		 * 
		 * @param constraint 制約
		 * @param tokenResolver トークンリゾルバ
		 * @param <T> 出力する制約モデルの型
		 * @param context コンテキスト
		 * @return トークンシーケンス
		 */
		public abstract <T extends JmConstraint>List<Token> emit(JiemamyContext context, T constraint,
				TokenResolver tokenResolver);
		
	}
	
	/**
	 * {@link DbObject}の出力戦略。
	 * 
	 * @author daisuke
	 */
	protected enum DbObjectEmitStrategy {
		
		/** テーブルの出力戦略 */
		TABLE(JmTable.class) {
			
			@Override
			public SqlStatement emit(JiemamyContext context, DbObject dbObject, DefaultSqlEmitter sqlEmitter,
					TokenResolver tokenResolver) {
				assert dbObject instanceof JmTable;
				JmTable table = (JmTable) dbObject;
				List<Token> tokens = Lists.newArrayList();
				tokens.add(Keyword.CREATE);
				tokens.add(Keyword.TABLE);
				tokens.add(Identifier.of(table.getName()));
				tokens.add(Separator.LEFT_PAREN);
				for (JmColumn column : table.getColumns()) {
					Boolean disabled = column.getParam(ColumnParameterKey.DISABLED);
					if (disabled != null && disabled) {
						continue;
					}
					List<Token> columnTokens = sqlEmitter.emitColumn(context, table, column, tokenResolver);
					tokens.addAll(columnTokens);
					tokens.add(Separator.COMMA);
				}
				
				List<JmConstraint> constraints = Lists.newArrayList(table.getConstraints());
				Collections.sort(constraints, ConstraintComparator.INSTANCE);
				for (JmConstraint constraint : constraints) {
//					Boolean disabled = constraint.getParam(ConstraintParameterKey.DISABLED);
//					if (disabled != null && disabled) {
//						continue;
//					}
					ConstraintEmitStrategy strategy = ConstraintEmitStrategy.fromAttribute(constraint);
					if (strategy != null) {
						List<Token> constraintTokens = strategy.emit(context, constraint, tokenResolver);
						tokens.addAll(constraintTokens);
						tokens.add(Separator.COMMA);
					}
				}
				
				if (table.getColumns().isEmpty() == false || table.getConstraints().isEmpty() == false) {
					tokens.remove(tokens.size() - 1);
				}
				tokens.add(Separator.RIGHT_PAREN);
				tokens.add(Separator.SEMICOLON);
				return new SimpleSqlStatement(tokens);
			}
			
		},
		
		/** ビューの出力戦略 */
		VIEW(JmView.class) {
			
			@Override
			public SqlStatement emit(JiemamyContext context, DbObject dbObject, DefaultSqlEmitter defaultSqlEmitter,
					TokenResolver tokenResolver) {
				assert dbObject instanceof JmView;
				JmView view = (JmView) dbObject;
				List<Token> tokens = Lists.newArrayList();
				tokens.add(Keyword.CREATE);
				tokens.add(Keyword.VIEW);
				tokens.add(Identifier.of(view.getName()));
				tokens.add(Keyword.AS);
				if (view.getDefinition().trim().endsWith(";")) {
					String definition = view.getDefinition().trim();
					definition = definition.substring(0, definition.length() - 1);
					tokens.add(Literal.of(definition, LiteralType.FRAGMENT));
				} else {
					tokens.add(Literal.of(view.getDefinition(), LiteralType.FRAGMENT));
				}
				tokens.add(Separator.SEMICOLON);
				return new SimpleSqlStatement(tokens);
			}
		};
		
		/**
		 * {@link DbObject}からその{@link DbObject}の出力戦略を取得する。
		 * 
		 * @param dbObject 出力対象の{@link DbObject}
		 * @return 出力戦略、マッチするものが見つからなかった場合は{@code null}
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		public static DbObjectEmitStrategy fromDbObject(DbObject dbObject) {
			Validate.notNull(dbObject);
			for (DbObjectEmitStrategy s : values()) {
				if (s.clazz == dbObject.getClass()) {
					return s;
				}
				for (Class<?> c : dbObject.getClass().getInterfaces()) {
					if (s.clazz == c) {
						return s;
					}
				}
			}
			return null;
		}
		
		
		private final Class<? extends DbObject> clazz;
		
		
		DbObjectEmitStrategy(Class<? extends DbObject> clazz) {
			this.clazz = clazz;
		}
		
		/**
		 * {@link DbObject}から{@link Token}列を出力する。
		 * 
		 * @param context コンテキスト
		 * @param dbObject {@link DbObject}
		 * @param sqlEmitter 利用している{@link SqlEmitter}のインスタンス
		 * @param tokenResolver トークンリゾルバ
		 * @return トークンシーケンス
		 */
		public abstract SqlStatement emit(JiemamyContext context, DbObject dbObject, DefaultSqlEmitter sqlEmitter,
				TokenResolver tokenResolver);
		
	}
}
