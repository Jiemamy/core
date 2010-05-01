package org.jiemamy.models;

import java.util.List;

/**
 * 主キー制約を表現するモデル
 * 
 * @author tonouchi
 *
 */
public interface PrimaryKeyModel extends ConstraintModel {
	
	/**
	 * 主キー制約にカラムを追加する
	 * 
	 * @param columnReference 主キー制約となるカラムへの参照
	 */
	public void addColumn(ColumnReferenceModel columnReference);
	
	/**
	 * 主キー制約のカラム一覧を取得する
	 * 
	 * @return 主キー制約として設定されたカラムへの全参照
	 */
	public List<ColumnReferenceModel> getColumns();
	
}
