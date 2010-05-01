package org.jiemamy.models;

import java.util.List;

/**
 * テーブルを表現するモデル
 * 
 * @author tonouchi
 *
 */
public interface TableModel extends DBObjectModel {
	
	/**
	 * テーブルにカラムを追加する
	 * 
	 * @param columnModel 追加対象となるカラム
	 */
	public void addColumn(ColumnModel<?> columnModel);
	
	/**
	 * 外部キー制約を追加する
	 * 
	 * @param moreignKeyModel 追加対象となる外部キー制約
	 */
	public void addForeignKey(ForeignKeyModel moreignKeyModel);
	
	/**
	 * テーブルにユニークキー制約を追加する
	 * 
	 * @param uniqueKeyModel 追加対象となるユニークキー制約
	 */
	public void addUniqueKey(UniqueKeyModel uniqueKeyModel);
	
	/**
	 * テーブルが保持するカラムの一覧を取得する
	 * 
	 * @return テーブルが保持する全カラム
	 */
	public List<ColumnModel<?>> getColumns();
	
	/**
	 * テーブルが保持する外部キー制約の一覧を取得する
	 * 
	 * @return テーブルが保持する全外部キー制約
	 */
	public List<ForeignKeyModel> getForeignKeyModels();
	
	/**
	 * テーブルの主キー制約を取得する
	 * 
	 * @return 主キー制約
	 */
	public PrimaryKeyModel getPrimaryKey();
	
	/**
	 * テーブルが保持するユニークキー制約の一覧を取得する
	 * 
	 * @return テーブルが保持する全ユニークキー制約
	 */
	public List<UniqueKeyModel> getUniqueKeys();
	
	/**
	 * テーブルの主キー制約を設定する
	 * 
	 * @param primaryKeyModel 主キー制約
	 */
	public void setPrimaryKey(PrimaryKeyModel primaryKeyModel);
	
}
