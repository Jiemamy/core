package org.jiemamy.models;

import java.util.List;

/**
 * Jiemamyがモデル化するデータベースを管理するインターフェース
 * 
 * @author tonouchi
 *
 */
public interface CoreFacetModel extends JiemamyElementModel {
	
	/**
	 * TableModelを追加する
	 * 
	 * @param tableModel 追加対称のインスタンス
	 */
	public void addTable(TableModel tableModel);
	
	/**
	 * TriggerModelを追加する
	 * @param triggerModel 追加対称のインスタンス
	 */
	public void addTrigger(TriggerModel triggerModel);
	
	/**
	 * ViewModelを追加する
	 * 
	 * @param viewModel 追加対称のインスタンス
	 */
	public void addView(ViewModel viewModel);
	
	/**
	 * TalbeModelのリストを取得する
	 * 
	 * @return インスタンスが管理する全テーブル
	 */
	public List<TableModel> getTableModels();
	
	/**
	 * TriggerModelのリストを取得する
	 * 
	 * @return インスタンスが管理する全TRIGGER
	 */
	public List<TriggerModel> getTriggerModels();
	
	/**
	 * ViewModelのリストを取得する
	 * 
	 * @return インスタンスが管理する全VIEW
	 */
	public List<ViewModel> getViewModels();
	
}
