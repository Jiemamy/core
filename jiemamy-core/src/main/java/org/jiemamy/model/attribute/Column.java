package org.jiemamy.model.attribute;

import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntityFactory;
import org.jiemamy.model.datatype.TypeVariant;

/**
 * {@link DefaultColumnModel}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Column extends AbstractEntityFactory<DefaultColumnModel> {
	
	String name;
	
	TypeVariant type;
	

	/**
	 * インスタンスを生成する。
	 */
	public Column() {
		// noop
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name カラム名
	 */
	public Column(String name) {
		this.name = name;
	}
	
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
	public Column whoseTypeIs(TypeVariant type) {
		this.type = type;
		return this;
	}
	
}
