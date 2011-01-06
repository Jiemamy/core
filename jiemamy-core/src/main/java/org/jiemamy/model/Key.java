package org.jiemamy.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 型パラメータのキー。
 * 
 * @param <T> 値の型
 * @author daisuke
 */
public class Key<T> {
	
	private final String keyString;
	
	private static Logger logger = LoggerFactory.getLogger(Key.class);
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param keyString キー文字列
	 */
	public Key(String keyString) {
		this.keyString = keyString;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Key<?> other = (Key<?>) obj;
		return keyString.equals(other.keyString);
	}
	
	public String getKeyString() {
		return keyString;
	}
	
	@Override
	public int hashCode() {
		return keyString.hashCode();
	}
}
