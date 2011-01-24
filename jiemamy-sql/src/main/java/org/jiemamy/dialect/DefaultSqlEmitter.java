/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.EntityDependencyCalculator;
import org.jiemamy.JiemamyContext;
import org.jiemamy.SqlFacet;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.model.DatabaseObjectModel;
import org.jiemamy.model.column.ColumnModel;
import org.jiemamy.model.constraint.CheckConstraintModel;
import org.jiemamy.model.constraint.ConstraintModel;
import org.jiemamy.model.constraint.DeferrabilityModel;
import org.jiemamy.model.constraint.ForeignKeyConstraintModel;
import org.jiemamy.model.constraint.NotNullConstraintModel;
import org.jiemamy.model.constraint.PrimaryKeyConstraintModel;
import org.jiemamy.model.constraint.UniqueKeyConstraintModel;
import org.jiemamy.model.dataset.DataSetModel;
import org.jiemamy.model.dataset.RecordModel;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.LiteralType;
import org.jiemamy.model.datatype.TypeVariant;
import org.jiemamy.model.index.IndexColumnModel;
import org.jiemamy.model.index.IndexColumnModel.SortOrder;
import org.jiemamy.model.index.IndexModel;
import org.jiemamy.model.script.AroundScriptModel;
import org.jiemamy.model.script.Position;
import org.jiemamy.model.sql.DefaultSqlStatement;
import org.jiemamy.model.sql.Identifier;
import org.jiemamy.model.sql.Keyword;
import org.jiemamy.model.sql.Literal;
import org.jiemamy.model.sql.Separator;
import org.jiemamy.model.sql.SqlStatement;
import org.jiemamy.model.sql.Token;
import org.jiemamy.model.table.DefaultTableModel;
import org.jiemamy.model.table.TableModel;
import org.jiemamy.model.view.ViewModel;

/**
 * {@link SqlEmitter}の標準実装クラス。
 * 
 * @author daisuke
 */
public class DefaultSqlEmitter implements SqlEmitter {
	
	private final TokenResolver tokenResolver;
	

	/**
	 * インスタンスを生成する。
	 * 
	 */
	public DefaultSqlEmitter() {
		this(new DefaultTokenResolver());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param tokenResolver
	 */
	public DefaultSqlEmitter(TokenResolver tokenResolver) {
		this.tokenResolver = tokenResolver;
	}
	
	public List<SqlStatement> emit(JiemamyContext context, EmitConfig config) {
		Validate.notNull(context);
		Validate.notNull(config);
		Validate.isTrue(context.hasFacet(SqlFacet.class));
		List<SqlStatement> result = Lists.newArrayList();
		
		// THINK RootBeginScript と CreateSchemaStatement、どっちが先？
		
		emitScript(context, Position.BEGIN, result);
		
		if (StringUtils.isEmpty(context.getSchemaName()) == false && config.emitCreateSchemaStatement()) {
			if (config.emitDropStatements()) {
				result.add(emitDropSchemaStatement(context.getSchemaName()));
			}
			
			result.add(emitCreateSchemaStatement(context.getSchemaName()));
		}
		
		if (config.emitDropStatements()) {
			List<DatabaseObjectModel> dropList = EntityDependencyCalculator.getSortedEntityList(context);
			DatabaseObjectModel[] array = dropList.toArray(new DatabaseObjectModel[dropList.size()]);
			ArrayUtils.reverse(array);
			for (DatabaseObjectModel entityModel : array) {
				result.add(emitDropEntityStatement(entityModel));
			}
		}
		
		for (DatabaseObjectModel dom : EntityDependencyCalculator.getSortedEntityList(context)) {
//			if (entityModel.hasAdapter(Disablable.class)
//					&& Boolean.TRUE.equals(entityModel.getAdapter(Disablable.class).isDisabled())) {
//				continue;
//			}
			
			emitScript(context, dom, Position.BEGIN, result);
			
			result.add(emitCreateStatement(context, dom));
			
//			if (dom instanceof TableModel) {
//				TableModel tableModel = (TableModel) dom;
//				for (IndexModel indexModel : tableModel.getIndexes()) {
//					if (indexModel.hasAdapter(Disablable.class)
//							&& Boolean.TRUE.equals(indexModel.getAdapter(Disablable.class).isDisabled())) {
//						continue;
//					}
//					if (config.emitDropStatements()) {
//						result.add(emitDropIndexStatement(indexModel));
//					}
//					
//					result.add(emitCreateIndexStatement(context, tableModel, indexModel));
//				}
//			}
			
			emitScript(context, dom, Position.END, result);
		}
		
		int dataSetIndex = config.getDataSetIndex();
		if (dataSetIndex >= 0 && dataSetIndex < context.getDataSets().size()) {
			DataSetModel dataSetModel = context.getDataSets().get(dataSetIndex);
			
			result.add(new DefaultSqlStatement(Keyword.of("BEGIN"), Separator.SEMICOLON));
			for (DatabaseObjectModel entityModel : EntityDependencyCalculator.getSortedEntityList(context)) {
//				if (entityModel.hasAdapter(Disablable.class)
//						&& Boolean.TRUE.equals(entityModel.getAdapter(Disablable.class).isDisabled())) {
//					continue;
//				}
				if (entityModel instanceof TableModel) {
					TableModel tableModel = (TableModel) entityModel;
					List<RecordModel> records = dataSetModel.getRecords().get(tableModel.toReference());
					if (records != null) {
						for (RecordModel recordModel : records) {
							result.add(emitInsertStatement(context, tableModel, recordModel));
						}
					}
				}
				
			}
			result.add(new DefaultSqlStatement(Keyword.of("COMMIT"), Separator.SEMICOLON));
		}
		
		emitScript(context, Position.END, result);
		
		return result;
	}
	
	protected List<Token> emitColumn(JiemamyContext context, TableModel tableModel, ColumnModel columnModel,
			TokenResolver tokenResolver) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Identifier.of(columnModel.getName()));
		tokens.addAll(tokenResolver.resolve(columnModel.getDataType()));
		
