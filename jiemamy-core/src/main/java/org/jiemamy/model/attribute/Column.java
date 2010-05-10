package org.jiemamy.model.attribute;

import java.util.UUID;

import org.jiemamy.model.AbstractEntityFactory;
import org.jiemamy.model.datatype.DataType;

/**
 * {@link DefaultColumnModel}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Column extends AbstractEntityFactory<DefaultColumnModel> {
	
	String name;
	
	DataType type;
	

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
