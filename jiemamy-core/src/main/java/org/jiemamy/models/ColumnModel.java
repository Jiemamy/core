package org.jiemamy.models;

/**
 * カラムを表現するモデル
 * 
 * @author tonouchi
 *
 * @param <T> カラムの型
 */
public interface ColumnModel<T> extends DBObjectModel {
	
	/**
	 * カラムに設定された値を取得する
	 * 
	 * @return インスタンスが保持する値
	 */
	public T getValue();
	
	/**
	 * そのカラムがNULL可能であるかを取得する
	 * 
	 * @return true-NULL可能
	 */
	public boolean isNullable();
	
	/**
	 * そのカラムがNULL可能かを設定する
	 * 
	 * @param nullable true-NULL可能
	 */
	public void setNullable(boolean nullable);
	
	/**
	 * カラムに値を設定する
	 * 
	 * @param value 値
	 */
	public void setValue(T value);
	
}
