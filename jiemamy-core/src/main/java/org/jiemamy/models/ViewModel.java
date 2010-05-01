package org.jiemamy.models;

/**
 * Viewを表現するモデル
 * 
 * @author tonouchi
 *
 */
public interface ViewModel extends DBObjectModel {
	
	/**
	 * VIEWを作成するための定義を取得

	 * @return VIEW定義のための構文
	 */
	public String getSQL();
	
	/**
	 * VIEWを作成するための定義を設定する
	 * 
	 * @param sql VIEW定義のための構文
	 */
	public void setSQL(String sql);
	
}
