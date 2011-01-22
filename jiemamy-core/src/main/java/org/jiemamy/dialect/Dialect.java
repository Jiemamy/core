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
package org.jiemamy.dialect;

import java.util.Collection;
import java.util.List;

import org.jiemamy.model.datatype.TypeReference;
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
	
	/**
	 * データ型名を全て取得する。
	 * 
	 * <p>必ず要素数が{@code 1}以上のリストを返さなければならない。</p>
	 * 
	 * @return データ型名のリスト
	 * @since 0.3
	 */
	List<TypeReference> getAllTypeReferences();
	
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
	 * TODO for daisuke
	 * 
	 * @return
	 */
	SqlEmitter getSqlEmitter();
	
	/**
		 * 指定した{@link TypeReference}に対する {@link TypeParameterSpec}の集合を取得する。
		 * 
		 * <p>例えば、VARCHARに対して、どんなパラメータ仕様がありますか？ → SIZE が REQUIRED です<br/>
		 * のような感じ。</p>
		 * 
		 * @param reference {@link TypeReference}
		 * @return {@link TypeReference}に対する {@link TypeParameterSpec}の集合
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
	Collection<TypeParameterSpec> getTypeParameterSpecs(TypeReference reference);
	
	/**
	 * モデルのバリデータを取得する。
	 * 
	 * @return モデルのバリデータ
	 * @since 0.2
	 */
	Validator getValidator();
	
	TypeReference normalize(TypeReference in);
	
	/**
	 * SQL方言IDを返す。
	 * 
	 * @return SQL方言ID
	 * @since 0.2
	 */
	String toString();
	
	String toString(TypeReference typeReference);
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
//	 * SQL文のリストから、{@link JiemamyContext} を生成する。
//	 * 
//	 * @param statements SQL文のリスト
//	 * @return 生成した{@link RootModel}
//	 * @throws UnsupportedOperationException 実装がこの機能を提供していない場合
//	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
//	 * @since 0.2
//	 */
//	JiemamyContext parseStatements(List<SqlStatement> statements);
}
