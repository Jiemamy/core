/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.dialect;

import org.jiemamy.validator.Validator;

/**
 * SQL方言インターフェイス。
 * 
 * <p>実装クラスは、引数無しのデフォルトコンストラクタを持たなければならない。
 * また、実装クラスはステートレスである必要がある。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface Dialect {
	
//	/**
//	 * 指定したデータ型に適合する、最も適したモールドを取得する。
//	 * 
//	 * @param dataType データ型
//	 * @return モールド
//	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
//	 * @since 0.2
//	 * @deprecated use {@link #getMoldManager()} and {@link MoldManager#findDataTypeMold(BuiltinDataType)}
//	 */
//	@Deprecated
//	BuiltinDataTypeMold findDataTypeMold(BuiltinDataType dataType);
//	
//	/**
//	 * 指定したカテゴリに最も適したモールドを取得する。
//	 * 
//	 * <p>typeNameには、{@code category.name()}を用いる。</p>
//	 * 
//	 * <p>{@code null}を返してはならない。</p>
//	 * 
//	 * @param category 型カテゴリ
//	 * @return モールド
//	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
//	 * @see #findDataTypeMold(DataTypeCategory, String)
//	 * @since 0.2
//	 * @deprecated use {@link #getMoldManager()} and {@link MoldManager#findDataTypeMold(DataTypeCategory)}
//	 */
//	@Deprecated
//	BuiltinDataTypeMold findDataTypeMold(DataTypeCategory category);
//	
//	/**
//	 * 指定したカテゴリ、型名に最も適したモールドを取得する。
//	 * 
//	 * <p><ol>
//	 *   <li>カテゴリと型名が完全一致するモールドがあれば、それを返す。</li>
//	 *   <li>見つからなければ、次に、型名が完全一致するモールドがあれば、それを返す。</li>
//	 *   <li>さらに見つからなければ、カテゴリが一致する中で、先に定義されているモールドを返す。</li>
//	 *   <li>与えられたカテゴリに該当する型がひとつもマッピングされていない場合は、 {@link BuiltinDataTypeMold#UNKNOWN}を返す。</li>
//	 * </ol></p>
//	 * 
//	 * <p>{@code null}を返してはならない。</p>
//	 * 
//	 * @param category 型カテゴリ
//	 * @param typeName 型名
//	 * @return モールド
//	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
//	 * @since 0.2
//	 * @deprecated use {@link #getMoldManager()} and {@link MoldManager#findDataTypeMold(DataTypeCategory, String)}
//	 */
//	@Deprecated
//	BuiltinDataTypeMold findDataTypeMold(DataTypeCategory category, String typeName);
//	
//	/**
//	 * データ型名を全て取得する。
//	 * 
//	 * <p>{@code null}を返してはならない。</p>
//	 * 
//	 * @return データ型名のリスト
//	 * @since 0.2
//	 * @deprecated use {@link #getMoldManager()} and {@link MoldManager#getTypeList()}
//	 */
//	@Deprecated
//	List<BuiltinDataTypeMold> getAllDataTypes();
	
	/**
	 * JDBC接続URLの雛形文字列を取得する。
	 * 
	 * @return JDBC接続URLの雛形文字列
	 * @since 0.2
	 */
	String getConnectionUriTemplate();
	
//	/**
//	 * モデリング用DataType・一般型・型名間のマッピング情報を取得する。
//	 * 
//	 * <p>{@code null}を返してはならない。</p>
//	 * 
//	 * @return マッピング情報
//	 * @since 0.2
//	 */
//	DataTypeResolver getDataTypeResolver();
//	
	/**
	 * SQL方言名を取得する。
	 * 
	 * @return SQL方言名
	 * @since 0.2
	 */
	String getName();
	
	/**
	 * モデルのバリデータを取得する。
	 * 
	 * <p>{@code null}を返してはならない。</p>
	 * 
	 * @return モデルのバリデータ
	 * @since 0.2
	 */
	Validator getValidator();
	
//	/**
//	 * エンティティ情報から{@link EntityModel}を生成する。
//	 * 
//	 * @param rootModel インポート先の{@link RootModel}
//	 * @param meta DBメタデータ
//	 * @param config インポート設定
//	 * @param importedEntities インポートされたエンティティ情報を格納する{@link Map}
//	 * @param importedForeignKeys インポートされた外部キー情報を格納する{@link Map}
//	 * @throws SQLException SQLの実行に失敗した場合
//	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
//	 * @throws UnsupportedOperationException DBからのインポートをサポートしていない場合
//	 * @since 0.2
//	 */
//	void importMetadata(RootModel rootModel, DatabaseMetaData meta, ImportMetadataConfig config,
//			Map<String, EntityModel> importedEntities, Map<String, ForeignKey> importedForeignKeys) throws SQLException;
//	
//	/**
//	 * SQL文のリストから、{@link RootModel} を生成する。
//	 * 
//	 * <p>{@code null}を返してはならない。</p>
//	 * 
//	 * @param statements SQL文のリスト
//	 * @return 生成した{@link RootModel}
//	 * @throws UnsupportedOperationException 実装がこの機能を提供していない場合
//	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
//	 * @since 0.2
//	 */
//	RootModel parseStatements(List<SqlStatement> statements);
	
	/**
	 * SQL方言IDを返す。
	 * 
	 * <p>{@code null}を返してはならない。</p>
	 * 
	 * @return SQL方言ID
	 * @since 0.2
	 */
	String toString();
}
