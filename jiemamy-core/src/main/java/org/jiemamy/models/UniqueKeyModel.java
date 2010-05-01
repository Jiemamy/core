package org.jiemamy.models;

import java.util.List;

/**
 * ユニークキー制約を表現するモデル
 * 
 * @author tonouchi
 *
 */
public interface UniqueKeyModel extends ConstraintModel {
	
	/**
	 * ユニークキー制約にカラムを追加する
	 * 
	 * @param columnReference ユニークキー制約対象となるカラムへの参照
	 */
	public void addColumn(ColumnReferenceModel columnReference);
	
	/**
	 * ユニークキー制約のカラム一覧を取得する
	 * 
	 * @return ユニークキー制約となったカラムへの全参照
	 */
	public List<ColumnReferenceModel> getColumns();
	
}
