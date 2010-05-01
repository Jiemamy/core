package org.jiemamy.models;

import java.util.List;

/**
 * 外部キー制約を表現するモデル
 * 
 * @author tonouchi
 *
 */
public interface ForeignKeyModel extends ConstraintModel {
	
	/**
	 * 参照元テーブルの外部キーを追加する
	 * 
	 * @param columnReference 追加対称の参照元テーブルのカラム参照
	 */
	public void addColumn(ColumnReferenceModel columnReference);
	
	/**
	 * 参照先テーブルのカラムを追加する
	 * 
	 * @param colunmReference 参照先テーブルのカラム参照
	 */
	public void addColumnToRefer(ColumnReferenceModel colunmReference);
	
	/**
	 * 参照元テーブルの外部キーを取得する
	 * 
	 * @return インスタンスが管理する参照元テーブルカラムへの全参照
	 */
	public List<ColumnReferenceModel> getColumns();
	
	/**
	 * 参照先テーブルのカラムを取得する
	 * 
	 * @return インスタンスが管理する参照先テーブルのカラム全参照
	 */
	public List<ColumnReferenceModel> getColumnsToRefer();
	
	/**
	 * 参照先テーブルへの参照を取得する
	 * 
	 * @return インスタンスが管理するテーブルへの参照
	 */
	public TableReferenceModel getTableToRefer();
	
	/**
	 * 参照先テーブルへの参照を設定する
	 * 
	 * @param tableReference 参照先テーブルへの参照
	 */
	public void setTableToRefer(TableReferenceModel tableReference);
	
}
