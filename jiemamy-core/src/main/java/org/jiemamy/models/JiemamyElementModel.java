package org.jiemamy.models;

import java.util.UUID;

/**
 * Jiemamyで使用する各種modelのルートインターフェース
 * 
 * @author tonouchi
 *
 */
public interface JiemamyElementModel {
	
	/**
	 * インスタンスに設定されたIDを取得する
	 * 
	 * @return インスタンスのID
	 */
	public UUID getId();
	
	/**
	 * インスタンスにIDを設定する
	 * 
	 * @param id インスタンスのID
	 */
	public void setId(UUID id);
	
}
