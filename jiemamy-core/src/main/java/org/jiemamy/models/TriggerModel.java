package org.jiemamy.models;

/**
 * トリガーを表現するモデル
 * 
 * @author tonouchi
 *
 */
public interface TriggerModel extends DBObjectModel {
	
	/**
	 * トリガーが紐づけられたテーブルへの参照を取得する
	 * 
	 * @return トリガーを紐づけたテーブルへの参照
	 */
	public TableReferenceModel getTable();
	
	/**
	 * モデルが保持するトリガー作成スクリプトを取得する
	 * 
	 * @return トリガー作成スクリプト
	 * 
	 * @since 0.3
	 */
	public String getTriggetSctipt();
	
	/**
	 * トリガーが紐づけられたテーブルへの参照を設定する
	 * 
	 * @param tableReference トリガーを紐づけたいテーブルへの参照
	 */
	public void setTable(TableReferenceModel tableReference);
	
	/**
	 * トリガーを作成するためのスクリプトを設定する
	 * 
	 * @param triggerScript トリガー設定スクリプト
	 * 
	 * @since 0.3
	 */
	public void setTriggetScript(String triggerScript);
	
}