		if (StringUtils.isEmpty(columnModel.getDefaultValue()) == false) {
			DataTypeCategory category = columnModel.getDataType().getTypeReference().getCategory();
			tokens.add(Keyword.DEFAULT);
			tokens.add(Literal.of(columnModel.getDefaultValue(), category.getLiteralType()));
		}
		
		NotNullConstraintModel nnModel = tableModel.getNotNullConstraintFor(columnModel.toReference());
		if (nnModel != null) {
			if (StringUtils.isEmpty(nnModel.getName()) == false) {
				tokens.add(Keyword.CONSTRAINT);
				tokens.add(Identifier.of(nnModel.getName()));
			}
			tokens.add(Keyword.NOT);
			tokens.add(Keyword.NULL);
		}
		
		return tokens;
	}
	
	/**
	 * CREATE INDEX文を出力する。
	 * 
	 * @param tableModel インデックスをつけるテーブル
	 * @param indexModel 対象インデックス
	 * @return CREATE INDEX文
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitCreateIndexStatement(JiemamyContext context, TableModel tableModel, IndexModel indexModel) {
		Validate.notNull(tableModel);
		Validate.notNull(indexModel);
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.CREATE);
		if (indexModel.isUnique()) {
			tokens.add(Keyword.UNIQUE);
		}
		tokens.add(Keyword.INDEX);
		tokens.add(Identifier.of(indexModel.getName()));
		tokens.add(Keyword.ON);
		tokens.add(Identifier.of(tableModel.getName()));
		tokens.add(Separator.LEFT_PAREN);
		
		List<IndexColumnModel> indexColumns = indexModel.getIndexColumns();
		for (IndexColumnModel indexColumnModel : indexColumns) {
			tokens.addAll(emitIndexColumnClause(context, indexColumnModel));
			tokens.add(Separator.COMMA);
		}
		
		if (indexModel.getIndexColumns().isEmpty() == false) {
			tokens.remove(tokens.size() - 1);
		}
		tokens.add(Separator.RIGHT_PAREN);
		tokens.add(Separator.SEMICOLON);
		return new DefaultSqlStatement(tokens);
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
		return new DefaultSqlStatement(tokens);
	}
	
	/**
	 * DDLを出力する。
	 * 
	 * @param dom 対象エンティティ
	 * @return DDL
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected SqlStatement emitCreateStatement(JiemamyContext context, DatabaseObjectModel dom) {
		Validate.notNull(dom);
		EntityEmitStrategy strategy = EntityEmitStrategy.fromEntity(dom);
		return strategy.emit(context, dom, this, tokenResolver);
	}
	
	/**
	 * エンティティのDROP文を出力する。
	 * 
	 * @param entityModel 対象エンティティ
	 * @return DROP文
	 */
	protected SqlStatement emitDropEntityStatement(DatabaseObjectModel entityModel) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.DROP);
		tokens.addAll(tokenResolver.resolve(entityModel));
		tokens.add(Identifier.of(entityModel.getName()));
		tokens.add(Separator.SEMICOLON);
		return new DefaultSqlStatement(tokens);
	}
	
	/**
	 * インデックスのDROP文を出力する。
	 * 
	 * @param indexModel 対象インデックス
	 * @return DROP文
	 */
	protected SqlStatement emitDropIndexStatement(IndexModel indexModel) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.DROP);
		tokens.add(Keyword.INDEX);
		tokens.add(Identifier.of(indexModel.getName()));
		tokens.add(Separator.SEMICOLON);
		return new DefaultSqlStatement(tokens);
	}
	
	/**
	 * スキーマのDROP文を出力する。
	 * 
	 * @param schemaName スキーマ名
	 * @return DROP文
	 */
	protected SqlStatement emitDropSchemaStatement(String schemaName) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.DROP);
		tokens.add(Keyword.SCHEMA);
		tokens.add(Identifier.of(schemaName));
		tokens.add(Separator.SEMICOLON);
		return new DefaultSqlStatement(tokens);
	}
	
	/**
	 * インデックスカラムの定義句を出力する。
	 * 
	 * @param indexColumnModel 対象インデックスカラム
	 * @return トークンシーケンス
	 */
	protected List<Token> emitIndexColumnClause(JiemamyContext context, IndexColumnModel indexColumnModel) {
		List<Token> tokens = Lists.newArrayList();
		EntityRef<? extends ColumnModel> columnRef = indexColumnModel.getColumnRef();
		ColumnModel columnModel = context.resolve(columnRef);
		tokens.add(Identifier.of(columnModel.getName()));
		SortOrder sortOrder = indexColumnModel.getSortOrder();
		tokens.addAll(tokenResolver.resolve(sortOrder));
		return tokens;
	}
	
	/**
	 * INSERT文を出力する。
	 * 
	 * @param tableModel 対象テーブル
	 * @param recordModel 対象レコード
	 * @return INSERT文
	 */
	protected SqlStatement emitInsertStatement(JiemamyContext context, TableModel tableModel, RecordModel recordModel) {
		List<Token> tokens = Lists.newArrayList();
		tokens.add(Keyword.INSERT);
		tokens.add(Keyword.INTO);
		tokens.add(Identifier.of(tableModel.getName()));
		
		List<Token> columnList = Lists.newArrayList();
		List<Token> dataList = Lists.newArrayList();
		columnList.add(Separator.LEFT_PAREN);
		dataList.add(Separator.LEFT_PAREN);
		
		List<ColumnModel> columns = tableModel.getColumns();
		for (ColumnModel columnModel : columns) {
			if (recordModel.getValues().containsKey(columnModel.toReference()) == false) {
				continue;
			}
			
			columnList.add(Identifier.of(columnModel.getName()));
			columnList.add(Separator.COMMA);
			
			TypeVariant dataType = columnModel.getDataType();
			
			String string = "";
			try {
				string =
						recordModel.getValues().get(columnModel.toReference())
							.process(context, new HashMap<String, Object>());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			dataList.add(Literal.of(string, dataType.getTypeReference().getCategory().getLiteralType()));
			dataList.add(Separator.COMMA);
		}
		columnList.remove(columnList.size() - 1);
		dataList.remove(dataList.size() - 1);
		columnList.add(Separator.RIGHT_PAREN);
		dataList.add(Separator.RIGHT_PAREN);
		
		tokens.addAll(columnList);
		tokens.add(Keyword.VALUES);
		tokens.addAll(dataList);
		tokens.add(Separator.SEMICOLON);
		
		return new DefaultSqlStatement(tokens);
	}
	
	private void emitScript(JiemamyContext context, DatabaseObjectModel dom, Position position,
			List<SqlStatement> result) {
		SqlFacet facet = context.getFacet(SqlFacet.class);
		AroundScriptModel aroundScript = facet.getAroundScriptFor(dom.toReference());
		
		if (aroundScript == null) {
			return;
		}
		
		try {
			String beginScriptResilt = aroundScript.process(context, position, dom);
			if (StringUtils.isEmpty(beginScriptResilt) == false) {
				result.add(new DefaultSqlStatement(Literal.of(beginScriptResilt, LiteralType.FRAGMENT)));
			}
		} catch (ClassNotFoundException e) {
			result.add(new DefaultSqlStatement(Literal.of(
					"-- ERROR: Cannot resolve " + aroundScript.getScriptEngineClassName(position) + "\n",
					LiteralType.FRAGMENT)));
		}
	}
	
	private void emitScript(JiemamyContext context, Position position, List<SqlStatement> result) {
		SqlFacet facet = context.getFacet(SqlFacet.class);
		AroundScriptModel aroundScript = facet.getUniversalAroundScript();
		
		if (aroundScript == null) {
			return;
		}
		
		try {
			String beginScriptResilt = aroundScript.process(context, position, context);
			if (StringUtils.isEmpty(beginScriptResilt) == false) {
				result.add(new DefaultSqlStatement(Literal.of(beginScriptResilt, LiteralType.FRAGMENT)));
			}
		} catch (ClassNotFoundException e) {
			result.add(new DefaultSqlStatement(Literal.of(
					"-- ERROR: Cannot resolve " + aroundScript.getScriptEngineClassName(position) + "\n",
					LiteralType.FRAGMENT)));
		}
	}
	

	/**
	 * 属性の出力戦略列挙型。
	 * 
	 * @author daisuke
	 */
	protected enum AttributeEmitStrategy {
		
		/** 主キーの出力戦略 */
		PK(PrimaryKeyConstraintModel.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, ConstraintModel attributeModel, TokenResolver tokenResolver) {
				PrimaryKeyConstraintModel primaryKey = (PrimaryKeyConstraintModel) attributeModel;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(primaryKey, tokens);
				tokens.add(Keyword.PRIMARY);
				tokens.add(Keyword.KEY);
				addColumnList(context, tokens, primaryKey.getKeyColumns());
				return tokens;
			}
			
		},
		
		/** ユニークキーの出力戦略 */
		UK(UniqueKeyConstraintModel.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, ConstraintModel attributeModel, TokenResolver tokenResolver) {
				UniqueKeyConstraintModel uniqueKey = (UniqueKeyConstraintModel) attributeModel;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(uniqueKey, tokens);
				tokens.add(Keyword.UNIQUE);
				tokens.add(Keyword.KEY);
				addColumnList(context, tokens, uniqueKey.getKeyColumns());
				return tokens;
			}
			
		},
		
		/** 外部キーの出力戦略 */
		FK(ForeignKeyConstraintModel.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, ConstraintModel attributeModel, TokenResolver tokenResolver) {
				ForeignKeyConstraintModel foreignKey = (ForeignKeyConstraintModel) attributeModel;
				List<Token> tokens = Lists.newArrayList();
				addConstraintNameDefinition(foreignKey, tokens);
				tokens.add(Keyword.FOREIGN);
				tokens.add(Keyword.KEY);
				addColumnList(context, tokens, foreignKey.getKeyColumns());
				tokens.add(Keyword.REFERENCES);
				
				DatabaseObjectModel referenceEntity =
						DefaultTableModel.findReferencedDatabaseObject(context.getDatabaseObjects(), foreignKey);
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
					DeferrabilityModel deferrability = foreignKey.getDeferrability();
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
		TABLE_CHECK(CheckConstraintModel.class) {
			
			@Override
			public List<Token> emit(JiemamyContext context, ConstraintModel attributeModel, TokenResolver tokenResolver) {
				CheckConstraintModel checkConstraint = (CheckConstraintModel) attributeModel;
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
		 * 属性モデルから出力戦略を取得する。
		 * 
		 * @param constraint 出力対象の属性モデル
		 * @return 出力戦略
		 */
		public static AttributeEmitStrategy fromAttribute(ConstraintModel constraint) {
			for (AttributeEmitStrategy strategy : values()) {
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
				List<EntityRef<? extends ColumnModel>> columnRefs) {
			tokens.add(Separator.LEFT_PAREN);
			for (EntityRef<? extends ColumnModel> columnRef : columnRefs) {
				ColumnModel columnModel = context.resolve(columnRef);
				tokens.add(Identifier.of(columnModel.getName()));
				tokens.add(Separator.COMMA);
			}
			
			if (columnRefs.isEmpty() == false) {
				tokens.remove(tokens.size() - 1);
			}
			tokens.add(Separator.RIGHT_PAREN);
		}
		
		private static void addConstraintNameDefinition(ConstraintModel constraint, List<Token> tokens) {
			if (StringUtils.isEmpty(constraint.getName()) == false) {
				tokens.add(Keyword.CONSTRAINT);
				tokens.add(Identifier.of(constraint.getName()));
			}
		}
		

		private final Class<? extends ConstraintModel> clazz;
		

		AttributeEmitStrategy(Class<? extends ConstraintModel> clazz) {
			this.clazz = clazz;
		}
		
		/**
		 * 属性モデルからトークンシーケンスを出力する。
		 * 
		 * @param attributeModel 属性モデル
		 * @param tokenResolver トークンリゾルバ
		 * @param <T> 出力する属性モデルの型
		 * @return トークンシーケンス
		 */
		public abstract <T extends ConstraintModel>List<Token> emit(JiemamyContext context, T attributeModel,
				TokenResolver tokenResolver);
		
	}
	
	/**
	 * エンティティの出力戦略。
	 * 
	 * @author daisuke
	 */
	protected enum EntityEmitStrategy {
		
		/** テーブルの出力戦略 */
		TABLE(TableModel.class) {
			
			@Override
			public SqlStatement emit(JiemamyContext context, DatabaseObjectModel dom, DefaultSqlEmitter sqlEmitter,
					TokenResolver tokenResolver) {
				TableModel tableModel = (TableModel) dom;
				List<Token> tokens = Lists.newArrayList();
				tokens.add(Keyword.CREATE);
				tokens.add(Keyword.TABLE);
				tokens.add(Identifier.of(tableModel.getName()));
				tokens.add(Separator.LEFT_PAREN);
				for (ColumnModel columnModel : tableModel.getColumns()) {
//					if (attributeModel.hasAdapter(Disablable.class)
//							&& Boolean.TRUE.equals(attributeModel.getAdapter(Disablable.class).isDisabled())) {
//						continue;
//					}
					List<Token> columnTokens = sqlEmitter.emitColumn(context, tableModel, columnModel, tokenResolver);
					tokens.addAll(columnTokens);
					tokens.add(Separator.COMMA);
				}
				
				for (ConstraintModel constraintModel : tableModel.getConstraints()) {
//					if (attributeModel.hasAdapter(Disablable.class)
//							&& Boolean.TRUE.equals(attributeModel.getAdapter(Disablable.class).isDisabled())) {
//						continue;
//					}
					AttributeEmitStrategy strategy = AttributeEmitStrategy.fromAttribute(constraintModel);
					if (strategy != null) {
						List<Token> constraintTokens = strategy.emit(context, constraintModel, tokenResolver);
						tokens.addAll(constraintTokens);
						tokens.add(Separator.COMMA);
					}
				}
				
				if (tableModel.getColumns().isEmpty() == false || tableModel.getConstraints().isEmpty() == false) {
					tokens.remove(tokens.size() - 1);
				}
				tokens.add(Separator.RIGHT_PAREN);
				tokens.add(Separator.SEMICOLON);
				return new DefaultSqlStatement(tokens);
			}
			
		},
		
		/** ビューの出力戦略 */
		VIEW(ViewModel.class) {
			
			@Override
			public SqlStatement emit(JiemamyContext context, DatabaseObjectModel entityModel,
					DefaultSqlEmitter defaultSqlEmitter, TokenResolver tokenResolver) {
				ViewModel viewModel = (ViewModel) entityModel;
				List<Token> tokens = Lists.newArrayList();
				tokens.add(Keyword.CREATE);
				tokens.add(Keyword.VIEW);
				tokens.add(Identifier.of(viewModel.getName()));
				tokens.add(Keyword.AS);
				tokens.add(Literal.of(viewModel.getDefinition(), LiteralType.FRAGMENT));
				tokens.add(Separator.SEMICOLON);
				return new DefaultSqlStatement(tokens);
			}
		};
		
		/**
		 * エンティティモデルから出力戦略を取得する。
		 * 
		 * @param entityModel 出力対象のエンティティモデル
		 * @return 出力戦略
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		public static EntityEmitStrategy fromEntity(DatabaseObjectModel entityModel) {
			Validate.notNull(entityModel);
			for (EntityEmitStrategy s : values()) {
				if (s.clazz == entityModel.getClass()) {
					return s;
				}
				for (Class<?> c : entityModel.getClass().getInterfaces()) {
					if (s.clazz == c) {
						return s;
					}
				}
			}
			return null;
		}
		

		private final Class<? extends DatabaseObjectModel> clazz;
		

		EntityEmitStrategy(Class<? extends DatabaseObjectModel> clazz) {
			this.clazz = clazz;
		}
		
		/**
		 * エンティティモデルからToken列を出力する。
		 * 
		 * @param domo エンティティモデル
		 * @param sqlEmitter 
		 * @param tokenResolver トークンリゾルバ
		 * @return トークンシーケンス
		 */
		public abstract SqlStatement emit(JiemamyContext context, DatabaseObjectModel domo,
				DefaultSqlEmitter sqlEmitter, TokenResolver tokenResolver);
		
	}
}
