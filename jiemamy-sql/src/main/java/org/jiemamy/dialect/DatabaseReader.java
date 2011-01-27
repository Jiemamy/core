/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/07/18
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
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.sql.metadata.KeyMeta;
import org.jiemamy.utils.sql.metadata.TableMeta;
import org.jiemamy.utils.sql.metadata.TypeSafeDatabaseMetaData;
import org.jiemamy.utils.sql.metadata.TypeSafeResultSet;
import org.jiemamy.utils.visitor.AbstractTypeSafeResultSetVisitor;
import org.jiemamy.utils.visitor.ForEachUtil;
import org.jiemamy.utils.visitor.ForEachUtil.TypeSafeResultSetVisitor;

/**
 * データベースを読み込む。
 * 
 * <p>model, dialect, reader, database の関係を人間のメタファで説明しようとすると、
 * 方言というのは、リソース(読み出し対象)の性質である為、読み手が知っていれば読めるもの。
 * 方言を知っている読み手に対して、リソースを与えると、方言に応じて様々な情報を読み取る。
 * readerはこの「読み手」であり、databaseは「リソース」、modelは「様々な情報」である。</p>
 * 
 * @author daisuke
 */
class DatabaseReader {
	
	// THINK DBを読み込んだ結果、modelを「出力する」主体はReaderである為、現在の実装のようにDialectに丸投げ委譲、
	// というのは責務の重心がおかしい。Dialectは必要な部分のみReaderから問い合わせられるだけの立場であり、
	// 基本的にモデルを組み立てる責務はReader側にあるものではないか？
	
	private final DatabaseMetadataParser parser;
	
	private final ParseMetadataConfig config;
	
	private final TypeSafeDatabaseMetaData metaData;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param parser SQL方言
	 * @param config コンテキスト情報
	 * @param meta DBメタデータ
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public DatabaseReader(DatabaseMetadataParser parser, ParseMetadataConfig config, DatabaseMetaData meta) {
		Validate.notNull(parser);
		Validate.notNull(config);
		Validate.notNull(meta);
		this.parser = parser;
		this.config = config;
		metaData = new TypeSafeDatabaseMetaData(meta);
	}
	
	/**
	 * DB接続からエンティティを読み込む。
	 * 
	 * @param <T> 読み込んだ結果、戻り値型
	 * @param visitor 各エンティティに情報に対するビジター
	 * @return ビジターに依存した、実行結果
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>T readEnities(TypeSafeResultSetVisitor<TableMeta, T, RuntimeException> visitor) throws SQLException {
		assert parser != null;
		assert config != null;
		assert metaData != null;
		Validate.notNull(visitor);
		
		TypeSafeResultSet<TableMeta> tables = metaData.getTables(null, config.getSchema(), "%", null);
		T result = ForEachUtil.accept(tables, visitor);
		
		return result;
	}
	
	/**
	 * DB接続からリレーションを読み込む。
	 * 
	 * @param <T> 読み込んだ結果、戻り値型
	 * @param visitor 各エンティティに情報に対するビジター
	 * @return ビジターに依存した、実行結果
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>Collection<T> readRelations(TypeSafeResultSetVisitor<KeyMeta, T, RuntimeException> visitor)
			throws SQLException {
		assert parser != null;
		assert config != null;
		Validate.notNull(visitor);
		
		Collection<String> entityNames = readEnities(new EntityNamesVisitor());
		
		List<T> result = Lists.newArrayList();
		for (String entityName : entityNames) {
			TypeSafeResultSet<KeyMeta> importedKeys = metaData.getImportedKeys(null, config.getSchema(), entityName);
			result.add(ForEachUtil.accept(importedKeys, visitor));
		}
		return result;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * エンティティ名のリストを取得するビジター。
	 * 
	 * @author daisuke
	 */
	private static class EntityNamesVisitor extends
			AbstractTypeSafeResultSetVisitor<TableMeta, Collection<String>, RuntimeException> {
		
		public Collection<String> visit(TableMeta tableMeta) {
			finalResult.add(tableMeta.tableName);
			return null;
		}
		
		@Override
		protected void init() {
			finalResult = Lists.newArrayList();
		}
		
	}
}
