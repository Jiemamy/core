package org.jiemamy.model.attribute;

import java.util.UUID;

import org.jiemamy.model.datatype.DataType;

/**
 * {@link DefaultColumnModel}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Column {
	
	private String name;
	
	private DataType type;
	

	/**
	 * ファクトリの状態に基づいて {@link DefaultColumnModel}のインスタンスを生成する。
	 * 
	 * <p>ENTITY IDは自動生成される。</p>
	 * 
	 * @return 新しい {@link DefaultColumnModel}のインスタンス
	 */
	public DefaultColumnModel build() {
		return build(UUID.randomUUID());
	}
	
	/**
	 * ファクトリの状態に基づいて {@link DefaultColumnModel}のインスタンスを生成する。
	 * 
	 * @param id  ENTITY ID
	 * @return 新しい {@link DefaultColumnModel}のインスタンス
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultColumnModel build(UUID id) {
		DefaultColumnModel columnModel = new DefaultColumnModel(id);
		columnModel.setName(name);
		columnModel.setDataType(type);
		return columnModel;
	}
	
	/**
	 * カラム名を設定する。
	 * 
	 * @param name カラム名
	 * @return this
	 */
	public Column whoseNameIs(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * カラムのデータ型を設定する。
	 * 
	 * @param type データ型
	 * @return this
	 */
	public Column whoseTypeIs(DataType type) {
		this.type = type;
		return this;
	}
	
}
